package dev.dsf.process.tutorial.exercise_1.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.constants.NamingSystems;
import dev.dsf.bpe.v1.variables.Variables;
import dev.dsf.process.tutorial.service.HelloDic;

@RunWith(MockitoJUnitRunner.class)
public class HelloDicServiceTest
{
	@Mock
	private DelegateExecution execution;

	@Mock
	private Variables variables;

	@Mock
	private ProcessPluginApi api;

	@InjectMocks
	private HelloDic service;

	// TODO: I'm assuming this test makes sure the user invoked the appropriate method to get access to the recipient
	// organization identifier from the "leading" (now: starting) task. Needs confirmation if the check is still valid
	// the way it is done here
	@Test
	public void testHelloDicServiceValid() throws Exception
	{
		Mockito.when(variables.getStartTask()).thenReturn(getTask());

		Mockito.when(api.getVariables(execution)).thenReturn(variables);

		service.execute(execution);

		Mockito.verify(variables).getStartTask();
	}

	private Task getTask()
	{
		Task task = new Task();
		task.getRestriction().addRecipient().getIdentifier().setSystem(NamingSystems.OrganizationIdentifier.SID)
				.setValue("MeDIC");

		return task;
	}
}
