package dev.dsf.process.tutorial.service;

import dev.dsf.process.tutorial.ConstantsTutorial;
import org.apache.http.util.EntityUtils;
import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Variables;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.constants.NamingSystems;
import dev.dsf.bpe.v1.variables.Target;
import dev.dsf.bpe.v1.variables.Variables;
import jakarta.ws.rs.core.MediaType;

import static dev.dsf.process.tutorial.ConstantsTutorial.*;

public class LoadImageDataService extends AbstractServiceDelegate {
    private static final Logger logger = LoggerFactory.getLogger(LoadImageDataService.class);

    public LoadImageDataService(ProcessPluginApi api) {
        super(api);
    }

    @Override
    protected void doExecute(DelegateExecution execution, Variables variables)
    {
        Optional<String> sourceInputParameter = api.getTaskHelper().getFirstInputParameterStringValue(
                variables.getStartTask(), CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_SOURCE);

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(new HttpGet(sourceInputParameter.map(url -> url.startsWith("https://") ? url : "https://download.schwzr.de/testfile.bin").get()));
            byte[] data = EntityUtils.toByteArray(response.getEntity());
            String dicomDataReference = storeBinary(data, ConstantsTutorial.TUTORIAL_COS_ORGANIZATION_IDENTIFIER);
            variables.setString("dicom-data-reference", dicomDataReference);
            logger.info("Inserted into DSF Fhir server, reference {}", dicomDataReference);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        logger.info("Executed '{}' ",
                    this.getClass().getSimpleName());


    }

    private String storeBinary(byte[] content, String dmsIdentifier)
    {
        MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM);
        String securityContext = getSecurityContext(dmsIdentifier);

        try (InputStream in = new ByteArrayInputStream(content))
        {
            IdType created = api.getFhirWebserviceClientProvider().getLocalWebserviceClient().withMinimalReturn()
                    .withRetry(ConstantsTutorial.DSF_CLIENT_RETRY_6_TIMES, ConstantsTutorial.DSF_CLIENT_RETRY_INTERVAL_5MIN)
                    .createBinary(in, mediaType, securityContext);
            return new IdType(api.getFhirWebserviceClientProvider().getLocalWebserviceClient().getBaseUrl(),
                    ResourceType.Binary.name(), created.getIdPart(), created.getVersionIdPart()).getValue();
        }
        catch (Exception exception)
        {
            logger.warn("Could not create binary - {}", exception.getMessage());
            throw new RuntimeException("Could not create binary - " + exception.getMessage(), exception);
        }
    }

    private String getSecurityContext(String dmsIdentifier)
    {
        return api.getOrganizationProvider().getOrganization(ConstantsTutorial.TUTORIAL_COS_ORGANIZATION_IDENTIFIER)
                .orElseThrow(() -> new RuntimeException("Could not find organization with id '" + dmsIdentifier + "'"))
                .getIdElement().toVersionless().getValue();
    }

}
