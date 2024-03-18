package dev.dsf.process.tutorial.service;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Target;
import dev.dsf.bpe.v1.variables.Variables;
import dev.dsf.fhir.client.FhirWebserviceClient;
import dev.dsf.process.tutorial.ConstantsTutorial;
import jakarta.ws.rs.core.MediaType;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Optional;

import static dev.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL;

public class StoreImageDataService extends AbstractServiceDelegate {
    private static final Logger logger = LoggerFactory.getLogger(StoreImageDataService.class);

    public StoreImageDataService(ProcessPluginApi api) {
        super(api);
    }

    @Override
    protected void doExecute(DelegateExecution execution, Variables variables)
    {

        Task task = variables.getStartTask();
        IdType id = getDataReference(task).get();

        try (InputStream binary = readBinaryResource(id))
        {
            byte[] dicomData = binary.readAllBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(dicomData);
            logger.info("Received data, length: {}", dicomData.length);
            BufferedImage image = ImageIO.read(bais);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.BLUE);
            g.drawRect(10, 10, image.getWidth() - 20, image.getHeight() - 20);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] outputData = baos.toByteArray();
            String outputBinaryReference = storeBinary(outputData, "TEST_DIC");
            variables.setString("output-data-reference", outputBinaryReference);



            Target target = variables.createTarget("Test_DIC", "Test_DIC_Endpoint", "https://dic/fhir");
            variables.setTarget(target);

            variables.updateTask(task);


        }
        catch (Exception e)
        {
            logger.warn("Error while reading Binary resource: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }

            logger.info("Executed '{}' ",
                    this.getClass().getSimpleName());


    }

    private Optional<IdType> getDataReference(Task task)
    {
        return api.getTaskHelper()
                .getFirstInputParameterValue(task, CODESYSTEM_TUTORIAL,
                        ConstantsTutorial.CODESYSTEM_TUTORIAL_VALUE_DICOM_DATA_REFERENCE, Reference.class)
                .map(Reference::getReference).map(IdType::new);
    }

    private InputStream readBinaryResource(IdType binaryId) throws Exception
    {
        final String id = binaryId.getIdPart();
        final String version = binaryId.getVersionIdPart();

        FhirWebserviceClient client = api.getFhirWebserviceClientProvider().getWebserviceClient(binaryId.getBaseUrl());

        logger.info("Reading binary from {} with id {}/{}", client.getBaseUrl(), id, version);

        if (version != null && !version.isEmpty())
            return client.readBinary(id, version, MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM));
        else
            return client.readBinary(id, MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM));
    }

    private String storeBinary(byte[] content, String dmsIdentifier)
    {
        MediaType mediaType = MediaType.valueOf("image/png");
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
