# Basic Concepts And Lessons

This is a centralized write-up of all concepts and lessons you will need to solve the tutorial exercises.
Exercises will tell you which concepts or lessons you need to be familiar with to be able to solve them 
but may also provide additional information concerning that concept to solve that specific exercise.  
Make sure you have read the [prerequisites](prerequisites.md).

## Disclaimer
The concept of `Tasks` exists in both the FHIR and BPMN domains. For this tutorial `Task resource` always refers
to [FHIR Tasks](https://www.hl7.org/fhir/R4/task.html) and `Service Task` always means the BPMN concept.

## BPMN Model

The DSF expects BPMN 2.0 for its process execution. This write-up will cover BPMN elements 
most commonly used in DSF process plugins.

### Sequence Flow
BPMN 2.0 calls the continuous arrows connecting the BPMN elements in BPMN models, Sequence Flow.
Sequence Flow exits one BPMN element and points at the next BPMN element to be processed.

### Service Tasks
**First use in [exercise 1](exercise-1.md)**

You will primarily use [Service Tasks](https://docs.camunda.org/manual/7.20/reference/bpmn20/tasks/service-task/) 
when creating BPMN models. They are different from regular BPMN Tasks in that they offer the ability to 
link an implementation to the [Service Task](https://docs.camunda.org/manual/7.20/reference/bpmn20/tasks/service-task/)
which can be called and executed by a BPMN engine. This BPE (Business Process Engine) server of the DSF leverages
this feature to execute your BPMN processes.

### Messaging
**First use in [exercise 3](exercise-3.md)**

In order to enable communication with other lanes, pools or even entirely separate processes you need to be able
to exchange information. In BPMN, you can use [Message Events](https://docs.camunda.org/manual/7.20/reference/bpmn20/events/message-events/)
to model this information exchange. Modeling communication with [Message Events](https://docs.camunda.org/manual/7.20/reference/bpmn20/events/message-events/) in the same diagram
uses Message Flow. Message Flow is typically represented by a dashed line arrow between BPMN elements with a black (send) or white (receive) envelope icon.
The following BPMN collaboration diagram shows message exchange between two processes.

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="figures/exercise3_message_flow_inverted.svg">
  <source media="(prefers-color-scheme: light)" srcset="figures/exercise3_message_flow.svg">
  <img alt="BPMN collaboration diagram with two processes using message flow to exchange information between two organizations" src="figures/exercise3_message_flow.svg">
</picture>

#### Message Start Event

[Message Start Events](https://docs.camunda.org/manual/7.20/reference/bpmn20/events/message-events/#message-start-event)
allow a BPMN process to be started by an incoming message. In the DSF, all BPMN processes are started via messages. Therefore, 
you will have to include a [Message Start Event](https://docs.camunda.org/manual/7.20/reference/bpmn20/events/message-events/#message-start-event) at
the beginning of all of your BPMN models.

#### Message Intermediate Throwing Event
[Message Intermediate Throwing Events](https://docs.camunda.org/manual/7.20/reference/bpmn20/events/message-events/#message-intermediate-throwing-event)
are used to send messages during process execution. 

#### Message Intermediate Catching Event
[Message Intermediate Catching Events](https://docs.camunda.org/manual/7.20/reference/bpmn20/events/message-events/#message-intermediate-catching-event) serve as
the counterpart to [Message Intermediate Throwing Events](basic-concepts-and-lessons.md#message-intermediate-throwing-event). 
Use them whenever you expect to receive a message from another process or organization during execution.

#### Message End Event
The [Message End Event](https://docs.camunda.org/manual/7.20/reference/bpmn20/events/message-events/#message-end-event) will 
stop the execution of a BPMN process and finish by sending a message.

### Gateways

[Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/) allow you to control the [Sequence Flow](basic-concepts-and-lessons.md#sequence-flow). Different
types of gateways will be useful for different scenarios.

#### Exclusive Gateways
**First use in [exercise 4](exercise-4.md)**

[Exclusive Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/)
allow you to decide which [Sequence Flow](basic-concepts-and-lessons.md#sequence-flow) should be followed based on [conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions). 
[Conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions) aren't part of the 
[Exclusive Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/) themselves. You set them 
through the sequence Flow Exiting the [Exclusive Gateway](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/).  
In the [Camunda Modeler](https://camunda.com/download/modeler/), you can add conditions to [Sequence Flow](basic-concepts-and-lessons.md#sequence-flow) by selecting
a [Sequence Flow](basic-concepts-and-lessons.md#sequence-flow) and opening the `Condition` tab. You can find more information on how to
use Conditions [here](basic-concepts-and-lessons.md#conditions).

#### Event-based Gateway
**First use in [exercise 5](exercise-5.md)**

The [Event-based Gateway](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/event-based-gateway/)
allows you model scenarios where you are expecting one out of a number of events to occur. 

### Timer Intermediate Catching Events
**First use in [exercise 5](exercise-5.md)**

A [Timer Intermediate Catching Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event)
allows you model stopwatch behavior. A timer is started once the BPMN execution arrives at the event.
The duration until the timer runs out is specified using the [ISO 8601 Durations](http://en.wikipedia.org/wiki/ISO_8601#Durations) format. 
Examples can be found [here](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#time-duration). After running out, the BPMN process executes the [Sequence Flow](basic-concepts-and-lessons.md#sequence-flow) following
the [Timer Intermediate Catching Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event).

### Conditions
**First use in [exercise 4](exercise-4.md)**

[Conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions) 
allow you to change the behaviour of BPMN processes during execution. There are two ways you 
are able to add decision logic to Conditions. The [Camunda Modeler](https://camunda.com/download/modeler/) refers to them as `Type`. You can find them in the ``Condition`` tab of 
certain BPMN elements. The first one is `Script`. This allows you to add arbitrary complexity 
to your decisions logic and is rarely used for process plugins. The more common Type is `Expression`.
Expressions have the following syntax: `${expression}`. For this tutorial, _expression_ will
use a boolean condition like `var == true`. You can learn more advanced features of Expressions [here](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/).
 For this to work during BPMN process execution, the variable you want to use for the boolean 
condition must be available in the BPMN process variables before [Sequence Flow](basic-concepts-and-lessons.md#sequence-flow)
reaches the evaluation of the expression.

## FHIR

The DSF uses a variety of [FHIR resources](https://dsf.dev/intro/info/basics.html#why-are-we-using-fhir-and-bpmn). 
The most important ones for plugin development will be [ActivityDefinitions](http://hl7.org/fhir/R4/activitydefinition.html), [Tasks](https://www.hl7.org/fhir/R4/task.html), 
[CodeSystems](https://www.hl7.org/fhir/R4/codesystem.html) and [ValueSets](https://www.hl7.org/fhir/R4/valueset.html).
There is also a catalogue of DSF-specific FHIR resources including CodeSystems, ValueSets and Extensions. For now, you can find them in the official
DSF GitHub repository [here](https://github.com/datasharingframework/dsf/tree/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir).

### ActivityDefinition

[ActivityDefinitions](http://hl7.org/fhir/R4/activitydefinition.html) are used by the DSF to advertise which processes are
available at any given instance and who is allowed to request and who is allowed to execute a process. 
For this they use an extension called [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml).
Having an understanding of this extension and its references to the other DSF-specific FHIR resources is vital
to ensure your plugin will only work with the communication partners you intend to include for your particular use-case.

### Task
**First use in [exercise 1](exercise-1.md)**

The [FHIR Task](https://www.hl7.org/fhir/R4/task.html) resource enables the DSF's distributed communication. 
Whenever a BPMN process instance communicates with a different process instance, the DSF will create a Task resource 
based on parameters you set in the BPMN model itself but also during execution. It will then 
automatically send the Task resource to the recipient to start or continue whatever process the Task resource referred to.
All Task resources used in the DSF derive from the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml).
This profile includes a splicing for `Task.input` with three additional [Input Parameters](basic-concepts-and-lessons.md#task-input-parameters):  
- `message-name`
- `business-key`
- `correlation-key`

When creating your own plugin, you will want to create your own profiles based on the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml).

#### Task Input Parameters

Task Input Parameters allow you to add additional information to [Task](basic-concepts-and-lessons.md#task) resources.
For example, if your particular data exchange requires additional medical data, you would add a slice to your Task profile in the same
way the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml) adds slices to the original [FHIR Task](https://www.hl7.org/fhir/R4/task.html) resource. Notice that this also requires creating a [CodeSystem](basic-concepts-and-lessons.md#codesystem) and
including it in a [ValueSet](basic-concepts-and-lessons.md#valueset) to be able to use it in the Task resource.

### CodeSystem

Plugin development for the DSF requires the use of [CodeSystems](https://www.hl7.org/fhir/R4/codesystem.html) in two major ways:
1. Using existing DSF [CodeSystems](https://github.com/datasharingframework/dsf/tree/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem) in other FHIR resources like the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml).
2. Creating your own CodeSystem to add additional [Input Parameters](basic-concepts-and-lessons.md#task-input-parameters) to your [Task](https://www.hl7.org/fhir/R4/task.html) profiles.

### ValueSet

[ValueSets](https://www.hl7.org/fhir/R4/valueset.html) are mostly needed to use the [Concepts](https://www.hl7.org/fhir/R4/codesystem-definitions.html#CodeSystem.concept) from [CodeSystems](basic-concepts-and-lessons.md#codesystem)
in your [Task](basic-concepts-and-lessons.md#task) profiles.

## DSF

This section will include all the DSF-specific functionality required to create basic process plugins.

### Tutorial Project
**First use in [exercise 1](exercise-1.md)**

The tutorial project consists of three parts: 
1. A `test-data-generator` project used to generate X.509 certificates and FHIR resources
during the maven build of the project. The certificates and FHIR resources are needed to start DSF instances which simulate
installations at three different organizations. 
2. The DSF instances are created using [Docker](https://www.docker.com/) and configured using
a `docker-compose.yml` file in the `dev-setup` folder. The docker-compose test setup uses a single PostgreSQL database server,
a single nginx reverse proxy as well as three separate DSF FHIR server instances and 3 separate DSF BPE server instances.
3. The `tutorial-process` project contains all resources (FHIR resources, BPMN models and Java code) for the actual
DSF process plugin. Java code for the `tutorial-process` project is located at `src/main/java`, FHIR resources and
BPMN process models at `src/main/resources` as well as prepared JUnit tests to verify your solution at `src/test/java`.

### Certificates
**First use in [exercise 1](exercise-1.md)**

There is a number of certificates that need to be generated in order for DSF instances to communicate with each other securely.
You can find a comprehensive lists of certificates needed by the [DSF FHIR](https://dsf.dev/stable/maintain/fhir/configuration.html)
and [DSF BPE](https://dsf.dev/stable/maintain/bpe/configuration.html) servers on the DSF website.  
Certificates will be created by the `test-data-generator` project through Maven by the time of the `package` phase in your process plugin build.
You can also invoke the generation of certificates separately by running the Maven build of `test-data-generator` until (and including) the `package` phase.   
Since this tutorial comes with three preconfigured DSF instances, the only time you will need to interact with certificates
is when you want to make requests to the DSF FHIR server. Either for access to the web frontend under https://instance-host-name/fhir/,
or when [starting your process plugin](basic-concepts-and-lessons.md#starting-a-process-via-task-resources).  
In case of the web frontend, you will need to add the CA certificate and client certificate of the DSF instance you want to access to your browser.
Certificates can be found in `test-data-generator/cert`.  

**Example:**  
You want to access the `dic` DSF FHIR server. You add the CA certificate located in `test-data-generator/cert/ca` to your 
browser's certificate store. You also add the client certificate for `dic` located in `test-data-generator/cert/dic-client`
to your browser's client certificates.

**Important: Passwords for .p12 files are always "password"**

### The Process Plugin Definition
**First use in [exercise 1](exercise-1.md)**

In order for the DSF BPE server to load your plugin you need to provide it with the following information:
* A plugin [version](basic-concepts-and-lessons.md#about-versions)
* A release date
* A plugin name
* The BPMN model files
* The FHIR resources grouped by BPMN process ID. Your plugin may have any number of BPMN models. Each has their own BPMN process ID and FHIR resources specific to that BPMN process (think [Tsak](basic-concepts-and-lessons.md#task) resources needed for messages specific to that BPMN model)
* The Class holding your [Spring Configuration](basic-concepts-and-lessons.md#spring-integration)

You will provide this information by implementing the `dev.dsf.bpe.ProcessPluginDefinition` interface.
The DSF BPE server then searches for classes implementing this interface using the
Java [ServiceLoader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html) mechanism. Therefore, you will have to register your interface implementation in the `src/main/resources/META-INF/services/dev.dsf.bpe.ProcessPluginDefinition` file.
For this tutorial, the class implementing the `ProcessPluginDefinition` interface, `TutorialProcessPluginDefinition`,
has already been added to the file. You can use it as a reference for later when you want to create your own plugin.

### About Versions, Placeholders and URLs

#### Version Pattern
**First use in [exercise 3](exercise-3.md)**

Process plugin versions have to obey the pattern: 
```
\d+\.\d+\.\d+\.\d+  Example: 1.0.1.2
```

The first two numbers (`1.0`) are used in FHIR resources and signal changes which break compatibility with previous
process versions. The latter two (`1.2`) signal changes which don't break compatibility with previous process versions. Specifically,
the 4th number is reserved for bug-fixes and the 3rd number includes all other non-breaking changes.

#### Placeholders
**First use in [exercise 2](exercise-2.md)**

To avoid the need to specify the version and release date for each [CodeSystem](basic-concepts-and-lessons.md#codesystem),
[Task](basic-concepts-and-lessons.md#task) profile and [ValueSet](basic-concepts-and-lessons.md#valueset) resource, 
the placeholders `#{version}` and `#{date}` can be used when creating FHIR resources or even in BPMN models. 
They are replaced with the values returned by the methods `ProcessPluginDefinition#getResourceVersion` 
and `ProcessPluginDefinition#getReleaseDate` respectively during deployment of a process plugin by the DSF BPE server.

#### URLs
**First use in [exercise 3](exercise-3.md)**

BPMN models have an ID we call process definition key. The BPMN process definition key needs to be specified following the pattern:
```
^[-a-zA-Z0-9]+_[-a-zA-Z0-9]+$   Example: domainorg_processKey
```
In addition, the BPMN model needs to specify a version. You should be using the ``#{version}`` [placeholder](basic-concepts-and-lessons.md#placeholders) 
for this as well. The DSF will use the process definition key and the version specified in the BPMN model to create a
URL to refer to this specific process. Like this:
```
http://domain.org/bpe/Process/processKey|1.0
```

As you can see, the version in the URL ``|1.0`` only uses the resource version and omits the code base version.
As mentioned in [Version Pattern](basic-concepts-and-lessons.md#version-pattern), this means that only changes to the first two
version numbers are significant to signal compatibility when communicating with other process plugin instances.

You will use the above URL as your instantiatesCanonical value for [Task](basic-concepts-and-lessons.md#task) profiles. You will
also use it as the URL value for your [ActivityDefinitions](basic-concepts-and-lessons.md#activitydefinition). In this case though, you
have to split up the URL into two parts. You will separate the version (``|1.0``) from the URL and use it as a value for the
`ActivityDefinition.version` element. Since it refers to the plugin's resource version, you should also use the `#{version}`
[placeholder](basic-concepts-and-lessons.md#placeholders) here, instead. Going by the example from above, you will be left with a URL that looks
like this:
```
http://domain.org/bpe/Process/processKey
```
This will be the value for your `ActivityDefinition.url` element.

### Spring Integration
**First use in [exercise 1](exercise-1.md)**

Since the DSF also employs the use of the [Spring Framework](https://spring.io/projects/spring-framework) you will also
have to provide some Spring functionality.
When deployed, every process plugin exists in its own [Spring context](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html). To make the process plugin work, you
have to provide [Spring Beans](https://docs.spring.io/spring-framework/reference/core/beans/definition.html) with `prototype` scope for all classes which either extend or implement the following classes/interfaces (as of version 1.4.0): 
- `AbstractTaskMessageSend`
- `AbstractServiceDelegate`
- `DefaultUserTaskListener`
- `ProcessPluginDeploymentStateListener`

A [Spring-Framework configuration class](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-java-basic-concepts) located in `spring/config` is expected to provide the Spring Beans. 
For this tutorial, the `TutorialConfig` class will take this role.

### Read Access Tag
**First use in [exercise 2](exercise-2.md)**

Axiomatically, nobody is allowed to write FHIR resources (except [Task](basic-concepts-and-lessons.md#task)) to the DSF FHIR server 
unless it's your own organization. By default, the same applies to reading FHIR resources
(again except [Task](basic-concepts-and-lessons.md#task)). But since the DSF is often used to offer medical data in form of
FHIR resources, you will find yourself wanting other organizations to be allowed to read the resources you are offering.
The `Resource.meta.tag` field is used define access rules for all FHIR resources in the DSF, with the
exception of [Task](basic-concepts-and-lessons.md#task) resources. We will explain the reason shortly. 
To allow read access for all organizations (the standard for metadata resources), 
the following `read-access-tag` value can be written into this field:

```xml
<meta>
   <tag>
      <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
      <code value="ALL" />
   </tag>
</meta>
```

The read access rules for [Task](basic-concepts-and-lessons.md#task) resources are defined through the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml) in your plugin's
[ActivityDefinitions](basic-concepts-and-lessons.md#activitydefinition). Therefore, no `read-access-tag` is needed.

It is also possible to restrict read access of FHIR resources to organizations with 
a specific role in a parent organization or a specific identifier.
If you want to find out more, you may look at the [dsf-read-access-tag](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-read-access-tag-1.0.0.xml).

### Process Access Control
**First use in [exercise 1](exercise-1.md)**

In order to manage who is allowed to request/process [Task](basic-concepts-and-lessons.md#task) resources in your BPMN process,
you will use the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml).
It is part of [Activity Definition](basic-concepts-and-lessons.md#activitydefinition) resources which are used to 
advertise available DSF process plugins to other DSF instances. 
The extension defines access rules for all [Task](basic-concepts-and-lessons.md#task) resources
available to the BPMN process. Depending on your use-case
the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml) will look quite different once configured. 
We will first look at the elements all permutations have in common and then show some example 
scenarios and how their extension configuration would look like.

All extension permutations have these elements in common:
* Exactly 1 `message-name` element
* Excatly 1 `task-profile` element
* At least 1 `requester` element
* At least 1 `recipient` element

The `message-name` refers to the name of the [Message Event](basic-concepts-and-lessons.md#messaging) inside your BPMN model.
The `task-profile` then references the [Task](basic-concepts-and-lessons.md#task) resource used for communicating this [Message Event](basic-concepts-and-lessons.md#messaging).  
The `requester` and `recipient` elements specify how certain organizations or users are allowed to use the [Task](basic-concepts-and-lessons.md#task) resource
specified by `task-profile`.

When you take a look at the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml), you will find a list of 
[Codes](https://www.hl7.org/fhir/R4/datatypes.html#code) for the `requester` and `recipient` elements.
You will choose one of these Codes depending on your use-case. And depending on which Code
you choose you will have to put together the right [DSF FHIR resources](https://github.com/datasharingframework/dsf/tree/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir)
to configure the extension properly.  
For the purposes of this tutorial we have 3 example scenarios and how their [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml)
would be configured.

#### Scenario 1

You want all remote organizations to be able to request BPMN process execution and all local 
organization to be able to execute the process:  
<details>
<summary>XML</summary>

```xml
<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
     <extension url="message-name">
         <valueString value="start-process-message" />
     </extension>
     <extension url="task-profile">
         <valueCanonical value="http://foo.org/fhir/StructureDefinition/profile|#{version}" />
     </extension>
     <extension url="requester">
         <valueCoding>
             <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
             <code value="REMOTE_ALL" />
         </valueCoding>
     </extension>
     <extension url="recipient">
         <valueCoding>
             <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
             <code value="LOCAL_ALL" />
         </valueCoding>
     </extension>
</extension>
```
</details>


#### Scenario 2

You want an organization with a certain [Identifier](https://www.hl7.org/fhir/R4/datatypes.html#Identifier) to be able to 
request BPMN process execution and an organization with a certain [Identifier](https://www.hl7.org/fhir/R4/datatypes.html#Identifier)
to be able to execute a BPMN process:  
<details>
<summary>XML</summary>

```xml
<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
     <extension url="message-name">
          <valueString value="start-process-message" />
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
</details>



### Scenario 3

You want all organizations with a certain role inside a parent organization to be able to
request BPMN process execution and all organizations with a certain role inside a parent
organization to be able to execute a BPMN process:
<details>
<summary>XML</summary>

```xml
<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
     <extension url="message-name">
            <valueString value="start-process-message" />
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
</details>




### BPMN Process Execution
**First use in [exercise 1](exercise-1.md)**

The BPMN process execution is the in-memory representation of a running BPMN process.
It holds all the BPMN elements from the BPMN model as well as the [BPMN process variables](basic-concepts-and-lessons.md#bpmn-process-variables)
and exists from the time when a deployed process plugin gets started until the time it stops running.
You have access to this representation in your Java code when overriding certain methods in [Service](basic-concepts-and-lessons.md#service-delegates) / [Message](basic-concepts-and-lessons.md#message-delegates) Delegates
like `doExecute` or `getAdditionalInputParameters` through the `execution` parameter.

### BPMN Process Variables
**First use in [exercise 1](exercise-1.md)**

BPMN process variables hold additional information which has to be available during BPMN process execution. 
Variables can be directly related to BPMN elements like the boolean value for [Conditions](basic-concepts-and-lessons.md#conditions), but
don't have to be. BPMN process variables are stored as key-value pairs with the key being the variable name. 
They are accessible during the entirety of the execution to all [Service](basic-concepts-and-lessons.md#service-delegates) / 
[Message](basic-concepts-and-lessons.md#message-delegates) Delegates.

You can learn how to access to the BPMN process variables [here](basic-concepts-and-lessons.md#accessing-bpmn-process-variables).

### Service Delegates
**First use in [exercise 1](exercise-1.md)**

Service Delegates are the Java representation of the [Service Tasks](basic-concepts-and-lessons.md#service-tasks) in your BPMN model.
You link a Service Delegate to a certain [Service Task](basic-concepts-and-lessons.md#service-tasks) by selecting the [Service Task](basic-concepts-and-lessons.md#service-tasks)
in the [Camunda Modeler](https://camunda.com/download/modeler/) and adding a Java class to the `Implementation` field.
Make sure you use the fully qualified class name. Like this: 
```
org.package.myClass
```
All that's left is for your Java class to extend `AbstractServiceDelegate` and override the `doExecute` method. 
This is the place where you can put your actual business logic. The method will be called when the [BPMN process execution](basic-concepts-and-lessons.md#bpmn-process-execution)
arrives at the [Service Task](basic-concepts-and-lessons.md#service-tasks) your Service Delegate is linked to.

### Message Delegates
**First use in [exercise 3](exercise-3.md)**

Message Delegates are the Java representation of the [Message Events](basic-concepts-and-lessons.md#messaging) in your BPMN model.
You link a Message Delegate to a certain [Message Event](basic-concepts-and-lessons.md#messaging) by selecting the Message Event
in the [Camunda Modeler](https://camunda.com/download/modeler/) and adding a Java class to the `Implementation` field.
Make sure you use the fully qualified class name. Like this:
```
org.package.myClass
```

You will only need Message Delegates for [Message Send Events](basic-concepts-and-lessons.md#messaging). Incoming messages will 
be resolved to the correct [BPMN process execution](basic-concepts-and-lessons.md#bpmn-process-execution) automatically using [Message Correlation](basic-concepts-and-lessons.md#message-correlation)
and the message inputs will be added to that execution's [process variables](basic-concepts-and-lessons.md#bpmn-process-variables).  

To make a Message Delegate for [Message Send Events](basic-concepts-and-lessons.md#messaging), your Java class needs to extend
`AbstractTaskMessageSend`. Most of the time, you won't be adding any processing logic to your Message Delegates, therefore you usually won't be overwriting
the `doExecute` method like with [Service Delegates](basic-concepts-and-lessons.md#service-delegates). Instead, you most likely want to 
aggregate the information you processed in earlier steps and attach it to a message. For this you need to overwrite the 
`getAdditionalInputParamters` method. The DSF translates BPMN messages 
into FHIR [Task](basic-concepts-and-lessons.md#task) resources to execute the communication modeled by your BPMN diagrams. The information
you are sending to another BPMN process is specified in the Task.input elements a.k.a. [Input Parameters](basic-concepts-and-lessons.md#task-input-parameters),
hence the name of the method.

### Environment Variables
**First use in [exercise 2](exercise-2.md)**

Environment variables offer a way to make configuration data available at the start of a [BPMN process execution](basic-concepts-and-lessons.md#bpmn-process-execution).
They are the same for all running process instances. They can be defined by adding a member variable with 
the [Spring-Framework @Value](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-value-annotations) annotation to the configuration class `TutorialConfig`. The value of the annotation uses 
the `${..}` notation and follows the form `${some.property:defaultValue}`, where each dot in the property name corresponds
to an underscore in the equivalent environment variable. Environment variables are always written upper-case. 
The property `some.property` therefore corresponds to the environment variable `SOME_PROPERTY`.

The DSF provides a feature to automatically generate documentation of environment variables during the Maven build process.
You can use the `@ProcessDocumentation` annotation to automatically generate Markdown documentation for all fields with this annotation. 
You simply have to add [dsf-tools-documentation-generator](https://mvnrepository.com/artifact/dev.dsf/dsf-tools-documentation-generator) as a maven plugin. 
You can take a look at the `pom.xml` for the `tutorial-process` submodule to see how you can add it to your own project. 
Keep in mind to point the `<workingPackage>` field to the package you want documentation for.

### DSF Process API Package

The [DSF Process API package](https://mvnrepository.com/artifact/dev.dsf/dsf-bpe-process-api-v1) consists of a set of utility classes designed to provide easy access to solutions for common problems.
This includes for example the `Variables` class, which provides access to the [BPMN process variables](basic-concepts-and-lessons.md#bpmn-process-variables).

#### Process Plugin Api
When creating [Service Delegates](basic-concepts-and-lessons.md#service-delegates) or [MessageDelegates](basic-concepts-and-lessons.md#message-delegates) you will 
notice that you need to provide a constructor which expects a `ProcessPluginApi` object and forward it to the superclasses' constructor.
This API instance provides a variety of utility classes. We will explicitly explore some on them in the tutorial.


### Message Correlation
**First use in [exercise 5](exercise-5.md)**

In order for messages to be able to be sent back and forth between organizations with potentially multiple of the 
same process plugin instances running at the same time and still arriving at the correct process instance,
we need some mechanism to map messages to their rightful process instance. 
This mechanism is called Message Correlation and requires attaching a unique identifier to every process instance.
This identifier is called the `business-key`. The `business-key` will get attached to every outgoing message automatically.  
  
It's possible that the `business-key` is insufficient to map messages to the correct process instance. This happens
when you use subprocesses in your BPMN model which all expect messages to be sent to them, not the parent process.
To solve this issue, [Task](basic-concepts-and-lessons.md#task) resources also come with an [Input Parameter](basic-concepts-and-lessons.md#task-input-parameters) called `correlation-key`.
This is a secondary identifier you can attach to all messages if you need them to arrive at a specific subprocess.
You can learn more about how `correlation-keys` are used by studying the [Ping-Pong Process](https://github.com/datasharingframework/dsf-process-ping-pong).

### Draft Task Resources
**First use in [exercise 1](exercise-1.md)**

[Task](basic-concepts-and-lessons.md#task) resources with status `draft` are used to create the DSF FHIR server's functionality
of starting processes via its web interface. They are stored in `.../tutorial-process/src/main/resources/fhir/Task`.
Compared to regular [Task](basic-concepts-and-lessons.md#task) resources used to
start BPMN processes, this type of [Task](basic-concepts-and-lessons.md#task) resource requires the status `draft` instead the usual `requested`.
It also replaces the value for `authoredOn` with the placeholder `#{date}`, the values of organization
identifiers with the placeholder `#{organization}` and all instances of version numbers with `#{version}`. 
Additionally, it requires setting the `Task.identifier`
element. It should look something like this:  

```xml
<identifier>
     <system value="http://dsf.dev/sid/task-identifier" />
     <value value="http://dsf.dev/bpe/Process/processKey/#{version}/task-name" />
</identifier>
```
`processKey` should be the same one used in [URLs](basic-concepts-and-lessons.md#urls).  
`task-name` can be any String you wish to identify this task with. You can use the file name for example. 

For a complete example you can take a look at the Draft Task Resource in one of the solution branches
and compare it to the one needed for cURL. The [Task](basic-concepts-and-lessons.md#task) resource created
for cURL can be found at `.../tutorial-process/src/main/resources/example-task.xml`.  

You might also want to check out [this guide](basic-concepts-and-lessons.md#creating-task-resources-based-on-a-definition) 
if you don't know how to create [Task](basic-concepts-and-lessons.md#task) resources in general.


## Lessons

### Accessing BPMN Process Variables
**First use in [exercise 1](exercise-1.md)**

After creating a [Service Delegate](basic-concepts-and-lessons.md#service-delegates) or [Message Delegate](basic-concepts-and-lessons.md#message-delegates), you might want to
retrieve data from or store data in the [BPMN process variables](basic-concepts-and-lessons.md#bpmn-process-variables).
You can achieve this either through the [BPMN process execution](basic-concepts-and-lessons.md#bpmn-process-execution) or via the `Variables` class.  
*It's very much recommended you use the latter method*. The `Variables` class provides lots of utility methods to read or write certain types
of [BPMN process variables](basic-concepts-and-lessons.md#bpmn-process-variables). If for some reason you need to fall back on the [BPMN process execution](basic-concepts-and-lessons.md#bpmn-process-execution)
to solve your problem, we would like to learn how the current API of the `Variables` class is limiting you. Contact us, and we might turn it into a feature request ([Contribute](https://dsf.dev/stable/contribute)).

### Accessing Task Resources During Execution
**First use in [exercise 2](exercise-2.md)**

If you want access to the [Task](basic-concepts-and-lessons.md#task) resources in your [Service](basic-concepts-and-lessons.md#service-delegates) / [Message](basic-concepts-and-lessons.md#message-delegates) Delegates, the `Variables` class will 
provide methods which return certain kinds of [Task](basic-concepts-and-lessons.md#task) resources. The most commonly used ones are
the start [Task](basic-concepts-and-lessons.md#task), referring to the [Task](basic-concepts-and-lessons.md#task) / [Message Start Event](basic-concepts-and-lessons.md#message-start-event) responsible for starting the process,
and the latest [Task](basic-concepts-and-lessons.md#task), referring to most recently received [Task](basic-concepts-and-lessons.md#task) / Message.  
In principle, this is sufficient to access all information in a [Task](basic-concepts-and-lessons.md#task) resource, since you have 
the [Task](basic-concepts-and-lessons.md#task) resource's Java object, but very cumbersome.
Instead of navigating the [Task](basic-concepts-and-lessons.md#task) resource's element tree,
you should first try to use the [ProcessPluginApi's](basic-concepts-and-lessons.md#process-plugin-api) `TaskHelper` in conjunction with the method above. The `TaskHelper` class
offers specific methods related to [Task](basic-concepts-and-lessons.md#task) resources.  
The most common use-case for this is retrieving data from a [Task's](basic-concepts-and-lessons.md#task) [Input Parameter](basic-concepts-and-lessons.md#task-input-parameters) or creating a new [Input Parameter](basic-concepts-and-lessons.md#task-input-parameters)
for a [Message Delegate's](basic-concepts-and-lessons.md#message-delegates) `getAdditionalInputParameters` method.  
When retrieving data from a [Task's](basic-concepts-and-lessons.md#task) Input Parameter you first have to get to the [Input Parameter](basic-concepts-and-lessons.md#task-input-parameters) you are looking to extract
data from. You can use one of the `TaskHelper's` getters for [Input Parameters](basic-concepts-and-lessons.md#task-input-parameters) to find the right one. The methods will try to match
the provided [CodeSystem](basic-concepts-and-lessons.md#codesystem) and Code to any [Input Parameter](basic-concepts-and-lessons.md#task-input-parameters) of the provided [Task](basic-concepts-and-lessons.md#task) resource.
Depending on the method you chose you will for example receive all matches or just the first one.  
To create new [Input Parameters](basic-concepts-and-lessons.md#task-input-parameters) to attach to a [Task](basic-concepts-and-lessons.md#task) resource, you may invoke the `TaskHelper#createInput` method. This
is most often used when overriding the `getAdditionalInputParamters` method of you [Message Delegate](basic-concepts-and-lessons.md#message-delegates).


### Setting Targets For Message Events
**First use in [exercise 3](exercise-3.md)**

Setting a target for a message event requires a `Target` object. To create one, you require a target's organization identifier, endpoint identifier and endpoint address.
There are two ways of adding `targets` to the BPMN execution variables:
#### 1. Adding the target in the message event implementation
In your message event implementation (the class extending `AbstractTaskMessageSend`), you can override `AbstractTaskMessageSend#doExecute`,
add your targets and then call the super-method.
#### 2. Adding the target in a service  task right before the message event
This is the preferred method of this tutorial but both methods will work perfectly fine. For our use-cases, we usually prefer this one
since there is enough complexity to warrant putting it into a separate BPMN [Service Task](https://docs.camunda.org/manual/7.17/reference/bpmn20/tasks/service-task/).

In both cases you can access methods to create and set `targets` through the `Variables` instance.

### Managing Multiple Incoming Messages And Missing Messages
**First use in [exercise 5](exercise-5.md)**

If an already running process instance is waiting for a message from another organization, the corresponding FHIR [Task](basic-concepts-and-lessons.md#task) may never arrive.
Either because the other organization decides to never send the message or because some technical problem prohibits the [Task](basic-concepts-and-lessons.md#task) resource from being posted to the DSF FHIR server.
This would result in stale process instances that never finish.

At the same time, you might also expect to receive one out of a number of different message types at once.

In order to solve both problems we can add an [Event Based Gateway](basic-concepts-and-lessons.md#event-based-gateway) to the process waiting 
for a response and then either handle a [Task](basic-concepts-and-lessons.md#task) resource with the response and finish the process in a success 
state or trigger a [Timer Intermediate Catching Event](basic-concepts-and-lessons.md#timer-intermediate-catching-events) after a defined wait period and finish the process in an error state. 
The following BPMN collaboration diagram shows how the process at the first organization would look like if we wanted to react to multiple different messages 
or missing messages:

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="figures/exercise5_event_based_gateway_inverted.svg">
  <source media="(prefers-color-scheme: light)" srcset="figures/exercise5_event_based_gateway.svg">
  <img alt="BPMN collaboration diagram with an Event Based Gateway" src="figures/exercise5_event_based_gateway.svg">
</picture>

### Starting A Process Via Task Resources
**First use in [exercise 1](exercise-1.md)**

To start a BPMN process, you need to create new a [Task](basic-concepts-and-lessons.md#task) resource in the DSF FHIR server
by sending an HTTP request according to the [FHIR RESTful API](https://www.hl7.org/fhir/R4/http.html). Specifically, you need to [create](https://www.hl7.org/fhir/R4/http.html#create)
a resource for the first time. Also, remember that the [Task](basic-concepts-and-lessons.md#task)
resource you are sending needs to comply to the [Task](basic-concepts-and-lessons.md#task) profile of the process you 
want to start and the [ActivityDefinition's](basic-concepts-and-lessons.md#activitydefinition) authorization rules.   
In this tutorial, there are two ways making this HTTP request:
1. Using cURL
2. Using the DSF FHIR server's web interface

#### Using cURL
But since cURL requires the actual [Task](basic-concepts-and-lessons.md#task) payload as an XML, it will prove useful to
gain more insight in how actual [Task](basic-concepts-and-lessons.md#task) resources look like and how they relate to
your [Task](basic-concepts-and-lessons.md#task) profiles and [ActivityDefinitions](basic-concepts-and-lessons.md#activitydefinition). You will have to create
an appropriate [Task](basic-concepts-and-lessons.md#task) resource for this. 
There already is a file called `example-task.xml` located in `tutorial-process/src/main/resources/fhir`.
You can use this as your starting point. You can try to follow [this guide](basic-concepts-and-lessons.md#creating-task-resources-based-on-a-definition),
or you can check the solution branches for this 
file if you need ideas on how to fill it out properly.

Below are some cURL command skeletons. Replace all <>-Placeholders with appropriate values. Host name depends on the
instance you want to address. For this tutorial this is either one of `dic`, `cos` or `hrp`. [Certificates](basic-concepts-and-lessons.md#certificates) can be found in 
`test-data-generator/cert`. Client [certificates](basic-concepts-and-lessons.md#certificates) and private keys can be found 
in the folder of their respective instance e.g. `test-data-generator/cert/dic-client` for the `dic` instance.

##### Linux:
```shell
curl https://<instance-host-name>/fhir/Task \
--cacert <path/to/ca-certificate-file.pem> \
--cert <path/to/client-certificate-file.pem>:password \
--key <path/to/client-private-key-file.pem> \
-H "Content-Type: application/fhir+xml" \
-H "Accept: application/fhir+xml" \
-d @<path/to/example-task.xml>
```
##### Windows CMD:
```shell
curl https://<instance-host-name>/fhir/Task ^
--cacert <path/to/ca-certificate-file.pem> ^
--cert <path/to/client-certificate-file.pem>:password ^
--key <path/to/client-private-key-file.pem> ^
-H "Content-Type: application/fhir+xml" ^
-H "Accept: application/fhir+xml" ^
-d @<path/to/example-task.xml>
```
*This may throw an error depending on which version of cURL Windows is using. If this is the case for you after making sure
you entered everything correctly, you can try using Git's version of cURL instead by adding it to the very top of your system's PATH environment
variable. Git's cURL is usually situated in C:\Program Files\Git\mingw64\bin.*

#### Using The DSF FHIR Server's Web Interface

When visiting the web interface of a DSF FHIR server instance (e.g. https://instance-name/fhir), you
can query the DSF FHIR server using the FHIR RESTful API to return a list of all [Draft Task Resources](basic-concepts-and-lessons.md#draft-task-resources).
These [Task](basic-concepts-and-lessons.md#draft-task-resources) resources act like a template you can use to 
instantiate [Task](basic-concepts-and-lessons.md#task) resources which start BPMN processes.  
Instead of querying the DSF FHIR server manually, you can use a predefined bookmark
to navigate to the query URL. You can find a list of Bookmarks in the top right corner of
the web interface. Simply select the bookmark referencing `?_sort=_profile,identifier&status=draft` under 
the `Task` section, and you will be taken to the list of all [Draft Task Resources](basic-concepts-and-lessons.md#draft-task-resources).  
Once there, you can select the one which starts your BPMN process. It will take you to a detailed view
of the resource where you will also have the chance to fill any [Task Input Parameters](basic-concepts-and-lessons.md#task-input-parameters)
you might need to specify.  
If everything is filled out correctly, you may start your process by clicking `Start Process`.  
Keep in mind that, for [Draft Task Resources](basic-concepts-and-lessons.md#draft-task-resources) to be 
available, you need to include them in your mapping for your BPMN process ID in `ProcessPluginDefinition#getFhirResourcesByProcessId`. 
Take a look at [the Process Plugin Definition](basic-concepts-and-lessons.md#the-process-plugin-definition) if you need a reminder.

### Creating Task Resources Based On A Definition

This short guide should help you understand how you can create [Task](basic-concepts-and-lessons.md#task)
resources for use in [Starting A Process Via Task Resources](basic-concepts-and-lessons.md#starting-a-process-via-task-resources).
We will employ the use of the free version of [Forge](https://simplifier.net/forge?utm_source=firely-forge) to help 
with visualization. You are invited to create a free account and follow along, but remember that the free 
version of Forge [mustn't be used commercially](https://simplifier.net/pricing).  
As an example, we will create a [Task](basic-concepts-and-lessons.md#task) resource from the `task-hello-dic.xml` profile.

#### 1st Step: Removing Placeholders
`task-hello-dic.xml` includes placeholders for the `version` and `date` elements. For the duration of this guide, 
you can either remove or comment these elements, so Forge does not try to perform type checking on them, which
would result in an error and Forge won't load the file.

#### 2nd Step: Differential Chain
If the resource profile is only available as a [differential](https://www.hl7.org/fhir/R4/profiling.html#snapshot), like in our
case, we have to aggregate the changes made to the base resource (in this case [Task](basic-concepts-and-lessons.md#task)) by all profiles to make
it more readable.
To do this, we first need all the profiles involved. We already have `task-hello-dic.xml` in our `StructureDefinition` folder.
It lists a resource called `task-base` in its `baseDefinition` element. This resource is part of the DSF and can be
found [here](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml).
Put it into the `StructureDefinition` folder. Since `task-base` has the original FHIR Task as its `baseDefinition`
element, we are done with this chain.
In forge, you should now be able to open the `StructureDefinition` folder and open the `task-hello-dic.xml` profile.
It should look something like this:  

![Forge overview](figures/forge_overview.png)

You can also calculate the [snapshot](https://www.hl7.org/fhir/R4/profiling.html#snapshot) instead and forego using Forge altogether
by reading the XML. If you know how to do this, you probably have no need for this guide.

#### 3rd Step: Building The Task Resource
We will now go through each element one by one and include it into our [Task](basic-concepts-and-lessons.md#task)
resource, if it is mandatory (cardinality at least 1..1) according to the profile. It's important to know
that you not use any placeholders like `#{version}` for resources not read by the DSF BPE server. This is the case
if we want a [Task](basic-concepts-and-lessons.md#task) resource for use with [cURL](basic-concepts-and-lessons.md#using-curl).
But, placeholders have to be used in [Draft Task Resources](basic-concepts-and-lessons.md#draft-task-resources) instead of actual values whenever possible, 
since those are read by the DSF BPE server. This guide will create a [Task](basic-concepts-and-lessons.md#task) resource without placeholders.  
We'll start out with the base element for all [Task](basic-concepts-and-lessons.md#task) resources:  
```xml
<Task xmlns="http://hl7.org/fhir">

</Task>
```

Before we start any elements listed in Forge's element tree, we have to include the `Task.meta.profile` element.
Its requirement cannot be seen here which is why we mention it specifically. This is the only instance you won't see
it in the element tree. It should look like this:  
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-hello-dic|1.0"/>
    </meta>
</Task>
```

The first element which can be found in the element tree is the `instantiatesCanonical` element. To add it, we
will create an XML element with the same name and the value according to [URLs](basic-concepts-and-lessons.md#urls): 
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-hello-dic|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/helloDic|1.0" />
</Task>
```
We can continue this process for all basic elements like these. Just make sure you pay attention to use the correct
data type (e.g. proper coding value for elements with `coding` type).

Let's look at a more complex element like the `requester` element:  

![Forge requester view](figures/forge_requester_view.png)

By now your [Task](basic-concepts-and-lessons.md#task) resources should look something like this:
<details>
<summary>Suggested solution</summary>

```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-hello-dic|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/helloDic|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
</Task>
```
</details>

We will start the same way we started with simple elements, by adding the `requester` element:  
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-hello-dic|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/helloDic|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
    <requester>
     
    </requester>
</Task>
```

Then, we'll add simple elements to `requester` like we did before for `Task`:  
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-hello-dic|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/helloDic|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
    <requester>
        <type value="Organization"/>
    </requester>
</Task>
```
*Important to note here that the value for the `status` will always be `requested` for Tasks being posted using cURL and the `type` element for `requester` and `recipient` will always have the value `Organization` in the DSF context.*

Next, we'll add the `identifier` element and its simple sub-elements just like we started out doing it for the `requester` element:  
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-hello-dic|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/helloDic|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
    <requester>
        <type value="Organization"/>
        <identifier>
            <system value="http://dsf.dev/sid/organization-identifier"/>
            <value value="Test_DIC" />
        </identifier>
    </requester>
</Task>
```
*Notice that `requester.identifier.system` has a `Fixed value` annotation. You can see what the value is supposed
to be by clicking on the `system` element in Forge or looking at the XML for the right Task profile. The right side will have all information about that element, including
the actual value for `Fixed value`.*

You should now be able to fill out all elements in your [Task](basic-concepts-and-lessons.md#task) resource until you reach
the [slicing](https://www.hl7.org/fhir/R4/profiling.html#slicing) for `Task.input`. Your [Task](basic-concepts-and-lessons.md#task)
resource should look something like this:  
<details>
<summary>Suggested solution</summary>

```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-hello-dic|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/helloDic|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
    <requester>
        <type value="Organization"/>
        <identifier>
            <system value="http://dsf.dev/sid/organization-identifier"/>
            <value value="Test_DIC" />
        </identifier>
    </requester>
    <restriction>
        <recipient>
            <type value="Organization"/>
            <identifier>
                <system value="http://dsf.dev/sid/organization-identifier" />
                <value value="Test_DIC" />
            </identifier>
        </recipient>
    </restriction>
</Task>
```
</details>


[Slicings](https://www.hl7.org/fhir/R4/profiling.html#slicing) are a bit different from regular elements. Let's look at the 
slice `message-name`:  

![Forge slice message name](figures/forge_slice_message_name.png)

If we were to continue including slices to the [Task](basic-concepts-and-lessons.md#task) resource like we did so far,
we would add a `message-name` element to our XML like this:  

```xml
<Task xmlns="http://hl7.org/fhir">
    ...
    <input>
        <message-name>
            ...
        </message-name>
    </input>
</Task>
```

This approach however, would not work. FHIR processors don't use the name of the slice to map entries in
your [Task](basic-concepts-and-lessons.md#task) resource to the correct slice. They use [discriminators](https://www.hl7.org/fhir/R4/profiling.html#discriminator).
Discriminators define the elements a processor needs to distinguish slices by. You can see
how the discriminator is configured by selecting the `input` element in Forge. In our case, a processor 
would look at the values for `input.type.coding.system` and `input.type.coding.code` to determine which
slice this element belongs to. This only works because `input.type.coding.system` and `input.type.coding.code`
are present in all slices and have a `Fixed value`. You can learn more about discriminators [here](https://www.hl7.org/fhir/R4/profiling.html#discriminator).  
All this means is that we effectively ignore the name of the slice as an element and start adding elements like we did before:  

```xml
<Task xmlns="http://hl7.org/fhir">
    ...
    <input>
        <type>
            <coding>
                <system value="http://dsf.dev/fhir/CodeSystem/bpmn-message" />
                <code value="message-name" />
            </coding>
        </type>
        <valueString value="helloDic" />
    </input>
</Task>
```

Now you should be able to add all remaining mandatory elements to your [Task](basic-concepts-and-lessons.md#task) 
resource. In the end, it should look something like this:  
<details>
<summary>Suggested solution</summary>

```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-hello-dic|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/helloDic|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
    <requester>
        <type value="Organization"/>
        <identifier>
            <system value="http://dsf.dev/sid/organization-identifier"/>
            <value value="Test_DIC" />
        </identifier>
    </requester>
    <restriction>
        <recipient>
            <type value="Organization"/>
            <identifier>
                <system value="http://dsf.dev/sid/organization-identifier" />
                <value value="Test_DIC" />
            </identifier>
        </recipient>
    </restriction>
    <input>
        <type>
            <coding>
                <system value="http://dsf.dev/fhir/CodeSystem/bpmn-message" />
                <code value="message-name" />
            </coding>
        </type>
        <valueString value="helloDic"/>
    </input>
</Task>
```
</details>