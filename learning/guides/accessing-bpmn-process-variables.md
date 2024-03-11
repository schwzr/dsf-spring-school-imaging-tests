### Accessing BPMN Process Variables

After creating a [Service Delegate](basic-concepts-and-guides.md#service-delegates) or [Message Delegate](basic-concepts-and-guides.md#message-delegates), you might want to
retrieve data from or store data in the [BPMN process variables](basic-concepts-and-guides.md#bpmn-process-variables).
You can achieve this either through the [BPMN process execution](basic-concepts-and-guides.md#bpmn-process-execution) or via the `Variables` class.  
*It is very much recommended you use the latter method*.   
The `Variables` class provides lots of utility methods to read or write certain types
of [BPMN process variables](basic-concepts-and-guides.md#bpmn-process-variables). If for some reason you need to fall back on the [BPMN process execution](basic-concepts-and-guides.md#bpmn-process-execution)
to solve your problem, we would like to learn how the current API of the `Variables` class is limiting you. Contact us, and we might turn it into a feature request ([Contribute](https://dsf.dev/stable/contribute)).
