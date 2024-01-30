package dev.dsf.process.tutorial.exercise_5.message;

import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION;
import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_INSTANTIATES_CANONICAL;
import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import java.util.UUID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Task.ParameterComponent;
import org.hl7.fhir.r4.model.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ca.uhn.fhir.context.FhirContext;
import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.constants.*;
import dev.dsf.bpe.v1.service.FhirWebserviceClientProvider;
import dev.dsf.bpe.v1.service.OrganizationProvider;
import dev.dsf.bpe.v1.service.TaskHelper;
import dev.dsf.bpe.v1.variables.Target;
import dev.dsf.bpe.v1.variables.Variables;
import dev.dsf.bpe.variables.TargetImpl;
import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import dev.dsf.fhir.client.FhirWebserviceClient;
import dev.dsf.fhir.client.PreferReturnMinimalWithRetry;
import dev.dsf.process.tutorial.message.HelloCosMessage;

@RunWith(MockitoJUnitRunner.class)
public class HelloCosMessageTest
{

	@Mock
	private PreferReturnMinimalWithRetry clientWithMinimalReturn;

	@Mock
	private TaskHelper taskHelper;

	@Mock
	private DelegateExecution execution;

	@Mock
	private ProcessPluginApi api;

	@Mock
	private Variables variables;

	private class MockableHelloCosMessage extends HelloCosMessage
	{

		public MockableHelloCosMessage(ProcessPluginApi api)
		{
			super(api);
		}

		@Override
		public void doExecute(DelegateExecution execution, Variables variables) throws Exception
		{
			super.doExecute(execution, variables);
		}

		@Override
		public String getProfile(DelegateExecution execution, Variables variables)
		{
			return getProfile(execution, variables);
		}

		@Override
		public String getInstantiatesCanonical(DelegateExecution execution, Variables variables)
		{
			return getInstantiatesCanonical(execution, variables);
		}
	}

	@Test
	public void testGetAdditionalInputParameters() throws Exception
	{
		MockableHelloCosMessage messageDelegate = new MockableHelloCosMessage(api);

		Mockito.when(variables.getTarget())
				.thenReturn(new TargetImpl("Test_COS", "Test_COS_Endpoint", "https://cos/fhir", null));
		Mockito.when(messageDelegate.getInstantiatesCanonical(execution, variables))
				.thenReturn(PROFILE_TUTORIAL_TASK_HELLO_COS_INSTANTIATES_CANONICAL); // TODO: Figure out why this needs
																						// to be mocked in the first
																						// place
		Mockito.when(variables.getVariable(CodeSystems.BpmnMessage.Codes.MESSAGE_NAME))
				.thenReturn(PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME);
		Mockito.when(messageDelegate.getProfile(execution, variables))
				.thenReturn(PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION); // TODO: Figure out why this needs to
																					// be mocked in the first place
		Mockito.when(variables.getVariable(CodeSystems.BpmnMessage.Codes.BUSINESS_KEY))
				.thenReturn(UUID.randomUUID().toString());
		/*
		 * Mockito.when(clientProvider.getWebserviceClient(anyString())).thenReturn(client);
		 * Mockito.when(client.getBaseUrl()).thenReturn("https://cos/fhir");
		 * Mockito.when(client.withMinimalReturn()).thenReturn(clientWithMinimalReturn);
		 */

		Mockito.when(api.getTaskHelper()).thenReturn(taskHelper);

		Mockito.when(api.getVariables(execution)).thenReturn(variables);

		Mockito.when(variables.getStartTask()).thenReturn(getTask());

		Mockito.when(taskHelper.getFirstInputParameterStringValue(any(), eq("http://dsf.dev/fhir/CodeSystem/tutorial"),
				eq("tutorial-input"))).thenReturn(Optional.of("Test"));

		Mockito.when(taskHelper.createInput(eq(new Reference("http://dsf.dev/fhir/CodeSystem/tutorial")),
				eq("tutorial-input"), eq("Test")))
				.thenReturn(new ParameterComponent(
						new CodeableConcept(
								new Coding("http://dsf.dev/fhir/CodeSystem/tutorial", "tutorial-input", null)),
						new StringType("Test")));

		messageDelegate.execute(execution);

		Mockito.verify(variables).getStartTask();
		ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
		Mockito.verify(clientWithMinimalReturn).create(captor.capture());

		Task sendTask = captor.getValue();
		assertNotNull(sendTask);
		assertEquals(3, sendTask.getInput().size());

		ParameterComponent tutorialInput = sendTask.getInput().get(2);
		assertEquals(1,
				tutorialInput.getType().getCoding().stream()
						.filter(c -> "http://dsf.dev/fhir/CodeSystem/tutorial".equals(c.getSystem()))
						.filter(c -> "tutorial-input".equals(c.getCode())).count());
		assertTrue(tutorialInput.getValue() instanceof StringType);
		assertEquals("Test", ((StringType) tutorialInput.getValue()).getValue());
	}

	private Task getTask()
	{
		Task task = new Task();
		task.getRestriction().addRecipient().getIdentifier().setSystem(NamingSystems.OrganizationIdentifier.SID)
				.setValue("MeDIC");
		task.addInput().setValue(new StringType("Test")).getType().addCoding()
				.setSystem("http://dsf.dev/fhir/CodeSystem/tutorial").setCode("tutorial-input");

		return task;
	}
}
