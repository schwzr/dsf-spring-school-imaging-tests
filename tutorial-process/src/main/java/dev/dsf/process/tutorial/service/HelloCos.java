package dev.dsf.process.tutorial.service;

import static dev.dsf.bpe.ConstantsBase.BPMN_EXECUTION_VARIABLE_TARGET;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import dev.dsf.bpe.delegate.AbstractServiceDelegate;
import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import dev.dsf.fhir.client.FhirWebserviceClientProvider;
import dev.dsf.fhir.task.TaskHelper;
import dev.dsf.fhir.variables.Target;
import dev.dsf.fhir.variables.TargetValues;

public class HelloCos extends AbstractServiceDelegate
{
	public HelloCos(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
			ReadAccessHelper readAccessHelper)
	{
		super(clientProvider, taskHelper, readAccessHelper);
	}

	@Override
	protected void doExecute(DelegateExecution execution)
	{
		Target target = Target.createUniDirectionalTarget("Test_HRP", "Test_HRP_Endpoint", "https://hrp/fhir");
		execution.setVariable(BPMN_EXECUTION_VARIABLE_TARGET, TargetValues.create(target));
	}
}
