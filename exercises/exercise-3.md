[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 2.1](exercise-2-1.md) • **Exercise 3** • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
___

# Exercise 3 - Message Events
Communication between organizations in BPMN processes is modeled using message flow. The third exercise shows how a process at one organization can trigger a process at another organization.

To demonstrate communication between two organizations we will configure message flow between the processes `dsfdev_helloDic` and `dsfdev_helloCos`. After that, the processes are to be executed at the organizations `Test_DIC` and `Test_COS` respectively in the docker dev setup, with the former triggering execution of the latter by automatically sending a [Task](http://hl7.org/fhir/R4/task.html) resource from organization `Test_DIC` to organization `Test_COS`.

In order to solve this exercise, you should have solved exercise 2 and read the topics on
[Messaging](basic-concepts-and-lessons.md#messaging),
[Message Delegates](basic-concepts-and-lessons.md#message-delegates),
[Version Pattern](basic-concepts-and-lessons.md#version-pattern),
[URLs](basic-concepts-and-lessons.md#urls) 
and [Setting Targets For Message Events](basic-concepts-and-lessons.md#setting-targets-for-message-events).

## Exercise Tasks
1. Replace the [End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/none-events/#none-end-event) of the `dsfdev_helloDic` process in the `hello-dic.bpmn` file with a [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event). Give the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) a name and an ID and set its implementation to the `HelloCosMessage` class.  
   Configure field injections `instantiatesCanonical`, `profile` and `messageName` in the BPMN model for the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event).
    Use `http://dsf.dev/fhir/StructureDefinition/task-hello-cos|#{version}` as the profile and `helloCos` as the message name. Figure out what the appropriate `instantiatesCanonical` value is, based on the name (process definition key) of the process to be triggered.
1. Modify the `dsfdev_helloCos` process in the `hello-cos.bpmn` file and configure the message name of the [Message Start Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-start-event) with the same value as the message name of the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) in the `dsfdev_helloDic` process. 
1. Create a new [StructureDefinition](http://hl7.org/fhir/R4/structuredefinition.html) with a [Task](http://hl7.org/fhir/R4/task.html) profile for the `helloCos` message.
    <details>
   <summary>Don't know how to get started?</summary>
   
   You can base this [Task](http://hl7.org/fhir/R4/task.html) profile off the `StructureDefinition/task-hello-dic.xml` resource. Then look for elements that need to be added, changed or can be omitted.
    </details>
1. Create a new [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resource for the `dsfdev_helloCos` process and configure the authorization extension to allow the `Test_DIC` organization as the requester and the `Test_COS` organization as the recipient.
    <details>
   <summary>Don't know how to get started?</summary>

   You can base this ActivityDefinition off the `ActivityDefinition/hello-dic.xml` resource. Then look for elements that need to be added, changed or can be omitted.
    </details>
    <details>
       <summary>Don't know how to create a proper authorization extension?</summary>
    
    You can base the authorization extension off [scenario 2](basic-concepts-and-lessons.md#scenario-2).
    </details>
1. Add the `dsfdev_helloCos` process and its resources to the `TutorialProcessPluginDefinition` class.
1. Modify `HelloDic` service class to set the `target` process variable for the `Test_COS` organization.
1. Configure the `HelloCosMessage` class as a Spring Bean in the `TutorialConfig` class. Don't forget the right scope.

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:
```
mvn clean install -Pexercise-3
```
Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `dsfdev_helloDic` and `dsfdev_helloCos` processes can be executed successfully, we need to deploy them into DSF instances and execute the `dsfdev_helloDic` process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker dev setup.
Don't forget that you will have to add the client certificate for the `COS` instance to your browser the same way you added it for the `DIC` instance
in [exercise 1](exercise-1.md). Otherwise, you won't be able to access [https://cos/fhir](https://cos/fhir). You can find the client certificate
in `.../dsf-process-tutorial/test-data-generator/cert/cos-client/cos-client_certificate.p12`.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in another console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloDic` process.

3. Start the DSF FHIR server for the `Test_COS` organization in a console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up cos-fhir
   ```
   Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server at https://cos/fhir.  
   The DSF FHIR server uses a server certificate that was generated during the first maven build. To authenticate yourself to the server you can use the client certificate located at `.../dsf-process-tutorial/test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12` (Password: password).

4. Start the DSF BPE server for the `Test_COS` organization in another console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up cos-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloCos` process. The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resource. Go to https://cos/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying the process. The returned FHIR [Bundle](http://hl7.org/fhir/R4/bundle.html) should contain two [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resources. Also, go to https://cos/fhir/StructureDefinition?url=http://dsf.dev/fhir/StructureDefinition/task-hello-cos to check if the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

5. Start the `dsfdev_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Execute therefore the `main` method of the `dev.dsf.process.tutorial.TutorialExampleStarter` class to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `dsfdev_helloDic` process.

   Verify that the FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server and the `dsfdev_helloDic` process was executed by the DSF BPE server of the `Test_DIC` organization. The DSF BPE server of the `Test_DIC` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `dsfdev_helloCos` process was send to the `Test_COS` organization.  
   Verify that a FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server of the `Test_COS` organization and the `dsfdev_helloCos` process was then executed by the DSF BPE server of the `Test_COS` organization.

___
[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 2.1](exercise-2-1.md) • **Exercise 3** • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)