package dev.dsf.process.tutorial.exercise_2.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.service.FhirWebserviceClientProvider;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import dev.dsf.bpe.v1.variables.Variables;
import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import dev.dsf.process.tutorial.service.HelloDic;
import dev.dsf.bpe.v1.service.TaskHelper;
import dev.dsf.bpe.v1.constants.*;
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
	private FhirWebserviceClientProvider clientProvider;

	@Mock
	private TaskHelper taskHelper;

	@Mock
	private ReadAccessHelper readAccessHelper;

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
		// not testing all parameter permutations
		Optional<Constructor<HelloDic>> constructor = getConstructor(FhirWebserviceClientProvider.class,
				TaskHelper.class, ReadAccessHelper.class, boolean.class);
		if (constructor.isEmpty())
			constructor = getConstructor(boolean.class, FhirWebserviceClientProvider.class, TaskHelper.class,
					ReadAccessHelper.class);
		if (constructor.isEmpty())
			constructor = getConstructor(FhirWebserviceClientProvider.class, boolean.class, TaskHelper.class,
					ReadAccessHelper.class);
		if (constructor.isEmpty())
			constructor = getConstructor(FhirWebserviceClientProvider.class, TaskHelper.class, boolean.class,
					ReadAccessHelper.class);

		if (constructor.isEmpty())
		{
			String errorMessage = "One public constructor in class " + HelloDic.class.getSimpleName()
					+ " with parameters (" + FhirWebserviceClientProvider.class.getSimpleName() + ", "
					+ TaskHelper.class.getSimpleName() + ", " + ReadAccessHelper.class.getSimpleName()
					+ ", boolean) expected";
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
		// not trying all parameter permutations
		Optional<HelloDic> optService = getInstance(Arrays.asList(FhirWebserviceClientProvider.class, TaskHelper.class,
				ReadAccessHelper.class, boolean.class), clientProvider, taskHelper, readAccessHelper, true);
		if (optService.isEmpty())
			optService = getInstance(Arrays.asList(boolean.class, FhirWebserviceClientProvider.class, TaskHelper.class,
					ReadAccessHelper.class), true, clientProvider, taskHelper, readAccessHelper);
		if (optService.isEmpty())
			optService = getInstance(Arrays.asList(FhirWebserviceClientProvider.class, boolean.class, TaskHelper.class,
					ReadAccessHelper.class), clientProvider, true, taskHelper, readAccessHelper);
		if (optService.isEmpty())
			optService = getInstance(Arrays.asList(FhirWebserviceClientProvider.class, TaskHelper.class, boolean.class,
					ReadAccessHelper.class), clientProvider, taskHelper, true, readAccessHelper);

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
