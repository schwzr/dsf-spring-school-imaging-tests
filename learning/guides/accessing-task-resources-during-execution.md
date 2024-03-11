### Accessing Task Resources During Execution

If you want access to the [Task](../concepts/fhir/task.md) resources in your [Service](../concepts/dsf/service-delegates.md) / [Message](../concepts/dsf/message-delegates.md) Delegates, the `Variables` class will
provide methods which return certain kinds of [Task](../concepts/fhir/task.md) resources. The most commonly used ones are
the start [Task](../concepts/fhir/task.md), referring to the [Task](../concepts/fhir/task.md) / [Message Start Event](../concepts/bpmn/messaging.md#message-start-event) responsible for starting the process,
and the latest [Task](../concepts/fhir/task.md), referring to most recently received [Task](../concepts/fhir/task.md) / Message.  
In principle, this is sufficient to access all information in a [Task](basic-concepts-and-guides.md#task) resource, since you have
the [Task](../concepts/fhir/task.md) resource's Java object, but very cumbersome.
Instead of navigating the [Task](../concepts/fhir/task.md) resource's element tree,
you should first try to use the [ProcessPluginApi's](../concepts/dsf/process-api.md) `TaskHelper` in conjunction with the method above. The `TaskHelper` class
offers specific methods related to [Task](../concepts/fhir/task.md) resources.  
The most common use case for this is retrieving data from a [Task's](../concepts/fhir/task.md) [Input Parameter](../concepts/fhir/task.md#task-input-parameters) or creating a new [Input Parameter](../concepts/fhir/task.md#task-input-parameters)
for a [Message Delegate's](../concepts/dsf/message-delegates.md) `getAdditionalInputParameters` method.  
When retrieving data from a [Task's](../concepts/fhir/task.md) Input Parameter you first have to get to the [Input Parameter](../concepts/fhir/task.md#task-input-parameters) you are looking to extract
data from. You can use one of the `TaskHelper's` getters for [Input Parameters](../concepts/fhir/task.md#task-input-parameters) to find the right one. The methods will try to match
the provided [CodeSystem](../concepts/fhir/codesystem.md) and Code to any [Input Parameter](../concepts/fhir/task.md#task-input-parameters) of the provided [Task](../concepts/fhir/task.md) resource.
Depending on the method you chose you will for example receive all matches or just the first one.  
To create new [Input Parameters](../concepts/fhir/task.md#task-input-parameters) to attach to a [Task](../concepts/fhir/task.md) resource, you may invoke the `TaskHelper#createInput` method. This
is most often used when overriding the `getAdditionalInputParamters` method of you [Message Delegate](../concepts/dsf/message-delegates.md).