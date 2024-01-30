package dev.dsf.process.tutorial.message;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractTaskMessageSend;

// Only needed for exercise 3 and above
public class HelloCosMessage extends AbstractTaskMessageSend
{
	public HelloCosMessage(ProcessPluginApi api)
	{
		super(api);
	}
}
