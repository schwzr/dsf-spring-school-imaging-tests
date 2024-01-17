package dev.dsf.process.tutorial.service;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractServiceDelegate;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.dsf.fhir.authorization.read.ReadAccessHelper;

public class HelloDic extends AbstractServiceDelegate
{

	private static final Logger logger = LoggerFactory.getLogger(HelloDic.class);
	public HelloDic(ProcessPluginApi api)
	{
		super(api);
	}

	@Override
	protected void doExecute(DelegateExecution execution, Variables variables) {
		logger.info("Hello Dic from organization '{}'", variables.getStartTask().getRestriction()
				.getRecipientFirstRep().getIdentifier().getValue());
	}
}
