package dev.dsf.process.tutorial.message;

import static dev.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL;
import static dev.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT;

import java.util.Optional;
import java.util.stream.Stream;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractTaskMessageSend;
import dev.dsf.bpe.v1.variables.Variables;

import ca.uhn.fhir.context.FhirContext;

// Only needed for exercise 3 and above
public class HelloCosMessage extends AbstractTaskMessageSend
{
	public HelloCosMessage(ProcessPluginApi api)
	{
		super(api);
	}

	@Override
	protected Stream<Task.ParameterComponent> getAdditionalInputParameters(DelegateExecution execution,
			Variables variables)
	{
		Optional<String> tutorialInputParameter = api.getTaskHelper().getFirstInputParameterStringValue(
				variables.getStartTask(), CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT);


		return tutorialInputParameter
				.map(s -> api.getTaskHelper().createInput(new StringType().setValue(s), CODESYSTEM_TUTORIAL, CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT))
				.stream();
	}
}
