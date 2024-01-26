package dev.dsf.process.tutorial.service;

import static dev.dsf.bpe.v1.constants.BpmnExecutionVariables.TARGET;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.variables.Target;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;

public class HelloCos extends AbstractServiceDelegate
{
	public HelloCos(ProcessPluginApi api)
	{
		super(api);
	}

	@Override
	protected void doExecute(DelegateExecution execution, Variables variables)
	{
		Target target = variables.createTarget("Test_HRP", "Test_HRP_Endpoint", "https://hrp/fhir", null);
		variables.setTarget(target);
	}
}
