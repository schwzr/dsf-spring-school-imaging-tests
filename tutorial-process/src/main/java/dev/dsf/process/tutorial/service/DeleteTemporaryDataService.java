package dev.dsf.process.tutorial.service;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static dev.dsf.process.tutorial.ConstantsTutorial.*;

public class DeleteTemporaryDataService extends AbstractServiceDelegate {
    private static final Logger logger = LoggerFactory.getLogger(DeleteTemporaryDataService.class);

    public DeleteTemporaryDataService(ProcessPluginApi api) {
        super(api);
    }

    @Override
    protected void doExecute(DelegateExecution execution, Variables variables) {
        Optional<String> tutorialInputParameter = api.getTaskHelper().getFirstInputParameterStringValue(
                variables.getStartTask(), CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT);

        String binaryUrl = variables.getString(DICOM_DATA_REFERENCE);
        IdType binaryId = new IdType(binaryUrl);

        logger.info("Deleting data binary {} permanently", binaryId.getValue());
        api.getFhirWebserviceClientProvider().getLocalWebserviceClient().delete(Binary.class, binaryId.getIdPart());
        api.getFhirWebserviceClientProvider().getLocalWebserviceClient().deletePermanently(Binary.class,
                binaryId.getIdPart());

        logger.info("Executed '{}' ",
                this.getClass().getSimpleName());


    }
}
