package dev.dsf.process.tutorial.exercise_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.junit.Test;

import dev.dsf.bpe.plugin.ProcessIdAndVersion;
import dev.dsf.bpe.v1.ProcessPluginDefinition;
import dev.dsf.bpe.v1.plugin.ProcessPluginImpl;
import dev.dsf.process.tutorial.ConstantsTutorial;
import dev.dsf.process.tutorial.TestProcessPluginGenerator;
import dev.dsf.process.tutorial.TutorialProcessPluginDefinition;
import dev.dsf.process.tutorial.service.DicTask;

public class TutorialProcessPluginDefinitionTest
{
	@Test
	public void testDicProcessBpmnProcessFile() throws Exception
	{
		String filename = "bpe/dic-process.bpmn";
		String processId = "dsfdev_helloDic";

		BpmnModelInstance model = Bpmn
				.readModelFromStream(this.getClass().getClassLoader().getResourceAsStream(filename));
		assertNotNull(model);

		List<Process> processes = model.getModelElementsByType(Process.class).stream()
				.filter(p -> processId.equals(p.getId())).collect(Collectors.toList());
		assertEquals(1, processes.size());

		String errorServiceTask = "Process '" + processId + "' in file '" + filename
				+ "' is missing a ServiceTask with java implementation class '" + DicTask.class.getName() + "'";
		assertTrue(errorServiceTask, processes.get(0).getChildElementsByType(ServiceTask.class).stream()
				.filter(Objects::nonNull).map(ServiceTask::getCamundaClass).anyMatch(DicTask.class.getName()::equals));
	}

	@Test
	public void testDicProcessResources() throws Exception
	{
		String draftTaskFile = "fhir/Task/task-start-dic-process.xml";

		ProcessPluginDefinition definition = new TutorialProcessPluginDefinition();
		ProcessPluginImpl processPlugin = TestProcessPluginGenerator.generate(definition, false, getClass());
		boolean initialized = processPlugin
				.initializeAndValidateResources(ConstantsTutorial.TUTORIAL_DIC_ORGANIZATION_IDENTIFIER);
		assertEquals(true, initialized);

		List<Resource> helloDicResources = processPlugin.getFhirResources().get(new ProcessIdAndVersion(
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC, definition.getResourceVersion()));


		int numExpectedResources = 2;

		if (draftTaskExists(draftTaskFile))
		{
			numExpectedResources = 3;
			String errorDraftTask = "Process is missing Task resource with status 'draft'.";
			assertEquals(errorDraftTask, 1, helloDicResources.stream().filter(r -> r instanceof Task).count());
		}

		assertEquals(numExpectedResources, helloDicResources.size());
	}

	private boolean draftTaskExists(String draftTaskFile)
	{
		return Objects.nonNull(getClass().getClassLoader().getResourceAsStream(draftTaskFile));
	}
}
