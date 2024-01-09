package dev.dsf.process.tutorial.spring.config;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.fhir.authorization.read.ReadAccessHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TutorialConfig
{
	@Autowired
	private ProcessPluginApi api;

	@Autowired
	private ReadAccessHelper readAccessHelper;
}
