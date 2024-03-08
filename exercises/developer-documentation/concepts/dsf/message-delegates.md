### Message Delegates

Message Delegates are the Java representation of the [Message Events](basic-concepts-and-guides.md#messaging) in your BPMN model.
You link a Message Delegate to a certain [Message Event](basic-concepts-and-guides.md#messaging) by selecting the Message Event
in the [Camunda Modeler](https://camunda.com/download/modeler/) and adding a Java class to the `Implementation` field.
Make sure you use the fully qualified class name. Like this:
```
org.package.myClass
```

You will only need Message Delegates for [Message Send Events](basic-concepts-and-guides.md#messaging). Incoming messages will
be resolved to the correct [BPMN process execution](basic-concepts-and-guides.md#bpmn-process-execution) automatically using [Message Correlation](basic-concepts-and-guides.md#message-correlation)
and the message inputs will be added to that execution's [process variables](basic-concepts-and-guides.md#bpmn-process-variables).

To make a Message Delegate for [Message Send Events](basic-concepts-and-guides.md#messaging), your Java class needs to extend
`AbstractTaskMessageSend`. Most of the time, you will not be adding any processing logic to your Message Delegates, therefore you usually won't be overwriting
the `doExecute` method like with [Service Delegates](basic-concepts-and-guides.md#service-delegates). Instead, you most likely want to
aggregate the information you processed in earlier steps and attach it to a message. For this you need to overwrite the
`getAdditionalInputParamters` method. The DSF translates BPMN messages
into FHIR [Task](basic-concepts-and-guides.md#task) resources to execute the communication modeled by your BPMN diagrams. The information
you are sending to another BPMN process is specified in the Task.input elements a.k.a. [Input Parameters](basic-concepts-and-guides.md#task-input-parameters),
hence the name of the method.  
The constructor of your delegate class should also forward a `ProcessPluginApi` instance to its superclass constructor.
You can learn more about the `ProcessPluginApi` [here](basic-concepts-and-guides.md#process-plugin-api).
