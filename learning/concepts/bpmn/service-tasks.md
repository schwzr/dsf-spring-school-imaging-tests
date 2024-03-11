### Service Tasks

You will primarily use [Service Tasks](https://docs.camunda.org/manual/7.20/reference/bpmn20/tasks/service-task/)
when creating BPMN models. They are different from regular BPMN Tasks in that they offer the ability to
link an implementation to the [Service Task](https://docs.camunda.org/manual/7.20/reference/bpmn20/tasks/service-task/)
which can be called and executed by a BPMN engine. The BPE (Business Process Engine) server of the DSF leverages
this engine to execute your BPMN processes.