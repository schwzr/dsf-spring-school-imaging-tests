package dev.dsf.process.tutorial.message;

import dev.dsf.bpe.v1.activity.AbstractTaskMessageSend;
import dev.dsf.bpe.v1.ProcessPluginApi;

// Only needed for exercise 5 and above
public class GoodbyeDicMessage extends AbstractTaskMessageSend
{
	public GoodbyeDicMessage(ProcessPluginApi api)
	{
		super(api);
	}
}
