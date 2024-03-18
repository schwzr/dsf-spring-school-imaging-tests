package dev.dsf.process.tutorial.message;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractTaskMessageSend;
import dev.dsf.bpe.v1.variables.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;

import java.util.Optional;
import java.util.stream.Stream;

import static dev.dsf.process.tutorial.ConstantsTutorial.*;
import static dev.dsf.process.tutorial.ConstantsTutorial.DICOM_DATA_REFERENCE;

// Only needed for exercise 4 and above
public class SendSuccessMessage extends AbstractTaskMessageSend
{
	public SendSuccessMessage(ProcessPluginApi api)
	{
		super(api);
	}

	@Override
	protected Stream<Task.ParameterComponent> getAdditionalInputParameters(DelegateExecution execution,
			Variables variables)
	{
		Task.ParameterComponent param = new Task.ParameterComponent();
		param.getType().addCoding().setSystem(CODESYSTEM_TUTORIAL)
				.setCode(CODESYSTEM_TUTORIAL_VALUE_OUTPUT_DATA_REFERENCE);
		param.setValue(new Reference().setReference(variables.getString(OUTPUT_DATA_REFERENCE)));

		return Stream.of(param);
	}
}
