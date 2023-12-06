package dev.dsf.process.tutorial.message;

import java.util.Optional;
import java.util.stream.Stream;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractTaskMessageSend;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import org.hl7.fhir.r4.model.Task;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Type;

// Only needed for exercise 5 and above
public class HelloHrpMessage extends AbstractTaskMessageSend
{
	public HelloHrpMessage(ProcessPluginApi api)
	{
		super(api);
	}

	@Override
	protected Stream<Task.ParameterComponent> getAdditionalInputParameters(DelegateExecution execution, Variables variables)
	{
		Optional<String> tutorialInputParameter = api.getTaskHelper().getFirstInputParameterStringValue(
				api.getVariables((DelegateExecution) this).getStartTask(), "http://highmed.org/fhir/CodeSystem/tutorial",
				"tutorial-input");

		return tutorialInputParameter.map(
						 i -> api.getTaskHelper().createInput(i , "http://highmed.org/fhir/CodeSystem/tutorial", "tutorial-input"))
				.stream();
	}
}
