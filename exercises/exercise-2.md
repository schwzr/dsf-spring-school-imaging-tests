[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • **Exercise 2** • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
___

# Exercise 2 - Environment Variables and Input Parameters
BPMN processes might require additional information during execution, e.g. for configuration purposes. We will take a look at two possibilities on how to pass additional information to a BPMN process. The goal of this exercise is to enhance the `dsfdev_helloDic` process by trying them both. 
In both cases the information will be available in the `doExecute` method of your service class.

## Introduction
DSF process plugins can be given additional information using two different approaches: 

* Static configuration using environment variables during the deployment of a process plugin.
* Dynamic input by sending values as part of the [Task](http://hl7.org/fhir/R4/task.html) resource to start or continue a process instance.

### Environment Variables
Environment variables are the same for all running process instances and allow static configuration of processes. They can be defined by adding a member variable having the [Spring-Framework @Value](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-value-annotations) annotation to the configuration class `TutorialConfig`. The value of the annotation uses the `${..}` notation and follows the form `${some.property:defaultValue}`, where each dot in the property name corresponds to an underscore in the environment variable and environment variables are always written upper-case. The property `some.property` therefore corresponds to the environment variable `SOME_PROPERTY`.

The DSF provides a feature to automatically generate documentation of environment variables during the Maven build process. 
You can use the [@ProcessDocumentation](https://github.com/datasharingframework/dsf/blob/main/dsf-tools/dsf-tools-documentation-generator/src/main/java/org/highmed/dsf/tools/generator/ProcessDocumentation.java) annotation to automatically generate Markdown documentation for all fields with this annotation. You simply have to add [dsf-tools-documentation-generator](https://mvnrepository.com/artifact/dev.dsf/dsf-tools-documentation-generator) as a maven plugin. You can take a look at the `pom.xml` for the `tutorial-process` submodule to see how you can add it to your own project. Keep in mind to point the `<workingPackage>` field to the package you want documentation for.


### Task Input Parameters
Providing input parameters to a specific process instance allows for dynamic configuration of process instances. It can be done by sending additional values as part of the [Task](http://hl7.org/fhir/R4/task.html) resource that starts or continues a process instance. It should be noted that a FHIR profile must be created for each [Task](http://hl7.org/fhir/R4/task.html) resource, i.e. for each message event in a process model, which inherits from the [DSF Task Base Profile](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml). This base profile defines three default input parameters:

* [`message-name`](https://github.com/datasharingframework/dsf/blob/f372b757b22d59b3594a220f7f380c60aa6f00b8/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml#L106-L145) (**mandatory 1..1**): the name of the BPMN message event, same as in the BPMN model
* [`business-key`](https://github.com/datasharingframework/dsf/blob/f372b757b22d59b3594a220f7f380c60aa6f00b8/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml#L146-L184) (optional 0..1): used to identify process instances
* [`correlation-key`](https://github.com/datasharingframework/dsf/blob/f372b757b22d59b3594a220f7f380c60aa6f00b8/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml#L185-L223) (optional 0..1): used to identify multi-instance process instances used for messaging multiple targets

A later exercise will examine these input parameters and their meaning in more detail. 

Since input parameters of [Task](http://hl7.org/fhir/R4/task.html) resources are identified by predefined codes, they are defined via FHIR [CodeSystem](http://hl7.org/fhir/R4/codesystem.html) and [ValueSet](http://hl7.org/fhir/R4/valueset.html) resources. The [BPMN-Message CodeSystem](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-bpmn-message-1.0.0.xml) and the [BPMN-Message ValueSet](
https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/ValueSet/dsf-bpmn-message-1.0.0.xml) are used in the [DSF Task Base Profile](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml) to define the three default input parameters of [Task](http://hl7.org/fhir/R4/task.html) resources.

### Version and Release-Date Placeholders
To avoid the need to specify the version and release date for each [CodeSystem](http://hl7.org/fhir/R4/codesystem.html), [StructureDefinition (Task profile)](http://hl7.org/fhir/R4/structuredefinition.html) and [ValueSet](http://hl7.org/fhir/R4/valueset.html) resource, the placeholders `#{version}` and `#{date}` can be used. They are replaced with the values returned by the methods `ProcessPluginDefinition#getResourceVersion()` and `ProcessPluginDefinition#getReleaseDate()` respectively during deployment of a process plugin by the DSF BPE server.

### Read Access Tag
While writing FHIR resources on the DSF FHIR server is only allowed by the own organization (except [Task](http://hl7.org/fhir/R4/task.html)), rules have to be defined for reading FHIR resources by external organizations (again except [Task](http://hl7.org/fhir/R4/task.html)). The `Resource.meta.tag` field is used for this purpose. To allow read access for all organizations (the standard for metadata resources), the following `read-access-tag` value can be written into this field:

```xml
<meta>
   <tag>
      <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
      <code value="ALL" />
   </tag>
</meta>
```

The read access rules for [Task](http://hl7.org/fhir/R4/task.html) resources are defined through the fields `Task.requester` and `Task.restriction.recipient`. Therefore, no `read-access-tag` is needed.

It is also possible to restrict read access of FHIR resources to organizations with a specific role in a parent organization or a specific identifier, but this is not covered in the tutorial.

The write access rules for [Task](http://hl7.org/fhir/R4/task.html) resources are defined through the [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resources belonging to the process. We will take a look at this in [exercise 3](exercise-3.md) and [exercise 5](exercise-5.md).

### TaskHelper

Through the `api` instance accessible to all classes extending `AbstractTaskMessageSend` and `AbstractServiceDelegate` you can get yourself an instance of `TaskHelper` using `ProcessPluginApi#getTaskHelper`. Similar to the `Variables` class, the `TaskHelper` class provides many utility methods to interact with Task resources in the BPMN process execution. For example, it can get you a list of all input parameters conforming to a given CodeSystem and Code for a certain Task resource.

## Exercise Tasks
1. Add a new field to the `TutorialConfig` class. It will enable/disable logging. Add the annotation and specify the default value as `false`. You may freely choose a name for your environment variable here. Just make sure you follow the naming convention explained earlier.
2. Modify the constructor of the `HelloDic` class to use the newly created field. Don't forget to change the `HelloDic` bean in `TutorialConfig`.
3. Use the value of the environment variable in the `HelloDic` class to decide whether the log message from exercise 1 should be printed.
4. Add the new environment variable to the `dic-bpe` service in `test-setup/docker-compose.yml` and set the value to `"true"`.
5. Create a new [CodeSystem](http://hl7.org/fhir/R4/codesystem.html) with url `http://dsf.dev/fhir/CodeSystem/tutorial` having a concept with code `tutorial-input`. Don't forget to add the `read-access-tag`.
6. Create a new [ValueSet](http://hl7.org/fhir/R4/valueset.html) with url `http://dsf.dev/fhir/ValueSet/tutorial` that includes all concepts from the [CodeSystem](http://hl7.org/fhir/R4/codesystem.html). Don't forget to add the `read-access-tag`.
7. Add a new input parameter of type `string` to the `task-hello-dic.xml` [Task](http://hl7.org/fhir/R4/task.html) profile using the concept of the new [CodeSystem](http://hl7.org/fhir/R4/codesystem.html) as a fixed coding.
8. `task-hello-dic` and by extension the process `dsfdev_helloDic` now require additional FHIR resources. Make sure the return value for `TutorialProcessPluginDefinition#getFhirResourcesByProcessId` also includes the new [CodeSystem](http://hl7.org/fhir/R4/codesystem.html) and [ValueSet](http://hl7.org/fhir/R4/valueset.html) resources for the `dsfdev_helloDic` process.
9. Read the new input parameter in the `HelloDic` class from the start [Task](http://hl7.org/fhir/R4/task.html) and add the value to the log message from exercise 1.
   <details>
   <summary>Don't know where to look for the input parameter?</summary>
   
   The `TaskHelper` instance will prove useful here. Use it in conjunction with `variables` to get the right Task resource from the BPMN process execution.
   </details>
10. We just changed the elements a Task resource has to include. So you need to change the starter class `TutorialExampleStarter` to include the new input parameter. The actual value may be any arbitrary string.

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:

```
mvn clean install -Pexercise-2
```

Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `dsfdev_helloDic` process can be executed successfully, we need to deploy it into a DSF instance and execute the process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker test setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in second console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloDic` process.

3. Start the `dsfdev_helloDic` process by posting an appropriate FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Execute the `main` method of the `dev.dsf.process.tutorial.TutorialExampleStarter` class as in [exercise 1](exercise-1.md) to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `dsfdev_helloDic` process.

   Verify that the `dsfdev_helloDic` process was executed by the DSF BPE server. The BPE server should:
    * Print a message showing that the process was started.
    * If logging is enabled - print the log message and the value of the input parameter you added to the `HelloDic`
      implementation.
    * Print a message showing that the process finished.
    
  Check that you can disable logging of you message by modifying the `docker-compose.yml` file and configuring your environment variable with the value `"false"` or removing the environment variable.  
  _Note: Changes to environment variable require recreating the docker container._
  
  Also check that modification to the [Task](http://hl7.org/fhir/R4/task.html) input parameter specified in the `TutorialExampleStarter` class, have the appropriate effect on your log message.

___
[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • **Exercise 2** • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)