### BPMN Process Execution

The BPMN process execution is the in-memory representation of a running BPMN process.
It holds all the BPMN elements from the BPMN model as well as the [BPMN process variables](../../concepts/dsf/bpmn-process-variables.md)
and exists from the time when a deployed process plugin gets started until the time it stops running.
You have access to this representation in your Java code when overriding certain methods in [Service](../../concepts/dsf/service-delegates.md) / [Message](../../concepts/dsf/message-delegates.md) Delegates
like `doExecute` or `getAdditionalInputParameters` through the `execution` parameter.