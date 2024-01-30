[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • **Exercise 3** • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)
___

# Exercise 3 - Message Events
Communication between organizations in BPMN processes is modeled using message flow. The third exercise shows how a process at one organization can trigger a process at another organization.

To demonstrate communication between two organizations we will configure message flow between the processes `dsfdev_helloDic` and `dsfdev_helloCos`. The processes are then to be executed at the organizations `Test_DIC` and `Test_COS` respectively in the docker test setup, with the former triggering execution of the latter by automatically sending a [Task](http://hl7.org/fhir/R4/task.html) from organization `Test_DIC` to organization `Test_COS`.

## Introduction
### Message Flow and FHIR Task resources
BPMN processes are instantiated and started within the DSF by creating a matching FHIR [Task](http://hl7.org/fhir/R4/task.html) resource in the DSF FHIR server which then gets consumed by the DSF BPE server. You can manually create such Task resources to start BPMN processes, like we did with `TutorialExampleStarter`, but you can also start a BPMN process automatically at any location during the execution of another BPMN process. 

To achieve this, you need to include message flow into your BPMN model. Message flow is typically represented by a dashed line arrow between BPMN elements with a black (send) or white (receive) envelope icon.
The following BPMN collaboration diagram shows two processes. The process at "Organization 1" is sending a message to "Organization 2" which results in the instantiation and execution of a new BPMN process instance at the second organization.

In order to exchange information between different processes, for example at two different organizations, BPMN message flow is used. Typically represented by a dashed line arrow between elements with black (send) and white (receive) envelop icons. The following BPMN collaboration diagram shows two processes. The process at "Organization 1" is sending a message to "Organization 2" which results in the instantiation and execution of new process instance at the second organization.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="figures/exercise3_message_flow_inverted.svg">
  <source media="(prefers-color-scheme: light)" srcset="figures/exercise3_message_flow.svg">
  <img alt="BPMN collaboration diagram with two processes using message flow to exchange information between two organizations" src="figures/exercise3_message_flow.svg">
</picture>

Whenever you use message flow, the DSF will send a Task resource specific to that one communication interaction to the receiving party. Therefore, every usage of message flow requires a corresponding Task profile.
The profile specifies which process is addressed and should be executed through the `instantiatesCanonical` value.
It also includes a `message-name` field to correlate this Task resource to a specific message flow event inside the BPMN process.
Some examples for message flow events available to BPMN processes are [Message Start Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-start-event) and [Intermediate Message Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-intermediate-catching-event).
Task profiles also include the fields `business-key` and `correlation-key`. They are both used to correlate message flow events and BPMN process execution instances.
While the `business-key` is usually sufficient to link message flow events to BPMN process instances, there are some limitations.  
Consider a scenario where a BPMN service task spawns multiple child processes which all send a message to another execution and expect an individual response.
If we only have a `business-key` we might be able to link the messages to the correct execution, but we won't be able to link the individual responses to the right child process since there is no way to differentiate between
any of the responses. This is where you would use a `correlation-key` in every child process message. Combined with the `business-key` you can resolve to
the original execution of the child processes and deliver the correct response to each one thanks to the `correlation-key`. More on `business-keys` in exercise 5.
You can learn more about the usage of `correlation-keys` from the [Ping-Pong Process](https://github.com/datasharingframework/dsf-process-ping-pong). 

### BPMN Process Definition Key vs. FHIR Task.instantiatesCanonical and ActivityDefinition.url + ActivityDefinition.version
FHIR [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resources are used to announce what processes can be instantiated at any given DSF instance. They also control what kind of organization can request the instantiation or continuation of a process instance and what kind of organization is allowed to fulfill the request.

BPMN models have an ID we call process definition key. In order to link the FHIR and BPMN worlds the BPMN process definition key needs to be specified following the pattern `^[-a-zA-Z0-9]+_[-a-zA-Z0-9]+$` for example:  
```
domainorg_processKey
```
In addition, the BPMN process needs to specify a process version with the pattern `\d+\.\d+\.\d+\.\d+` for example:
```
1.0.2.0
```

This results in a canonical URL used to identify the process, for example:
```
http://domain.org/bpe/Process/processKey|1.0
```
This canonical URL is used as is for [Task.instantiatesCanonical](http://hl7.org/fhir/R4/task.html). It's also used for [ActivityDefinition.url](http://hl7.org/fhir/R4/activitydefinition.html) while omitting the resource version and using the `#{version}` placeholder in `ActivityDefinition.version`.

As you can see, we lost the `.2.0` part of the original process version. This is because the first two numbers (`1.0`) specify the FHIR resource version
used by this process plugin and latter two (`2.0`) specify the version of the plugin code base. This was done so that minor version changes in the code base
of a process plugin don't automatically invalidate communication to all other deployed instances of this process plugin. Instead, it will be assumed that as
long as two deployed process plugins share the same FHIR resource version number, they will be sufficiently compatible to remain operational. Larger changes in BPMN process flow
usually require the addition of new Task profiles or changes to the ActivityDefinition. It is expected that your resource version reflects these changes to signal (in)compatibility appropriately.  

### Authorization for the DSF
FHIR [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resources also contain the authorization rules for the specified process. [ActivityDefinitions](http://hl7.org/fhir/R4/activitydefinition.html) for the DSF need to comply with the [http://dsf.dev/fhir/StructureDefinition/activity-definition](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-activity-definition-1.0.0.xml) profile, with authorization rules configured using the [http://dsf.dev/fhir/StructureDefinition/extension-process-authorization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml) extension.

The authorization extension needs to be configured at least once and has four sub extensions:
#### message-name [1..1]
String value specifying the message name of [Message Start Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-start-event), [Intermediate Message Catch Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-intermediate-catching-event) or [Message Receive Task](https://docs.camunda.org/manual/7.17/reference/bpmn20/tasks/receive-task/) this authorization rule should match. Can only be specified once per authorization rule extension.

#### task-profile [1..1]
Canonical URL value specifying the [Task](http://hl7.org/fhir/R4/task.html) profile this authorization rule should match. Can only be specified once per authorization rule extension.

#### requester [1..]
Coding value matching entries from the [http://dsf.dev/fhir/ValueSet/process-authorization-requester](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/ValueSet/dsf-process-authorization-requester-1.0.0.xml) ValueSet:
* **LOCAL_ORGANIZATION** A local organization with a specific identifier.
    The organization identifier needs to specified using the [http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-organization-1.0.0.xml) extension.
    
* **REMOTE_ORGANIZATION** A remote (non local) organization with a specific identifier.
    The organization identifier needs to specified using the [http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-organization-1.0.0.xml) extension.
    
* **LOCAL_ROLE** A local organizations with a specific role defined via [OrganizationAffiliation](http://hl7.org/fhir/R4/organizationaffiliation.html).
    Role and parent organization identifier need to be specified using the [http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-parent-organization-role-1.0.0.xml) extension.
    
* **REMOTE_ROLE** A remote (non local) organizations with a specific role defined via [OrganizationAffiliation](http://hl7.org/fhir/R4/organizationaffiliation.html).
    Role and parent organization identifier need to be specified using the [http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-parent-organization-role-1.0.0.xml) extension.
    
* **LOCAL_ALL** All local organizations regardless of their identifier or role in a consortium.

* **REMOTE_ALL** All remote (non local) organizations regardless of their identifier or role in a consortium.

#### recipient [1..]
Coding value matching entries from the [http://dsf.dev/fhir/ValueSet/process-authorization-recipient ValueSet](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/ValueSet/dsf-process-authorization-recipient-1.0.0.xml).
* **LOCAL_ORGANIZATION** Organization with a specific identifier.
    The organization identifier needs to specified using the [http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-organization-1.0.0.xml) extension.
    
* **LOCAL_ROLE** Organizations with a specific role defined via [OrganizationAffiliation](http://hl7.org/fhir/R4/organizationaffiliation.html).
    Role and parent organization identifier need to be specified using the [http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-parent-organization-role-1.0.0.xml) extension.
    
* **LOCAL_ALL** All organizations regardless of their identifier or role in a consortium.

_The local organization of a DSF instance is configured using the environment variables [DEV_DSF_FHIR_SERVER_ORGANIZATION_IDENTIFIER_VALUE](https://dsf.dev/stable/maintain/fhir/configuration.html#dev-dsf-fhir-server-organization-identifier-value) for the DSF FHIR server and [DEV_DSF_BPE_FHIR_SERVER_ORGANIZATION_IDENTIFIER_VALUE](https://github.com/highmed/dsf-dsf/wiki/DSF-0.7.0-Configuration-Parameters-BPE#org_dev_dsf_bpe_fhir_server_organization_identifier_value) for the DSF BPE server._

#### Authorization Extension Example
The following example specifies that process execution can only be requested by an organization with a specific identifier and only allows execution of the process in the DSF instance of an organization with a specific identifier.
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
			<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization">
				<valueIdentifier>
					<system value="http://dsf.dev/sid/organization-identifier" />
					<value value="identifier.remote.org" />
				</valueIdentifier>
			</extension>
			<system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
			<code value="REMOTE_ORGANIZATION" />
		</valueCoding>
	</extension>
	<extension url="recipient">
		<valueCoding>
			<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization">
				<valueIdentifier>
					<system value="http://dsf.dev/sid/organization-identifier" />
					<value value="identifier.local.org" />
				</valueIdentifier>
			</extension>
			<system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
			<code value="LOCAL_ORGANIZATION" />
		</valueCoding>
	</extension>
</extension>
```

### Setting Targets for Message Events

Setting a target for a message event requires a `Target` object. To create one, you require a target's organization identifier, endpoint identifier and endpoint address.
There are two ways of adding `targets` to the BPMN execution variables:
#### 1. Adding the target in the message event implementation
In your message event implementation (the class extending `AbstractTaskMessageSend`), you can override `AbstractTaskMessageSend#doExecute`,
add your targets and then call the super-method.
#### 2. Adding the target in a service  task right before the message event
This is the preferred method of this tutorial but both methods will work perfectly fine. For our use-cases, we usually prefer this one
since there is enough complexity to warrant putting it into a separate BPMN service task.  

In both cases you can access methods to create and set `targets` through the `variables` instance.

## Exercise Tasks
1. Replace the [End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/none-events/#none-end-event) of the `dsfdev_helloDic` process in the `hello-dic.bpmn` file with a [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event). Give the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) a name and an ID and set its implementation to the `HelloCosMessage` class.  
   Configure field injections `instantiatesCanonical`, `profile` and `messageName` in the BPMN model for the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event).
    Use `http://dsf.dev/fhir/StructureDefinition/task-hello-cos|#{version}` as the profile and `helloCos` as the message name. Figure out what the appropriate `instantiatesCanonical` value is, based on the name (process definition key) of the process to be triggered.
1. Modify the `dsfdev_helloCos` process in the `hello-cos.bpmn` file and configure the message name of the [Message Start Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-start-event) with the same value as the message name of the [Message End Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/message-events/#message-end-event) in the `dsfdev_helloDic` process. 
1. Create a new [StructureDefinition](http://hl7.org/fhir/R4/structuredefinition.html) with a [Task](http://hl7.org/fhir/R4/task.html) profile for the `helloCos` message.
    <details>
   <summary>Don't know how to get started?</summary>
   
   You can base this Task profile off the `StructureDefinition/task-hello-dic.xml` resource. Then take a look at elements that need to be added, changed or can be omitted.
    </details>
1. Create a new [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resource for the `dsfdev_helloCos` process and configure the authorization extension to allow the `Test_DIC` organization as the requester and the `Test_COS` organization as the recipient.
    <details>
   <summary>Don't know how to get started?</summary>

   You can base this ActivityDefinition off the `ActivityDefinition/hello-dic.xml` resource. Then take a look at elements that need to be added, changed or can be omitted.
    </details>
    <details>
       <summary>Don't know how to create a proper authorization extension?</summary>
    
    You can base the authorization extension off the authorization extension example.
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
To verify the `dsfdev_helloDic` and `dsfdev_helloCos` processes can be executed successfully, we need to deploy them into DSF instances and execute the `dsfdev_helloDic` process. The maven `install` build is configured to create a process jar file with all necessary resources and copy the jar to the appropriate locations of the docker test setup.

1. Start the DSF FHIR server for the `Test_DIC` organization in a console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-fhir
   ```
   Verify the DSF FHIR server started successfully.

2. Start the DSF BPE server for the `Test_DIC` organization in another console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up dic-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloDic` process.

3. Start the DSF FHIR server for the `Test_COS` organization in a console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up cos-fhir
   ```
   Verify the DSF FHIR server started successfully. You can access the webservice of the DSF FHIR server at https://cos/fhir.  
   The DSF FHIR server uses a server certificate that was generated during the first maven build. To authenticate yourself to the server you can use the client certificate located at `.../dsf-process-tutorial/test-data-generator/cert/Webbrowser_Test_User/Webbrowser_Test_User_certificate.p12` (Password: password).

4. Start the DSF BPE server for the `Test_COS` organization in another console at location `.../dsf-process-tutorial/test-setup`:
   ```
   docker-compose up cos-bpe
   ```
   Verify the DSF BPE server started successfully and deployed the `dsfdev_helloCos` process. The DSF BPE server should print a message that the process was deployed. The DSF FHIR server should now have a new [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resource. Go to https://cos/fhir/ActivityDefinition to check if the expected resource was created by the BPE while deploying the process. The returned FHIR [Bundle](http://hl7.org/fhir/R4/bundle.html) should contain two [ActivityDefinition](http://hl7.org/fhir/R4/activitydefinition.html) resources. Also, go to https://cos/fhir/StructureDefinition?url=http://dsf.dev/fhir/StructureDefinition/task-hello-cos to check if the expected [Task](http://hl7.org/fhir/R4/task.html) profile was created.

5. Start the `dsfdev_helloDic` process by posting a specific FHIR [Task](http://hl7.org/fhir/R4/task.html) resource to the DSF FHIR server of the `Test_DIC` organization:
   Execute therefore the `main` method of the `dev.dsf.process.tutorial.TutorialExampleStarter` class to create the [Task](http://hl7.org/fhir/R4/task.html) resource needed to start the `dsfdev_helloDic` process.

   Verify that the FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server and the `dsfdev_helloDic` process was executed by the DSF BPE server of the `Test_DIC` organization. The DSF BPE server of the `Test_DIC` organization should print a message showing that a [Task](http://hl7.org/fhir/R4/task.html) resource to start the `dsfdev_helloCos` process was send to the `Test_COS` organization.  
   Verify that a FHIR [Task](http://hl7.org/fhir/R4/task.html) resource was created at the DSF FHIR server of the `Test_COS` organization and the `dsfdev_helloCos` process was then executed by the DSF BPE server of the `Test_COS` organization.

___
[Prerequisites](prerequisites.md) • [Exercise 1](exercise-1.md) • [Exercise 1.1](exercise-1-1.md) • [Exercise 2](exercise-2.md) • **Exercise 3** • [Exercise 4](exercise-4.md) • [Exercise 5](exercise-5.md)