### Managing Multiple Incoming Messages and Missing Messages

If an already running process instance is waiting for a message from another organization, the corresponding FHIR [Task](basic-concepts-and-guides.md#task) may never arrive.
Either because the other organization decides to never send the message or because some technical problem prohibits the [Task](basic-concepts-and-guides.md#task) resource from being posted to the DSF FHIR server.
This would result in stale process instances that never finish.

At the same time, you might also expect to receive one out of a number of different message types at once.

In order to solve both problems we can add an [Event Based Gateway](basic-concepts-and-guides.md#event-based-gateway) to the process waiting
for a response and then either handle a [Task](basic-concepts-and-guides.md#task) resource with the response and finish the process in a success
state or trigger a [Timer Intermediate Catching Event](basic-concepts-and-guides.md#timer-intermediate-catching-events) after a defined wait period and finish the process in an error state.
The following BPMN collaboration diagram shows how the process at the first organization would look like if we wanted to react to multiple different messages
or missing messages:

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../figures/exercise5_event_based_gateway_inverted.svg">
  <source media="(prefers-color-scheme: light)" srcset="../../figures/exercise5_event_based_gateway.svg">
  <img alt="BPMN collaboration diagram with an Event Based Gateway" src="../../figures/exercise5_event_based_gateway.svg">
</picture>
