# Basic Concepts And Lessons

This is a centralized write-up of all concepts and lessons you will need to solve the tutorial exercises.
Exercises will tell you which concepts or lessons you need to be familiar with to be able to solve them 
but may also provide additional information concerning that concept to solve that specific exercise.  
Make sure you have read the [prerequisites](prerequisites.md).

## BPMN Model

The DSF expects BPMN 2.0 for its process execution. This write-up will cover BPMN elements 
most commonly used in DSF process plugins.

### Sequence Flow
BPMN 2.0 calls the continuous arrows connecting the BPMN elements in BPMN models, Sequence Flow.
Sequence Flow exits one BPMN element and points at the next BPMN element to be processed.

### Service Tasks

You will primarily use [Service Tasks](https://docs.camunda.org/manual/7.20/reference/bpmn20/tasks/service-task/) 
when creating BPMN models. They are different from normal BPMN Tasks in that they offer the ability to 
link an implementation to the [Service Task](https://docs.camunda.org/manual/7.20/reference/bpmn20/tasks/service-task/)
which can be called and executed by a BPMN engine. This BPE (Business Process Engine) server of the DSF leverages
this feature to execute your BPMN processes.

### Messaging

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

[Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/) allow you to control the Sequence Flow. Different
types of gateways will be useful for different scenarios.

#### Exclusive Gateways
[Exclusive Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/)
allow you to decide which Sequence Flow should be followed based on [conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions). 
[Conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions) aren't part of the 
[Exclusive Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/) themselves. You set them 
through the sequence Flow Exiting the [Exclusive Gateway](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/).  
In the [Camunda Modeler](https://camunda.com/download/modeler/), you can add conditions to Sequence Flow by selecting
a Sequence Flow and opening the `Condition` tab. You can find more information on how to
use Conditions [here](basic-concepts-and-lessons.md#conditions).

#### Event-based Gateway
The [Event-based Gateway](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/event-based-gateway/)
allows you model scenarios where you are expecting one out of a number of events to occur. 

### Timer Intermediate Catching Events

A [Timer Intermediate Catching Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event)
allows you model stopwatch behavior. A timer is started once the BPMN execution arrives at the event.
The duration until the timer runs out is specified using the [ISO 8601 Durations](http://en.wikipedia.org/wiki/ISO_8601#Durations) format. 
Examples can be found [here](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#time-duration). After running out, the BPMN process executes the Sequence Flow following
the [Timer Intermediate Catching Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event).

### Conditions

[Conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions) 
allow you to change the behaviour of BPMN processes during execution. There are two ways you 
are able to add decision logic to Conditions. The [Camunda Modeler](https://camunda.com/download/modeler/) refers to them as `Type`. You can find them in the ``Condition`` tab of 
certain BPMN elements. The first one is ``Script``. This allows you to add arbitrary complexity 
to your decisions logic and is rarely used for process plugins. The more common Type is ``Expression``.
Expressions have the following syntax: ``${expression}``. For this tutorial, _expression_ will
use a boolean condition like ``var == true``. You can learn more advanced features of Expressions [here](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/).
 For this to work during BPMN process execution, the variable you want to use for the boolean 
condition must be available in the BPMN process variables before Sequence Flow
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

The FHIR [Task](https://www.hl7.org/fhir/R4/task.html) resource enables the DSF's distributed communication. 
Whenever a BPMN process instance communicates with a different process instance, the DSF will create a Task resource 
based on parameters you set in the BPMN model itself but also during execution. It will then 
automatically send the Task resource to the recipient to start or continue whatever process the Task resource referred to.
All Task resources used in the DSF derive from the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml).
This profile includes a splicing for Task.input with three additional Input Parameters:  
- ``message-name``
- ``business-key``
- ``correlation-key``

When creating your own plugin, you will want to create your own profiles based on the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml).
For example, if your particular data exchange requires additional information, you would add a slice to your Task profile in the same
way the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml)
adds slices to the original FHIR [Task](https://www.hl7.org/fhir/R4/task.html) resource. Notice that this also requires creating a [CodeSystem](basic-concepts-and-lessons.md#codesystem) and
including it in a [ValueSet](basic-concepts-and-lessons.md#valueset) to be able to use it in the Task resource.

### CodeSystem

Plugin development for the DSF requires the use of [CodeSystems](https://www.hl7.org/fhir/R4/codesystem.html) in two major ways:
1. Using existing DSF [CodeSystems](https://github.com/datasharingframework/dsf/tree/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem) in other FHIR resources like the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml).
2. Creating your own CodeSystem to add additional Input Parameters to your [Task](https://www.hl7.org/fhir/R4/task.html) profiles.

### ValueSet

[ValueSets](https://www.hl7.org/fhir/R4/valueset.html) are mostly needed to use the [Concepts](https://www.hl7.org/fhir/R4/codesystem-definitions.html#CodeSystem.concept) from [CodeSystems](basic-concepts-and-lessons.md#codesystem)
in your [Task](basic-concepts-and-lessons.md#task) profiles.

## DSF

This section will include all the DSF-specific functionality required to create basic process plugins.

### Tutorial Project
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

### The Process Plugin Definition

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
For this tutorial, the class implementing the ``ProcessPluginDefinition`` interface, `TutorialProcessPluginDefinition`,
has already been added to the file. You can use it as a reference for later when you want to create your own plugin.

### About Versions, Placeholders and URLs

#### Version Pattern
Process plugin versions have to obey the pattern: 
```
\d+\.\d+\.\d+\.\d+  Example: 1.0.1.2
```

The first two numbers (`1.0`) are used in FHIR resources and signal changes which break compatibility with previous
process versions. The latter two (`1.2`) signal changes which don't break compatibility with previous process versions. Specifically,
the last number is reserved for bug-fixes and the number before that includes all other non-breaking changes.

#### Placeholders
To avoid the need to specify the version and release date for each [CodeSystem](basic-concepts-and-lessons.md#codesystem),
[Task](basic-concepts-and-lessons.md#task) profile and [ValueSet](basic-concepts-and-lessons.md#valueset) resource, 
the placeholders `#{version}` and `#{date}` can be used when creating FHIR resources or even in BPMN models. 
They are replaced with the values returned by the methods `ProcessPluginDefinition#getResourceVersion` 
and `ProcessPluginDefinition#getReleaseDate` respectively during deployment of a process plugin by the DSF BPE server.

#### URLs

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
Since the DSF also employs the use of the [Spring Framework](https://spring.io/projects/spring-framework) you will also
have to provide some Spring functionality.
When deployed, every process plugin exists in its own [Spring context](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html). To make the process plugin work, you
have to provide [Spring Beans](https://docs.spring.io/spring-framework/reference/core/beans/definition.html) with `prototype` scope for all classes which either extend or implement the following classes/interfaces (as of version 1.4.0): 
- `AbstractTaskMessageSend`
- `AbstractServiceDelegate`
- `DefaultUserTaskListener`
- `ProcessPluginDeploymentStateListener`

A [Spring-Framework configuration class](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-java-basic-concepts) located in `spring/config` is expected to provide the Spring Beans. 
For this plugin, the `TutorialConfig` class will take this role.

### Read Access Tag

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
a specific role in a parent organization or a specific identifier, but this is not covered in the tutorial.
If you want to find out more, you may look at the [dsf-read-access-tag](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-read-access-tag-1.0.0.xml) resource.

### Process Access Control

As mentioned in the description of [ActivityDefinition](basic-concepts-and-lessons.md#activitydefinition) and [Read Access Tag](basic-concepts-and-lessons.md#read-access-tag), you will
use the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml) to define access rules for all [Task](basic-concepts-and-lessons.md#task) resources
available to the BPMN process that ActivityDefinition represents. Depending on your use-case
the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml) will look quite different once configured. 
We will first look at the elements all permutations have in common and then show some example 
scenarios and how their extension configuration would look like.

All extensions have these elements in common:
* Exactly 1 ``message-name`` element
* Excatly 1 ``task-profile`` element
* At least 1 ``requester`` element
* At least 1 ``recipient`` element

When you take a look at the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml), you will find a list of 
[Codes](https://www.hl7.org/fhir/R4/datatypes.html#code) for the ``requester`` and `recipient` elements.
You will choose one of these Codes depending on your use-case. And depending on which Code
you choose you will have to puzzle together the right [DSF FHIR resources](https://github.com/datasharingframework/dsf/tree/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir)
to configure the extension properly.  
For the purposes of this tutorial we have 3 example scenarios and how their [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml)
would be configured.

#### Scenario 1

You want all remote organizations to be able to request BPMN process execution and all local 
organization to be able to execute the process:  

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
#### Scenario 2

You want an organization with a certain [Identifier](https://www.hl7.org/fhir/R4/datatypes.html#Identifier) to be able to 
request BPMN process execution and an organization with a certain [Identifier](https://www.hl7.org/fhir/R4/datatypes.html#Identifier)
to be able to execute a BPMN process:  
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

### Scenario 3

You want all organizations with a certain role inside a parent organization to be able to
request BPMN process execution and all organizations with a certain role inside a parent
organization to be able to execute a BPMN process:

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

### BPMN Process Execution

The BPMN process execution is the in-memory representation of a running BPMN process.
It holds all the BPMN elements from the BPMN model as well as the [BPMN process variables](basic-concepts-and-lessons.md#bpmn-process-variables)
and exists from the time when a deployed process plugin gets started until the time it stops running.
You have access to this representation in your Java code when overriding certain methods in [Service](basic-concepts-and-lessons.md#service-delegates) / [Message](basic-concepts-and-lessons.md#message-delegates) Delegates
like `doExecute` or `getAdditionalInputParameters`.

### BPMN Process Variables

BPMN process variables hold all information which isn't a BPMN element. For example, if you added an [Exclusive Gateway](basic-concepts-and-lessons.md#exclusive-gateways)
to your model, this would be part of the [BPMN process execution](basic-concepts-and-lessons.md#bpmn-process-execution). The [conditions](basic-concepts-and-lessons.md#conditions)
you added to the outgoing Sequence Flows will refer to some variables for boolean operation. These variables are part of
the BPMN process variables. BPMN process variables are stored key-value pairs with the key being the variable name.

You can learn how to access to the BPMN process variables [here](basic-concepts-and-lessons.md#accessing-bpmn-process-variables).

### Service Delegates

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

In order for messages to be able to be sent back and forth between organizations with potentially multiple of the 
same process plugin instances running at the same time and still arriving at the correct process instance,
we need some mechanism to map messages to their rightful process instance. 
This mechanism is called Message Correlation and requires attaching a unique identifier to every process instance.
This identifier is called the `business-key`. The `business-key` will get attached to every outgoing message automatically.  
  
It's possible that the ´business-key´ is insufficient to map messages to the correct process instance. This happens
when you use subprocesses in your BPMN model which all expect messages to be sent to them, not the parent process.
To solve this issue, [Task](basic-concepts-and-lessons.md#task) resources also come with an Input Parameter called `correlation-key`.
This is a secondary identifier you can attach to all messages if you need them to arrive at a specific subprocess.

## Lessons

### Accessing BPMN Process Variables

After creating a [Service Delegate](basic-concepts-and-lessons.md#service-delegates) or [Message Delegate](basic-concepts-and-lessons.md#message-delegates), you might want to
retrieve data from or store data in the [BPMN process variables](basic-concepts-and-lessons.md#bpmn-process-variables).
You can achieve this either through the [BPMN process execution](basic-concepts-and-lessons.md#bpmn-process-execution) or via the `Variables` class.  
*It's very much recommended you use the latter method*. The `Variables` class provides lots of utility methods to read or write certain types
of [BPMN process variables](basic-concepts-and-lessons.md#bpmn-process-variables). If for some reason you need to fall back on the [BPMN process execution](basic-concepts-and-lessons.md#bpmn-process-execution)
to solve your problem, we would like to learn how the current API of the `Variables` class is limiting you. Contact us, and we might turn it into a feature request ([Contribute](https://dsf.dev/stable/contribute)).

### Accessing Task Resources During Execution

If you want access to the [Task](basic-concepts-and-lessons.md#task) resources in your [Service](basic-concepts-and-lessons.md#service-delegates) / [Message](basic-concepts-and-lessons.md#message-delegates) Delegates, the `Variables` class will
also provide methods which return certain kinds of [Task](basic-concepts-and-lessons.md#task) resources. The most commonly used ones are
the start [Task](basic-concepts-and-lessons.md#task), referring to the [Task](basic-concepts-and-lessons.md#task) / Message Start Event responsible for starting the process,
and the latest [Task](basic-concepts-and-lessons.md#task), referring to most recently received [Task](basic-concepts-and-lessons.md#task).  
If you want to go one level deeper and extract certain information from a particular [Task](basic-concepts-and-lessons.md#task) resource,
you may use the `ProcessPluginApi`'s `TaskHelper` in conjunction with the method we just described. The `TaskHelper` class
offers methods to retrieve specific information from a [Task](basic-concepts-and-lessons.md#task) resource you provide as a parameter.  
The most common use-case for this is retrieving data from a [Task's](basic-concepts-and-lessons.md#task) Input Parameter or creating a new Input Parameter
for a [Message Delegate's](basic-concepts-and-lessons.md#message-delegates) `getAdditionalInputParameters` method.  
When retrieving data from a [Task's](basic-concepts-and-lessons.md#task) Input Parameter you first have to get to the Input Parameter you are looking to extract
data from. You can use one of the `TaskHelper's` getters for Input Parameters to find the right one. The method will try to match
the provided [CodeSystem](basic-concepts-and-lessons.md#codesystem) and Code to any Input Parameter of the provided [Task](basic-concepts-and-lessons.md#task) resource.
Depending on the method you chose you will for example receive all matches or just the first one.  
To create new Input Parameters to attach to a [Task](basic-concepts-and-lessons.md#task) resource, you may invoke the `TaskHelper.createInput` method. This
is most often used when overriding the `getAdditionalInputParamters` method of you [Message Delegate](basic-concepts-and-lessons.md#message-delegates).


### Setting Targets For Message Events

Setting a target for a message event requires a `Target` object. To create one, you require a target's organization identifier, endpoint identifier and endpoint address.
There are two ways of adding `targets` to the BPMN execution variables:
#### 1. Adding the target in the message event implementation
In your message event implementation (the class extending `AbstractTaskMessageSend`), you can override `AbstractTaskMessageSend#doExecute`,
add your targets and then call the super-method.
#### 2. Adding the target in a service  task right before the message event
This is the preferred method of this tutorial but both methods will work perfectly fine. For our use-cases, we usually prefer this one
since there is enough complexity to warrant putting it into a separate BPMN [Service Task](https://docs.camunda.org/manual/7.17/reference/bpmn20/tasks/service-task/).

In both cases you can access methods to create and set `targets` through the `variables` instance.

### Managing Multiple Incoming Messages And Missing Messages
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
