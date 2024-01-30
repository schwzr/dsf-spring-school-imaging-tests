package dev.dsf.process.tutorial;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import dev.dsf.bpe.v1.ProcessPluginDefinition;
import dev.dsf.process.tutorial.spring.config.TutorialConfig;

public class TutorialProcessPluginDefinition implements ProcessPluginDefinition
{
	public static final String VERSION = "1.0.0.1";
	public static final LocalDate RELEASE_DATE = LocalDate.of(2022, 8, 21);

	@Override
	public String getName()
	{
		return "dsf-process-tutorial";
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}

	@Override
	public LocalDate getReleaseDate()
	{
		return RELEASE_DATE;
	}

	@Override
	public List<String> getProcessModels()
	{

		return List.of("bpe/hello-dic.bpmn", "bpe/hello-cos.bpmn", "bpe/hello-hrp.bpmn");
	}

	@Override
	public Map<String, List<String>> getFhirResourcesByProcessId()
	{

		String aHelloDic = "fhir/ActivityDefinition/hello-dic.xml";
		String sTaskHelloDic = "fhir/StructureDefinition/task-hello-dic.xml";
		String sTaskGoodbyeDic = "fhir/StructureDefinition/task-goodbye-dic.xml";

		String cTutorial = "fhir/CodeSystem/tutorial.xml";

		String vTutorial = "fhir/ValueSet/tutorial.xml";

		String aHelloCos = "fhir/ActivityDefinition/hello-cos.xml";
		String sTaskHelloCos = "fhir/StructureDefinition/task-hello-cos.xml";

		String aHelloHrp = "fhir/ActivityDefinition/hello-hrp.xml";
		String sTaskHelloHrp = "fhir/StructureDefinition/task-hello-hrp.xml";

		return Map.of(ConstantsTutorial.PROCESS_NAME_FULL_HELLO_DIC,
				List.of(aHelloDic, sTaskHelloDic, sTaskGoodbyeDic, cTutorial, vTutorial),
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_COS, List.of(aHelloCos, sTaskHelloCos, cTutorial, vTutorial),
				ConstantsTutorial.PROCESS_NAME_FULL_HELLO_HRP, List.of(aHelloHrp, sTaskHelloHrp, cTutorial, vTutorial));
	}

	@Override
	public List<Class<?>> getSpringConfigurations()
	{
		return List.of(TutorialConfig.class);
	}
}
