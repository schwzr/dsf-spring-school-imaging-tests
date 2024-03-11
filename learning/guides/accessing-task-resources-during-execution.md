### Accessing Task Resources During Execution

If you want access to the [Task](basic-concepts-and-guides.md#task) resources in your [Service](basic-concepts-and-guides.md#service-delegates) / [Message](basic-concepts-and-guides.md#message-delegates) Delegates, the `Variables` class will
provide methods which return certain kinds of [Task](basic-concepts-and-guides.md#task) resources. The most commonly used ones are
the start [Task](basic-concepts-and-guides.md#task), referring to the [Task](basic-concepts-and-guides.md#task) / [Message Start Event](basic-concepts-and-guides.md#message-start-event) responsible for starting the process,
and the latest [Task](basic-concepts-and-guides.md#task), referring to most recently received [Task](basic-concepts-and-guides.md#task) / Message.  
In principle, this is sufficient to access all information in a [Task](basic-concepts-and-guides.md#task) resource, since you have
the [Task](basic-concepts-and-guides.md#task) resource's Java object, but very cumbersome.
Instead of navigating the [Task](basic-concepts-and-guides.md#task) resource's element tree,
you should first try to use the [ProcessPluginApi's](basic-concepts-and-guides.md#process-plugin-api) `TaskHelper` in conjunction with the method above. The `TaskHelper` class
offers specific methods related to [Task](basic-concepts-and-guides.md#task) resources.  
The most common use case for this is retrieving data from a [Task's](basic-concepts-and-guides.md#task) [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) or creating a new [Input Parameter](basic-concepts-and-guides.md#task-input-parameters)
for a [Message Delegate's](basic-concepts-and-guides.md#message-delegates) `getAdditionalInputParameters` method.  
When retrieving data from a [Task's](basic-concepts-and-guides.md#task) Input Parameter you first have to get to the [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) you are looking to extract
data from. You can use one of the `TaskHelper's` getters for [Input Parameters](basic-concepts-and-guides.md#task-input-parameters) to find the right one. The methods will try to match
the provided [CodeSystem](basic-concepts-and-guides.md#codesystem) and Code to any [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) of the provided [Task](basic-concepts-and-guides.md#task) resource.
Depending on the method you chose you will for example receive all matches or just the first one.  
To create new [Input Parameters](basic-concepts-and-guides.md#task-input-parameters) to attach to a [Task](basic-concepts-and-guides.md#task) resource, you may invoke the `TaskHelper#createInput` method. This
is most often used when overriding the `getAdditionalInputParamters` method of you [Message Delegate](basic-concepts-and-guides.md#message-delegates).