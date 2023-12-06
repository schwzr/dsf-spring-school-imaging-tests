package dev.dsf.process.tutorial.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import dev.dsf.bpe.delegate.AbstractServiceDelegate;
import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import dev.dsf.fhir.client.FhirWebserviceClientProvider;
import dev.dsf.fhir.task.TaskHelper;

public class HelloDic extends AbstractServiceDelegate
{
	public HelloDic(FhirWebserviceClientProvider clientProvider, TaskHelper taskHelper,
			ReadAccessHelper readAccessHelper)
	{
		super(clientProvider, taskHelper, readAccessHelper);
	}

	@Override
	protected void doExecute(DelegateExecution execution)
	{

	}
}
