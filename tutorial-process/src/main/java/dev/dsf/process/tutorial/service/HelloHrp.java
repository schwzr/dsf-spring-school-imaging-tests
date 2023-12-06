package dev.dsf.process.tutorial.service;

import static dev.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_TARGET;

import java.util.Optional;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.Variables;
import dev.dsf.bpe.delegate.AbstractServiceDelegate;
import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import dev.dsf.fhir.client.FhirWebserviceClientProvider;
import dev.dsf.fhir.task.TaskHelper;
import dev.dsf.fhir.variables.Target;
import dev.dsf.fhir.variables.TargetValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloHrp extends AbstractServiceDelegate
{
	private static final Logger logger = LoggerFactory.getLogger(HelloHrp.class);

	public HelloHrp(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
			ReadAccessHelper readAccessHelper)
	{
		super(clientProvider, taskHelper, readAccessHelper);
	}

	@Override
	protected void doExecute(DelegateExecution execution)
	{
		Optional<String> tutorialInputParameter = getTaskHelper().getFirstInputParameterStringValue(
				getLeadingTaskFromExecutionVariables(), "http://highmed.org/fhir/CodeSystem/tutorial",
				"tutorial-input");
		boolean sendResponse = tutorialInputParameter.map("send-response"::equals).orElse(false);
		execution.setVariable("sendResponse", Variables.booleanValue(sendResponse));

		if (sendResponse)
		{
			Target target = Target.createUniDirectionalTarget("Test_DIC", "Test_DIC_Endpoint", "https://dic/fhir");
			execution.setVariable(BPMN_EXECUTION_VARIABLE_TARGET, TargetValues.create(target));
		}
		else
		{
			logger.info("Not sending response to organization with identifier 'Test_DIC'");
		}
	}
}