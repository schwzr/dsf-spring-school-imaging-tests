[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md) • **Exercise 6**
___

# Exercise 6 - Event Based Gateways and Intermediate Events
In the final exercise we will look at message flow between three organizations as well as how to continue a waiting process if no return message arrives. 
With this exercise we will add a third process and complete a message loop from `Test_DIC` to `Test_COR` to `Test_HRP` back to `Test_DIC`.

In order to solve this exercise, you should have solved exercise 5 and read the topics on 
[Managing Multiple Incoming Messages and Missing Messages](basic-concepts-and-guides.md#managing-multiple-incoming-messages-and-missing-messages)
and [Message Correlation](basic-concepts-and-guides.md#message-correlation).

Solutions to this exercise are found on the branch `solutions/exercise-6`.

## Exercise Tasks
1. Forward the value from the [Task.input](http://hl7.org/fhir/R4/task.html) parameter of the `dicProcess` [Task](http://hl7.org/fhir/R4/task.html) to the `dsfdev_helloCos` process using the `HelloCosMessage`. To do this, you need to override `HelloCosMessage#getAdditionalInputParameters`. Don't forget to also add the definition of your `tutorial-input` [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) from `task-start-dic-process.xml` to `task-hello-cos.xml`. 
1. Modify the `dsfdev_helloCos` process to use a [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) to trigger the process in file `hello-hrp.bpmn`. Figure out the values for the `instantiatesCanonical`, `profile` and `messageName` input parameters of the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) based on the [AcitvityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) in file `hello-hrp.xml`.
1. Modify the process in file `hello-hrp.bpmn` and set the _process definition key_ and _version_. Figure out the appropriate values based on the [AcitvityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) in file `hello-hrp.xml`.
1. Add the process in file `hello-hrp.bpmn` to the `TutorialProcessPluginDefinition` and configure the FHIR resources needed for the three processes.
1. Add the `HelloCos`, `HelloHrpMessage `, `HelloHrp` and `GoodbyeDicMessage` classes as Spring Beans. Don't forget the scope.
1. Modify the `dsfdev_dicProcess` process:
    * Change the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) to an [Intermediate Message Throw Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-intermediate-throwing-event)
    * Add an [Event Based Gateway](https://docs.camunda.org/manual/7.17/reference/bpmn20/gateways/event-based-gateway/) after the throw event
    * Configure two cases for the [Event Based Gateway](https://docs.camunda.org/manual/7.17/reference/bpmn20/gateways/event-based-gateway/):
        1. An [Intermediate Message Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-intermediate-catching-event) to catch the `goodbyDic` message from the `dsfdev_hrpProcess` process.
        1. An [Intermediate Timer Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event) to end the process if no message is sent by the `dsfdev_hrpProcess` process after two minutes.
        Make sure both cases finish with a process [End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/none-events/).
    * Add a new process authorization extension element to the ActivityDefinition for `dsfdev_dicProcess` using [scenario 3](basic-concepts-and-guides.md#scenario-3) where
   only organizations which are part of `medizininformatik-initiative.de` and have the `HRP` role are allowed to request `goodByeDic` messages and only
   organizations which are part of `medizininformatik-initiative.de` and have the `DIC` role are allowed to receive `goodByeDic` messages
      <details>
      <summary>Don't know which values to choose for roles?</summary>
   
      Take a look at the [dsf-organization-role](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-organization-role-1.0.0.xml) CodeSystem.
      </details>

## Solution Verification
### Maven Build and Automated Tests
Execute a maven build of the `dsf-process-tutorial` parent module via:
```
mvn clean install -Pexercise-6
```
Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `dsfdev_dicProcess`, `dsfdev_helloCos` and `dsfdev_hrpProcess` processes can be executed successfully, we need to deploy them into DSF instances and execute the `dsfdev_dicProcess` process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker dev setup.
Don't forget that you will have to add the client certificate for the `HRP` instance to your browser the same way you added it for the `DIC` instance
in [exercise 1](exercise-1.md) and [exercise 4](exercise-5.md) or use the Keycloak user you created in [exercise 3](exercise-3.md) for the `hrp` realm. Otherwise, you won't be able to access [https://hrp/fhir](https://hrp/fhir). You can find the client certificate
in `.../dsf-process-tutorial/test-data-generator/cert/hrp-client/hrp-client_certificate.p12` (password: password).

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in a second console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_dicProcess` process.

3. Start the DSF FHIR server for the `Test_COS` organization in a third console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up cos-fhir
   ```
   Verify the DSF FHIR server started successfully.

4. Start the DSF BPE server for the `Test_COS` organization in a fourth console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up cos-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_dicProcess` process.


5. Start the DSF FHIR server for the `Test_HRP` organization in a fifth at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up hrp-fhir
   ```
   Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server at https://hrp/fhir.

6. Start the DSF BPE server for the `Test_HRP` organization in a sixth console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up hrp-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_hrpProcess` process. The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resource. Go to https://hrp/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying the process. The returned FHIR [Bundle](http://hl7.org/fhir/R4/bundle.html) should contain a three [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resources. Also, go to https://hrp/fhir/StructureDefinition?url=http://dsf.dev/fhir/StructureDefinition/task-hello-hrp to check if the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

7. Start the `dsfdev_dicProcess` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization using either cURL or the DSF FHIR server's web interface. Check out [Starting A Process Via Task Resources](basic-concepts-and-guides.md#starting-a-process-via-task-resources) again if you are unsure.

   Verify that the FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server and the `dsfdev_dicProcess` process was executed by the DSF BPE server of the `Test_DIC` organization. The DSF BPE server of the `Test_DIC` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `dsfdev_helloCos` process was sent to the `Test_COS` organization.  
   Verify that a FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server of the `Test_COS` organization and the `dsfdev_helloCos` process was executed by the DSF BPE server of the `Test_COS` organization. The DSF BPE server of the `Test_COS` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `dsfdev_hrpProcess` process was send to the `Test_HRP` organization.  
   
   Based on the value of the Task.input parameter you send, the `dsfdev_hrpProcess` process will either send a `goodbyDic` message to the `Test_DIC` organization or finish without sending a message.
   
   To trigger the `goodbyDic` message, use `send-response` as the `http://dsf.dev/fhir/CodeSystem/tutorial#tutorial-input` input parameter.
   
   Verify that the `dsfdev_dicProcess` process either finishes with the arrival of the `goodbyDic` message or after waiting for two minutes.

___
[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md) • **Exercise 6**