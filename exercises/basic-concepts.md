# Basic Concepts

This is a centralized write-up of all concepts you will need to solve the tutorial exercises.
Exercises will tell you which concepts you need to be familiar with to be able to solve them 
but may expand certain concepts to fit their specific problems.  
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

There are also cases 

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
the counterpart to [Message Intermediate Throwing Events](basic-concepts.md#message-intermediate-throwing-event). 
Use them whenever you expect to receive a message from another process or organization during 

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
In the [Camunda Modeler](https://camunda.com/de/download/modeler/), you can add conditions to Sequence Flow by selecting
a Sequence Flow and opening the `Condition` tab. You can find more information on how to
use Conditions [here](basic-concepts.md#conditions).

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
are able to add decision logic to Conditions. The Camunda Modeler refers to them as `Type`. You can find them in the ``Condition`` tab of 
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
For example, if your particular data exchange required additional information, you would add a slice to your Task profile in the same
way the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml)
added slices to the original FHIR Task resource. Notice that this also requires creating a [CodeSystem](basic-concepts.md#codesystem) and
including it in a [ValueSet](basic-concepts.md#valueset) to be able to use it in the Task resource.

### CodeSystem

Plugin development for the DSF requires the use of [CodeSystems](https://www.hl7.org/fhir/R4/codesystem.html) in two major ways:
1. Using existing DSF [CodeSystems](https://github.com/datasharingframework/dsf/tree/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem) in other FHIR resources like the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/release/1.4.0/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml).
2. Creating your own CodeSystem to add additional Input Parameters to your [Task](https://www.hl7.org/fhir/R4/task.html) profiles.

### ValueSet

[ValueSets](https://www.hl7.org/fhir/R4/valueset.html) are mostly needed to use the [Concepts](https://www.hl7.org/fhir/R4/codesystem-definitions.html#CodeSystem.concept) from [CodeSystems](basic-concepts.md#codesystem)
in your [Task](basic-concepts.md#task) profiles.

## DSF

This section will include all the DSF-specific functionality required to create basic process plugins.

### Tutorial Project
The tutorial project consists of three parts: A `test-data-generator` project used to generate X.509 certificates and FHIR resources
during the maven build of the project. The certificates and FHIR resources are needed to start DSF instances which simulate
installations at three different organizations. The DSF instances are configured using
a `docker-compose.yml` file in the `test-setup` folder. The docker-compose test setup uses a single PostgreSQL database server,
a single nginx reverse proxy as well as three separate DSF FHIR server instances and 3 separate DSF BPE server instances.
The `tutorial-process` project contains all resources (FHIR resources, BPMN models and Java code) for the actual
DSF process plugin.

Java code for the `tutorial-process` project is located at `src/main/java`, FHIR resources and
BPMN process models at `src/main/resources` as well as prepared JUnit tests to verify your solution at `src/test/java`.

### The Process Plugin Definition

In order for the DSF BPE server to load your plugin you need to provide it with the following information:
* A plugin version conforming to the pattern `\d+\.\d+\.\d+\.\d+`
* A release date
* A plugin name
* The BPMN model files
* The FHIR resources grouped by BPMN process ID. Your plugin may have any number of BPMN models. Each has their own BPMN process ID and FHIR resources specific to that BPMN process (think [Tsak](basic-concepts.md#task) resources needed for messages specific to that BPMN model)
* The Class holding your [Spring Configuration](basic-concepts.md#spring-integration)

You will provide this information by implementing the `dev.dsf.bpe.ProcessPluginDefinition` interface.
The DSF BPE server then searches for classes implementing this interface using the
Java [ServiceLoader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html) mechanism. Therefore, you will have to register your interface implementation in the `src/main/resources/META-INF/services/dev.dsf.bpe.ProcessPluginDefinition` file.
For this tutorial, the class implementing the ``ProcessPluginDefinition`` interface, `TutorialProcessPluginDefinition`,
has already been added to the file. You can use it as a reference for later when you want to create your own plugin.

### Spring Integration
Since the DSF also employs the use of the [Spring Framework](https://spring.io/projects/spring-framework) you will also
have to interact with it.
When deployed, every process plugin exists in its own [Spring context](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html). To make the process plugin work, you
have to provide Spring Beans with `prototype` scope for all classes which either extend or implement the following classes/interfaces (as of version 1.4.0): `AbstractTaskMessageSend`, `AbstractServiceDelegate`,
`DefaultUserTaskListener` and `ProcessPluginDeploymentStateListener`. A [Spring-Framework configuration class](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-java-basic-concepts)
located in `spring/config` is expected to provide the Spring Beans. For this plugin, the `TutorialConfig` class will take this role.