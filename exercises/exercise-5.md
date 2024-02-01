[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • **Exercise 5**
___

# Exercise 5 - Event Based Gateways and Intermediate Events
In the final exercise we will look at message flow between three organizations as well as how to continue a waiting process if no return message arrives. 
With this exercise we will add a third process and complete a message loop from `Test_DIC` to `Test_COR` to `Test_HRP` back to `Test_DIC`.

## Introduction
### Managing Multiple and Missing Messages
If an existing and already running process instance is waiting for a message from another organization, the corresponding FHIR [Task](http://hl7.org/fhir/R4/task.html) may never arrive. 
Either because the other organization decides to never send the message or because some technical problem prohibits the [Task](http://hl7.org/fhir/R4/task.html) resource from being posted to the DSF FHIR server. 
This would result in stale process instances that never finish.

In order to solve this problem we can add an [Event Based Gateway](https://docs.camunda.org/manual/7.17/reference/bpmn20/gateways/event-based-gateway/) to the process waiting for a response and then either handle a [Task](http://hl7.org/fhir/R4/task.html) resource with the response and finish the process in a success state or trigger an [Intermediate Timer Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event) after a defined wait period and finish the process in an error state. The following BPMN collaboration diagram shows how the process at the first organization would look like if two different message or no message could be received:

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="figures/exercise5_event_based_gateway_inverted.svg">
  <source media="(prefers-color-scheme: light)" srcset="figures/exercise5_event_based_gateway.svg">
  <img alt="BPMN collaboration diagram with a Event Based Gateway" src="figures/exercise5_event_based_gateway.svg">
</picture>

#### Timer Events
For [Timer Events](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/) the duration until the timer runs out is specified using the [ISO 8601 Durations](http://en.wikipedia.org/wiki/ISO_8601#Durations) format. Examples can be found in the [Camunda 7 documentation](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#time-duration).

### Matching Process Instances With Business Keys
In the example above, the first organization is sending a message to the second and waiting for a reply. In order to correlate the return message with the waiting process instance, a unique identifier needs to be exchanged between both process instances. Within the DSF this is implemented using the process instance `business-key` and a corresponding [Task.input](http://hl7.org/fhir/R4/task.html) parameter. For **1:1** communication relationships this is handled by the DSF BPE servers automatically, but the corresponding [Task](http://hl7.org/fhir/R4/task.html) profiles need to define the `business-key` input parameter as mandatory.

If multiple message are send in a **1:n** relationship with an **n:1** return an additional `correlation-key` needs to be configured in order to correlate every bidirectional communication between two DSF instances.
This is not covered in this tutorial. Until we amend this tutorial, you are able to study the usage of `correlation-keys` in the [Ping-Pong Process](https://github.com/datasharingframework/dsf-process-ping-pong).

### Organization Roles in the DSF

In exercise 1 we took a first look at the [process authorization extension](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml). Back then it used either the coding for [all local clients](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml) or the one for [all remote clients](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-coding-process-authorization-remote-all-1.0.0.xml). 
In exercise 3 we used the codings for [local organizations](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-coding-process-authorization-local-organization-1.0.0.xml) and the one for [remote organizations](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-coding-process-authorization-remote-organization-1.0.0.xml) in the ActivityDefinition for the `dsfdev_helloCos` process.
The example below shows a scenario where all organizations, which have a certain role inside a parent organization, are allowed to request or receive [Task](http://hl7.org/fhir/R4/task.html) resources.

```xml
<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
	<extension url="message-name">
		<valueString value="some-message-name" />
	</extension>
	<extension url="task-profile">
		<valueCanonical value="http://foo.org/fhir/StructureDefinition/profile|#{version}" />
	</extension>
	<extension url="requester">
		<valueCoding>
			<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role">
				<extension url="parent-organization">
					<valueIdentifier>
						<system value="http://dsf.dev/sid/organization-identifier" />
						<value value="identifier.parent.org" />
					</valueIdentifier>
				</extension>
				<extension url="organization-role">
					<valueCoding>
						<system value="http://dsf.dev/fhir/CodeSystem/organization-role" />
						<code value="SOME_ROLE" />
					</valueCoding>
				</extension>
			</extension>
			<system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
			<code value="REMOTE_ROLE" />
		</valueCoding>
	</extension>
	<extension url="recipient">
		<valueCoding>
			<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role">
				<extension url="parent-organization">
					<valueIdentifier>
						<system value="http://dsf.dev/sid/organization-identifier" />
						<value value="identifier.parent.org" />
					</valueIdentifier>
				</extension>
				<extension url="organization-role">
					<valueCoding>
						<system value="http://dsf.dev/fhir/CodeSystem/organization-role" />
						<code value="SOME_ROLE" />
					</valueCoding>
				</extension>
			</extension>
			<system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
			<code value="LOCAL_ROLE" />
		</valueCoding>
	</extension>
</extension>
```

## Exercise Tasks
1. Forward the value from the [Task.input](http://hl7.org/fhir/R4/task.html) parameter of the `helloDic` [Task](http://hl7.org/fhir/R4/task.html) to the `dsfdev_helloCos` process using the `HelloCosMessage`. To do this, you need to override `HelloCosMessage#getAdditionalInputParameters`.
1. Modify the `dsfdev_helloCos` process to use a [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) to trigger the process in file `hello-hrp.bpmn`. Figure out the values for the `instantiatesCanonical`, `profile` and `messageName` input parameters of the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) based on the [AcitvityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) in file `hello-hrp.xml`.
1. Modify the process in file `hello-hrp.bpmn` and set the _process definition key_ and _version_. Figure out the appropriate values based on the [AcitvityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) in file `hello-hrp.xml`.
1. Add the process in file `hello-hrp.bpmn` to the `TutorialProcessPluginDefinition` and configure the FHIR resources needed for the three processes.
1. Add the `HelloCos`, `HelloHrpMessage `, `HelloHrp` and `GoodbyeDicMessage` classes as Spring Beans. Don't forget the scope.
1. Modify the `dsfdev_helloDic` process:
	* Change the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) to an [Intermediate Message Throw Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-intermediate-throwing-event)
	* Add an [Event Based Gateway](https://docs.camunda.org/manual/7.17/reference/bpmn20/gateways/event-based-gateway/) after the throw event
	* Configure two cases for the [Event Based Gateway](https://docs.camunda.org/manual/7.17/reference/bpmn20/gateways/event-based-gateway/):
	    1. An [Intermediate Message Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-intermediate-catching-event) to catch the `goodbyDic` message from the `dsfdev_helloHrp` process.
	    1. An [Intermediate Timer Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event) to end the process if no message is sent by the `dsfdev_helloHrp` process after two minutes.
	    Make sure both cases finish with a process [End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/none-events/).
    * Add a new process authorization extension element to the ActivityDefinition for `dsfdev_helloDic` using the role scenario from above where
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
mvn clean install -Pexercise-5
```
Verify that the build was successful and no test failures occurred.

### Process Execution and Manual Tests
To verify the `dsfdev_helloDic`, `dsfdev_helloCos` and `dsfdev_helloHrp` processes can be executed successfully, we need to deploy them into DSF instances and execute the `dsfdev_helloDic` process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker dev setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in a second console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloDic` process.

3. Start the DSF FHIR server for the `Test_COS` organization in a third console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up cos-fhir
   ```
   Verify the DSF FHIR server started successfully.

4. Start the DSF BPE server for the `Test_COS` organization in a fourth console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up cos-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloDic` process.


5. Start the DSF FHIR server for the `Test_HRP` organization in a fifth at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up hrp-fhir
   ```
   Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server at https://hrp/fhir.  
   The DSF FHIR server uses a server certificate that was generated during the first maven build. To authenticate yourself to the server you can use the client certificate located at `.../dsf-process-tutorial/test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12` (Password: password).

6. Start the DSF BPE server for the `Test_HRP` organization in a sixth console at location `.../dsf-process-tutorial/dev-setup`:
   ```
   docker-compose up hrp-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloHrp` process. The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resource. Go to https://hrp/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying the process. The returned FHIR [Bundle](http://hl7.org/fhir/R4/bundle.html) should contain a three [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resources. Also, go to https://hrp/fhir/StructureDefinition?url=http://dsf.dev/fhir/StructureDefinition/task-hello-hrp to check if the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

7. Start the `dsfdev_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Execute therefore the `main` method of the `dev.dsf.process.tutorial.TutorialExampleStarter` class to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `dsfdev_helloDic` process.

   Verify that the FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server and the `dsfdev_helloDic` process was executed by the DSF BPE server of the `Test_DIC` organization. The DSF BPE server of the `Test_DIC` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `dsfdev_helloCos` process was sent to the `Test_COS` organization.  
   Verify that a FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server of the `Test_COS` organization and the `dsfdev_helloCos` process was executed by the DSF BPE server of the `Test_COS` organization. The DSF BPE server of the `Test_COS` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `dsfdev_helloHrp` process was send to the `Test_HRP` organization.  
   
   Based on the value of the Task.input parameter you send, the `dsfdev_helloHrp` process will either send a `goodbyDic` message to the `Test_DIC` organization or finish without sending a message.
   
   To trigger the `goodbyDic` message, use `send-response` as the `http://dsf.dev/fhir/CodeSystem/tutorial#tutorial-input` input parameter.
   
   Verify that the `dsfdev_helloDic` process either finishes with the arrival of the `goodbyDic` message or after waiting for two minutes.

___
[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • [Exercise 3](exercise-3.md) • [Exercise 4](exercise-4.md) • **Exercise 5**