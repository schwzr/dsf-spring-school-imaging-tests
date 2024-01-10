package dev.dsf.process.tutorial.spring.config;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.process.tutorial.service.HelloDic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TutorialConfig
{
	@Autowired
	private ProcessPluginApi api;

	@Bean
	public HelloDic helloDic()
	{
		return new HelloDic(api);
	}

}
