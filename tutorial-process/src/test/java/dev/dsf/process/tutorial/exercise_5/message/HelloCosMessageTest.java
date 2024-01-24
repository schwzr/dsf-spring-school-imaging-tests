package dev.dsf.process.tutorial.exercise_5.message;


import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_AND_LATEST_VERSION;
import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_MESSAGE_NAME;
import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_COS_INSTANTIATES_CANONICAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.variables.Variables;
import dev.dsf.bpe.variables.TargetImpl;
import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import dev.dsf.bpe.v1.service.FhirWebserviceClientProvider;
import dev.dsf.bpe.v1.service.OrganizationProvider;
import dev.dsf.bpe.v1.service.TaskHelper;
import dev.dsf.bpe.v1.variables.Target;
import dev.dsf.bpe.v1.constants.*;
import dev.dsf.process.tutorial.ConstantsTutorial;
import dev.dsf.process.tutorial.message.HelloCosMessage;
import dev.dsf.fhir.client.FhirWebserviceClient;
import dev.dsf.fhir.client.PreferReturnMinimalWithRetry;
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

@RunWith(MockitoJUnitRunner.class)
public class HelloCosMessageTest
{

	@Mock
	private TaskHelper taskHelper;

	@Mock
	private DelegateExecution execution;

	@Mock
	private ProcessPluginApi api;

	@Mock
	private Variables variables;

	private class MockableHelloCosMessage extends HelloCosMessage {

		public MockableHelloCosMessage(ProcessPluginApi api)
		{
			super(api);
		}

		@Override
		public Stream<ParameterComponent> getAdditionalInputParameters(DelegateExecution execution,
				Variables variables)
		{
			return super.getAdditionalInputParameters(execution, variables);
		}
	}

	@Test
	public void testGetAdditionalInputParameters() throws Exception
	{

		MockableHelloCosMessage messageDelegate = new MockableHelloCosMessage(api);

		Mockito.when(api.getTaskHelper()).thenReturn(taskHelper);

		Mockito.when(variables.getLatestTask()).thenReturn(getTask());

		Mockito.when(taskHelper.getFirstInputParameterStringValue(any(),
				eq("http://dsf.dev/fhir/CodeSystem/tutorial"), eq("tutorial-input")))
				.thenReturn(Optional.of("Test"));

		Mockito.when(taskHelper.createInput(any(Type.class), eq("http://dsf.dev/fhir/CodeSystem/tutorial"), eq("tutorial-input")))
				.thenReturn(new ParameterComponent(
						new CodeableConcept(
								new Coding("http://dsf.dev/fhir/CodeSystem/tutorial", "tutorial-input", null)),
						new StringType("Test")));

		Stream<ParameterComponent> testParameterComponents = messageDelegate.getAdditionalInputParameters(execution, variables);

		Mockito.verify(variables).getLatestTask();
		Mockito.verify(taskHelper).createInput(any(Type.class), anyString(), anyString());

		ParameterComponent tutorialInput = testParameterComponents
				.filter(parameterComponent -> ((StringType) parameterComponent.getValue()).getValue().equals("Test"))
				.findFirst().get();
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