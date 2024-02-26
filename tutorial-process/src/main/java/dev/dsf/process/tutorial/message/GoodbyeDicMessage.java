package dev.dsf.process.tutorial.message;

import dev.dsf.bpe.v1.ProcessPluginApi;
import dev.dsf.bpe.v1.activity.AbstractTaskMessageSend;

// Only needed for exercise 6 and above
public class GoodbyeDicMessage extends AbstractTaskMessageSend
{
	public GoodbyeDicMessage(ProcessPluginApi api)
	{
		super(api);
	}
}
