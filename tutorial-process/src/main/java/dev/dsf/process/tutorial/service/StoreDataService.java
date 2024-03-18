package dev.dsf.process.tutorial.service;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Target;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static dev.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL;
import static dev.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT;

public class StoreDataService extends AbstractServiceDelegate {
    private static final Logger logger = LoggerFactory.getLogger(StoreDataService.class);

    public StoreDataService(ProcessPluginApi api) {
        super(api);
    }

    @Override
    protected void doExecute(DelegateExecution execution, Variables variables)
    {
        Optional<String> tutorialInputParameter = api.getTaskHelper().getFirstInputParameterStringValue(
                variables.getStartTask(), CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT);


        // todo
        Target target = variables.createTarget("Test_COS", "Test_COS_Endpoint", "https://cos/fhir");
        variables.setTarget(target);

        //variables.setString("tutorial-input", "send-response");


            logger.info("Executed '{}' ",
                    this.getClass().getSimpleName());


    }
}
