package dev.dsf.process.tutorial.spring.config;

import static dev.dsf.process.tutorial.ConstantsTutorial.PROCESS_NAME_FULL_DIC;

import dev.dsf.process.tutorial.message.*;
import dev.dsf.process.tutorial.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.documentation.ProcessDocumentation;

@Configuration
public class TutorialConfig
{
	@Autowired
	private ProcessPluginApi api;

	// send dicom specific

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SendReceiveDataMessage sendReceiveDataMessage()
	{
		return new SendReceiveDataMessage(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ExtractImagingStudyService extractImagingStudyService()
	{
		return new ExtractImagingStudyService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public LoadImageDataService loadImageDataService()
	{
		return new LoadImageDataService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public LoadImagingStudyService loadImagingStudyService()
	{
		return new LoadImagingStudyService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public LogSuccessService logSuccessService()
	{
		return new LogSuccessService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public LogTimeoutService logTimeoutService()
	{
		return new LogTimeoutService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public StoreDataService storeDataService()
	{
		return new StoreDataService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public LoadImagingDataService loadImagingDataService()
	{
		return new LoadImagingDataService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public StoreImagingStudyService storeImagingStudyService()
	{
		return new StoreImagingStudyService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public StoreImageDataService storeImageDataService()
	{
		return new StoreImageDataService(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SendSuccessMessage sendSuccessMessage()
	{
		return new SendSuccessMessage(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DeleteTemporaryDataService deleteTemporaryDataService()
	{
		return new DeleteTemporaryDataService(api);
	}



	// tutorial beans, not relevant anymore

	@Value("${dev.dsf.process.tutorial.loggingEnabled:false}")
	@ProcessDocumentation(description = "Set to true to enable logging", required = false, processNames = PROCESS_NAME_FULL_DIC)
	private boolean loggingEnabled;

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DicTask dicTask()
	{
		return new DicTask(api, loggingEnabled);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public HelloCosMessage helloCosMessage()
	{
		return new HelloCosMessage(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CosTask cosTask()
	{
		return new CosTask(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public HelloHrpMessage helloHrpMessage()
	{
		return new HelloHrpMessage(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public HrpTask helloHrp()
	{
		return new HrpTask(api);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public GoodbyeDicMessage goodbyeDicMessage()
	{
		return new GoodbyeDicMessage(api);
	}

	// end tutorial beans, not relevent anymore


}
