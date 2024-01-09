package dev.dsf.process.tutorial.exercise_1.service;

import dev.dsf.bpe.v1.constants.NamingSystems;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import dev.dsf.process.tutorial.service.HelloDic;
import org.hl7.fhir.r4.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HelloDicServiceTest
{
	@Mock
	private DelegateExecution execution;

	@Mock
	private Variables variables;

	@InjectMocks
	private HelloDic service;

	//TODO: I'm assuming this test makes sure the user invoked the appropriate method to get access to the recipient organization identifier from the "leading" (now: starting) task. Needs confirmation if the check is still valid the way it is done here
	@Test
	public void testHelloDicServiceValid() throws Exception
	{
		//Mockito.when(execution.getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK)).thenReturn(getTask());
		Mockito.when(variables.getStartTask()).thenReturn(getTask());

		service.execute(execution);

		//Mockito.verify(execution).getVariable(BPMN_EXECUTION_VARIABLE_LEADING_TASK);
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
