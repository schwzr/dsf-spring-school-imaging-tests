package dev.dsf.process.tutorial.message;

import java.util.Optional;
import java.util.stream.Stream;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractTaskMessageSend;
import dev.dsf.bpe.v1.variables.Variables;

// Only needed for exercise 6 and above
public class HelloHrpMessage extends AbstractTaskMessageSend
{
	public HelloHrpMessage(ProcessPluginApi api)
	{
		super(api);
	}

	@Override
	protected Stream<Task.ParameterComponent> getAdditionalInputParameters(DelegateExecution execution,
			Variables variables)
	{
		Optional<Task.ParameterComponent> tutorialInputParameter = api.getTaskHelper().getFirstInputParameter(
				variables.getStartTask(), "http://dsf.dev/fhir/CodeSystem/tutorial", "tutorial-input",
				StringType.class);

		return tutorialInputParameter.map(i -> api.getTaskHelper().createInput(i.getValue(),
				"http://dsf.dev/fhir/CodeSystem/tutorial", "tutorial-input")).stream();
	}
}
