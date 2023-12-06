package dev.dsf.process.tutorial.spring.config;

import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import dev.dsf.fhir.client.FhirWebserviceClientProvider;
import dev.dsf.fhir.task.TaskHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TutorialConfig
{
	@Autowired
	private FhirWebserviceClientProvider clientProvider;

	@Autowired
	private TaskHelper taskHelper;

	@Autowired
	private ReadAccessHelper readAccessHelper;
}
