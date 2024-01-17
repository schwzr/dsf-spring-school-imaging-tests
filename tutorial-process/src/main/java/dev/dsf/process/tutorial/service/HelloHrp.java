package dev.dsf.process.tutorial.service;

import static dev.dsf.bpe.v1.constants.BpmnExecutionVariables.TARGET;

import java.util.Optional;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Variables;
import dev.dsf.bpe.variables.TargetImpl;
import dev.dsf.bpe.variables.TargetValues;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloHrp extends AbstractServiceDelegate
{
	private static final Logger logger = LoggerFactory.getLogger(HelloHrp.class);

	public HelloHrp(ProcessPluginApi api)
	{
		super(api);
	}

	@Override
	protected void doExecute(DelegateExecution execution, Variables variables)
	{
		Optional<String> tutorialInputParameter = api.getTaskHelper().getFirstInputParameterStringValue(
				variables.getStartTask(), "http://dsf.dev/fhir/CodeSystem/tutorial",
				"tutorial-input");
		boolean sendResponse = tutorialInputParameter.map("send-response"::equals).orElse(false);
		execution.setVariable("sendResponse", org.camunda.bpm.engine.variable.Variables.booleanValue(sendResponse));

		if (sendResponse)
		{
			TargetImpl target = new TargetImpl("Test_DIC", "Test_DIC_Endpoint", "https://dic/fhir", null);
			execution.setVariable(TARGET, TargetValues.create(target));
		}
		else
		{
			logger.info("Not sending response to organization with identifier 'Test_DIC'");
		}
	}
}