package dev.dsf.process.tutorial.service;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static dev.dsf.process.tutorial.ConstantsTutorial.*;

public class LogSuccessService extends AbstractServiceDelegate {
    private static final Logger logger = LoggerFactory.getLogger(LogSuccessService.class);

    public LogSuccessService(ProcessPluginApi api) {
        super(api);
    }

    @Override
    protected void doExecute(DelegateExecution execution, Variables variables)
    {
        Optional<Reference> outputDataParameter = api.getTaskHelper().getFirstInputParameterValue(
                variables.getLatestTask(), CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_OUTPUT_DATA_REFERENCE, Reference.class);

        String reference = outputDataParameter.get().getReference();
        logger.info("output data reference {}", reference);

        Task.TaskOutputComponent output = api.getTaskHelper().createOutput(new StringType(reference), CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_OUTPUT_URL);

        variables.updateTask(variables.getStartTask().addOutput(output));

            logger.info("Executed '{}' ",
                    this.getClass().getSimpleName());


    }
}
