### Service Delegates

Service Delegates are the Java representation of the [Service Tasks](basic-concepts-and-guides.md#service-tasks) in your BPMN model.
You link a Service Delegate to a certain [Service Task](basic-concepts-and-guides.md#service-tasks) by selecting the [Service Task](basic-concepts-and-guides.md#service-tasks)
in the [Camunda Modeler](https://camunda.com/download/modeler/) and adding a Java class to the `Implementation` field.
Make sure you use the fully qualified class name. Like this:
```
org.package.myClass
```
All that is left is for your Java class to extend `AbstractServiceDelegate` and override the `doExecute` method.
This is the place where you can put your actual business logic. The method will be called when the [BPMN process execution](basic-concepts-and-guides.md#bpmn-process-execution)
arrives at the [Service Task](basic-concepts-and-guides.md#service-tasks) your Service Delegate is linked to.  
The constructor of your delegate class should also forward a `ProcessPluginApi` instance to its superclass constructor.
You can learn more about the `ProcessPluginApi` [here](basic-concepts-and-guides.md#process-plugin-api).