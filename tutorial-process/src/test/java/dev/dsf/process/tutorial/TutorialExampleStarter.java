package dev.dsf.process.tutorial;

import static dev.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL;
import static dev.dsf.process.tutorial.ConstantsTutorial.CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT;
import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_DIC_AND_LATEST_VERSION;
import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_DIC_INSTANTIATES_CANONICAL;
import static dev.dsf.process.tutorial.ConstantsTutorial.PROFILE_TUTORIAL_TASK_HELLO_DIC_MESSAGE_NAME;

import java.util.Date;

import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;

import dev.dsf.bpe.start.ExampleStarter;
import dev.dsf.bpe.v1.constants.CodeSystems;
import dev.dsf.bpe.v1.constants.NamingSystems;

public class TutorialExampleStarter
{
	// Environment variable "DSF_CLIENT_CERTIFICATE_PATH" or args[0]: the path to the client-certificate
	// test-data-generator/cert/dic-client/dic-client_certificate.p12
	// Environment variable "DSF_CLIENT_CERTIFICATE_PASSWORD" or args[1]: the password of the client-certificate
	// password
	public static void main(String[] args) throws Exception
	{
		Task task = createStartResource();
		ExampleStarter.forServer(args, "https://dic/fhir").startWith(task);
	}

	private static Task createStartResource()
	{
		Task task = new Task();
		task.getMeta().addProfile(PROFILE_TUTORIAL_TASK_HELLO_DIC_AND_LATEST_VERSION);
		task.setInstantiatesCanonical(PROFILE_TUTORIAL_TASK_HELLO_DIC_INSTANTIATES_CANONICAL);
		task.setStatus(Task.TaskStatus.REQUESTED);
		task.setIntent(Task.TaskIntent.ORDER);
		task.setAuthoredOn(new Date());
		task.getRequester().setType(ResourceType.Organization.name()).getIdentifier()
				.setSystem(NamingSystems.OrganizationIdentifier.SID).setValue("Test_DIC");
		task.getRestriction().addRecipient().setType(ResourceType.Organization.name()).getIdentifier()
				.setSystem(NamingSystems.OrganizationIdentifier.SID).setValue("Test_DIC");

		task.addInput().setValue(new StringType(PROFILE_TUTORIAL_TASK_HELLO_DIC_MESSAGE_NAME)).getType().addCoding()
				.setSystem(CodeSystems.BpmnMessage.URL).setCode(CodeSystems.BpmnMessage.Codes.MESSAGE_NAME);
		task.addInput().setValue(new StringType("send-response")).getType().addCoding().setSystem(CODESYSTEM_TUTORIAL)
				.setCode(CODESYSTEM_TUTORIAL_VALUE_TUTORIAL_INPUT);

		return task;
	}
}
