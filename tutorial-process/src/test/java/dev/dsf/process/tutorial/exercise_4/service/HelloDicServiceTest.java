package dev.dsf.process.tutorial.exercise_4.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.value.BooleanValue;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.service.TaskHelper;
import dev.dsf.bpe.v1.variables.Variables;
import dev.dsf.bpe.variables.TargetValues;
import dev.dsf.bpe.v1.constants.*;
import dev.dsf.process.tutorial.service.HelloDic;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HelloDicServiceTest
{

	@Mock
	private TaskHelper taskHelper;

	@Mock
	private DelegateExecution execution;

	@Mock
	private ProcessPluginApi api;

	@Mock
	private Variables variables;

	private Optional<Constructor<HelloDic>> getConstructor(Class<?>... args)
	{
		try
		{
			return Optional.of(HelloDic.class.getConstructor(args));
		}
		catch (NoSuchMethodException e)
		{
			return Optional.empty();
		}
		catch (SecurityException e)
		{
			throw e;
		}
	}

	@Test
	public void testHelloDicConstructorWithAdditionalBooleanParameterExists() throws Exception
	{
		Optional<Constructor<HelloDic>> constructor = getConstructor(ProcessPluginApi.class, boolean.class);

		if (constructor.isEmpty())
		{
			String errorMessage = "One public constructor in class " + HelloDic.class.getSimpleName()
					+ " with parameters (" + ProcessPluginApi.class.getSimpleName() + ") expected";
			fail(errorMessage);
		}
	}

	private Optional<HelloDic> getInstance(List<Class<?>> types, Object... args)
	{
		try
		{
			return Optional.of(HelloDic.class.getConstructor(types.toArray(Class[]::new))).map(c ->
			{
				try
				{
					return c.newInstance(args);
				}
				catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e)
				{
					throw new RuntimeException(e);
				}
			});
		}
		catch (NoSuchMethodException e)
		{
			return Optional.empty();
		}
		catch (SecurityException e)
		{
			throw e;
		}
	}

	@Test
	public void testHelloDicServiceDoExecute() throws Exception
	{
		Optional<HelloDic> optService = getInstance(Arrays.asList(ProcessPluginApi.class));

		assumeTrue(optService.isPresent());

		Task task = getTask();
		Mockito.when(variables.getStartTask()).thenReturn(task);
		Mockito.when(taskHelper.getFirstInputParameterStringValue(any(),
				eq("http://dsf.dev/fhir/CodeSystem/tutorial"), eq("tutorial-input")))
				.thenReturn(Optional.of("Test"));

		optService.get().execute(execution);

		ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
		Mockito.verify(taskHelper).getFirstInputParameterStringValue(captor.capture(),
				eq("http://dsf.dev/fhir/CodeSystem/tutorial"), eq("tutorial-input"));
		Mockito.verify(variables, atLeastOnce()).getStartTask();
		assertEquals(task, captor.getValue());

		ArgumentCaptor<TargetValues.TargetValue> targetArgumentCaptor = ArgumentCaptor.forClass(TargetValues.TargetValue.class);
		Mockito.verify(variables).setVariable(eq(BpmnExecutionVariables.TARGET),targetArgumentCaptor.capture());
		assertEquals("Test_COS", targetArgumentCaptor.getValue().getValue().getOrganizationIdentifierValue());
		assertEquals("Test_COS_Endpoint", targetArgumentCaptor.getValue().getValue().getEndpointIdentifierValue());
		assertEquals("https://cos/fhir", targetArgumentCaptor.getValue().getValue().getEndpointUrl());

		Mockito.verify(variables).setVariable(anyString(), any(BooleanValue.class));
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
