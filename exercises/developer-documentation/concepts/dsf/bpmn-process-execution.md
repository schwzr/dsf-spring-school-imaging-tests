### BPMN Process Execution

The BPMN process execution is the in-memory representation of a running BPMN process.
It holds all the BPMN elements from the BPMN model as well as the [BPMN process variables](basic-concepts-and-guides.md#bpmn-process-variables)
and exists from the time when a deployed process plugin gets started until the time it stops running.
You have access to this representation in your Java code when overriding certain methods in [Service](basic-concepts-and-guides.md#service-delegates) / [Message](basic-concepts-and-guides.md#message-delegates) Delegates
like `doExecute` or `getAdditionalInputParameters` through the `execution` parameter.