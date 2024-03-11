### Gateways

[Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/) allow you to control the [Sequence Flow](../../concepts/bpmn/sequence-flow.md). Different
types of gateways will be useful for different scenarios.

#### Exclusive Gateways

[Exclusive Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/)
allow you to decide which [Sequence Flow](../../concepts/bpmn/sequence-flow.md) should be followed based on [conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions).
[Conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions) are not part of the
[Exclusive Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/) themselves. You set them
through the sequence Flow Exiting the [Exclusive Gateway](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/).  
In the [Camunda Modeler](https://camunda.com/download/modeler/), you can add conditions to [Sequence Flow](../../concepts/bpmn/sequence-flow.md) by selecting
a [Sequence Flow](../../concepts/bpmn/sequence-flow.md) and opening the `Condition` tab. You can find more information on how to
use Conditions [here](../../concepts/bpmn/conditions.md).

#### Event-based Gateway

The [Event-based Gateway](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/event-based-gateway/)
allows you model scenarios where you are expecting one out of a number of events to occur. 