[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • **Exercise 2** • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
___

# Exercise 2 - Environment Variables and Input Parameters
BPMN processes might require additional information during execution, e.g. for configuration purposes. 
We will take a look at two possibilities on how to pass additional information to a BPMN process: Environment Variables and Input Parameters.   
The goal of this exercise is to enhance the `dsfdev_helloDic` process by trying them both. 
In both cases the information will be available in the `doExecute` method of your service class.

In order to solve this exercise, you should have solved the first exercise and read the topics on
[Environment Variables](basic-concepts-and-lessons.md#environment-variables), 
[Task Input Parameters](basic-concepts-and-lessons.md#task-input-parameters),
[Accessing Task Resources During Execution](basic-concepts-and-lessons.md#accessing-task-resources-during-execution),
[Placeholders](basic-concepts-and-lessons.md#placeholders) and
[Read Access Tag](basic-concepts-and-lessons.md#read-access-tag).

Solutions to this exercise are found on the branch `solutions/exercise-2`.


## Exercise Tasks
1. Add a new boolean variable to the `TutorialConfig` class. It will enable/disable logging and have its value injected from an environment variable. Add the annotation and specify the default value as `false`. You may freely choose a name for your environment variable here. Just make sure you follow the naming convention explained earlier.
2. Modify the constructor of the `HelloDic` class to use the newly created variable. Don't forget to change the `HelloDic` bean in `TutorialConfig`.
3. Use the value of the environment variable in the `HelloDic` class to decide whether the log message from exercise 1 should be printed.
4. Add the new environment variable to the `dic-bpe` service in `dev-setup/docker-compose.yml` and set the value to `"true"`.
5. Create a new [CodeSystem](basic-concepts-and-lessons.md#codesystem) with url `http://dsf.dev/fhir/CodeSystem/tutorial` having a concept with code `tutorial-input`. Don't forget to add the `read-access-tag`.
   <details>
   <summary>Don't know where to put the CodeSystem?</summary>
   
   `tutorial-process/src/main/resources/fhir/CodeSystem`.
   </details>

6. Create a new [ValueSet](basic-concepts-and-lessons.md#valueset) with url `http://dsf.dev/fhir/ValueSet/tutorial` that includes all concepts from the [CodeSystem](basic-concepts-and-lessons.md#codesystem). Don't forget to add the `read-access-tag`.
   <details>
   <summary>Don't know where to put the ValueSet?</summary>

   `tutorial-process/src/main/resources/fhir/ValueSet`.
   </details>

7. Add a new input parameter of type `string` to the `task-hello-dic.xml` [Task](http://hl7.org/fhir/R4/task.html) profile using the concept of the new [CodeSystem](basic-concepts-and-lessons.md#codesystem) as a fixed coding.
8. `task-hello-dic` and by extension the process `dsfdev_helloDic` now require additional FHIR resources. Make sure the return value for `TutorialProcessPluginDefinition#getFhirResourcesByProcessId` also includes the new [CodeSystem](basic-concepts-and-lessons.md#codesystem) and [ValueSet](basic-concepts-and-lessons.md#valueset) resources for the `dsfdev_helloDic` process.
9. Read the new input parameter in the `HelloDic` class from the start [Task](http://hl7.org/fhir/R4/task.html) and add the value to the log message from exercise 1.
   <details>
   <summary>Don't know how to get the input parameter?</summary>
   
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
To verify the `dsfdev_helloDic` process can be executed successfully, we need to deploy it into a DSF instance and execute the process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker dev setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in second console at location `.../dsf-process-tutorial/dev-setup`:
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