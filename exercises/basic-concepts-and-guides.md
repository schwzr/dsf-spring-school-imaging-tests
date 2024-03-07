# Basic Concepts and Guides

This is a centralized write-up of all concepts and guides you will need to solve the tutorial exercises.
Exercises will tell you which concepts or guides you need to be familiar with to be able to solve them 
but may also provide additional information concerning that concept to solve that specific exercise.  
Make sure you have read the [prerequisites](prerequisites.md).

***

## Disclaimer
The concept of `Tasks` exists in both the FHIR and BPMN domains. For this tutorial `Task resource` always refers
to [FHIR Tasks](https://www.hl7.org/fhir/R4/task.html) and `Service Task` always means the BPMN concept.

***

## BPMN Model

The DSF expects BPMN 2.0 for its process execution. This write-up covers the BPMN elements 
most commonly used in DSF process plugins.

***

### Sequence Flow
BPMN 2.0 calls the continuous arrows connecting the BPMN elements in BPMN models, Sequence Flow.
Sequence Flow exits one BPMN element and points at the next BPMN element to be processed.

***

### Service Tasks

You will primarily use [Service Tasks](https://docs.camunda.org/manual/7.20/reference/bpmn20/tasks/service-task/) 
when creating BPMN models. They are different from regular BPMN Tasks in that they offer the ability to 
link an implementation to the [Service Task](https://docs.camunda.org/manual/7.20/reference/bpmn20/tasks/service-task/)
which can be called and executed by a BPMN engine. The BPE (Business Process Engine) server of the DSF leverages
this engine to execute your BPMN processes.

***

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
the counterpart to [Message Intermediate Throwing Events](basic-concepts-and-guides.md#message-intermediate-throwing-event). 
Use them whenever you expect to receive a message from another process or organization during execution.

#### Message End Event
The [Message End Event](https://docs.camunda.org/manual/7.20/reference/bpmn20/events/message-events/#message-end-event) will 
stop the execution of a BPMN process and finish by sending a message.

***

### Gateways

[Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/) allow you to control the [Sequence Flow](basic-concepts-and-guides.md#sequence-flow). Different
types of gateways will be useful for different scenarios.

#### Exclusive Gateways

[Exclusive Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/)
allow you to decide which [Sequence Flow](basic-concepts-and-guides.md#sequence-flow) should be followed based on [conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions). 
[Conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions) are not part of the 
[Exclusive Gateways](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/) themselves. You set them 
through the sequence Flow Exiting the [Exclusive Gateway](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/exclusive-gateway/).  
In the [Camunda Modeler](https://camunda.com/download/modeler/), you can add conditions to [Sequence Flow](basic-concepts-and-guides.md#sequence-flow) by selecting
a [Sequence Flow](basic-concepts-and-guides.md#sequence-flow) and opening the `Condition` tab. You can find more information on how to
use Conditions [here](basic-concepts-and-guides.md#conditions).

#### Event-based Gateway

The [Event-based Gateway](https://docs.camunda.org/manual/7.20/reference/bpmn20/gateways/event-based-gateway/)
allows you model scenarios where you are expecting one out of a number of events to occur. 

***

### Timer Intermediate Catching Events

A [Timer Intermediate Catching Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event)
allows you model stopwatch behavior. A timer is started once the BPMN execution arrives at the event.
The duration until the timer runs out is specified using the [ISO 8601 Durations](http://en.wikipedia.org/wiki/ISO_8601#Durations) format. 
Examples can be found [here](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#time-duration). After running out, the BPMN process executes the [Sequence Flow](basic-concepts-and-guides.md#sequence-flow) following
the [Timer Intermediate Catching Event](https://docs.camunda.org/manual/7.17/reference/bpmn20/events/timer-events/#timer-intermediate-catching-event).

***

### Conditions

[Conditions](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/#conditions) 
allow you to change the behaviour of BPMN processes during execution. There are two ways you 
are able to add decision logic to Conditions. The [Camunda Modeler](https://camunda.com/download/modeler/) refers to them as `Type`. You can find them in the ``Condition`` tab of 
certain BPMN elements. The first one is `Script`. This allows you to add arbitrary complexity 
to your decisions logic and is rarely used for process plugins. The more common Type is `Expression`.
Expressions have the following syntax: `${expression}`. For this tutorial, _expression_ will
use a boolean condition like `var == true`. You can learn more advanced features of Expressions [here](https://docs.camunda.org/manual/7.20/user-guide/process-engine/expression-language/).
 For this to work during BPMN process execution, the variable you want to use for the boolean 
condition must be available in the BPMN process variables before [Sequence Flow](basic-concepts-and-guides.md#sequence-flow)
reaches the evaluation of the expression.

***

## FHIR

The DSF uses a variety of [FHIR resources](https://dsf.dev/intro/info/basics.html#why-are-we-using-fhir-and-bpmn). They use the XML format for the DSF.  
The most important ones for plugin development will be [ActivityDefinitions](http://hl7.org/fhir/R4/activitydefinition.html), [Tasks](https://www.hl7.org/fhir/R4/task.html), 
[CodeSystems](https://www.hl7.org/fhir/R4/codesystem.html) and [ValueSets](https://www.hl7.org/fhir/R4/valueset.html).
There is also a catalogue of DSF-specific FHIR resources including CodeSystems, ValueSets and Extensions. For now, you can find them in the official
DSF GitHub repository [here](https://github.com/datasharingframework/dsf/tree/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir).

***

### ActivityDefinition

[ActivityDefinitions](http://hl7.org/fhir/R4/activitydefinition.html) are used by the DSF to advertise which processes are
available at any given instance and who is allowed to request and who is allowed to execute a process. The DSF defined elements
for this purpose in the [dsf-activity-definition](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-activity-definition-1.0.0.xml) profile.


The most important elements in ActivityDefinitions are:
- `message-name`
- `task-profile`
- `requester`
- `recipient`

The `message-name` element contains the name of the [BPMN message start event](basic-concepts-and-guides.md#message-start-event) or
[BPMN message intermediate catching event](basic-concepts-and-guides.md#message-intermediate-catching-event) which expects 
a [Task](basic-concepts-and-guides.md#task) resource complying to the profile defined by `task-profile`.

The `requester` and `recipient` elements define the organisation(s) or person(s) who are allowed to request or receive the message
specified by `message-name`. The receiving DSF instance is the one who will execute the process connected to the message.

You will have to create your own [ActivityDefinitions](http://hl7.org/fhir/R4/activitydefinition.html) when developing a process plugin.
If you are fluent in reading XML FHIR definitions and translating them into XML resources, you can take a look at the 
DSF's profile for ActivityDefinitions [here](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-activity-definition-1.0.0.xml). 
ActivityDefinitions also reference other resource definitions. Depending on the resource, you will find them in one of [these folders](https://github.com/datasharingframework/dsf/tree/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir).  
If you are not as comfortable with these requirements you might want to check out the guide on [creating ActivityDefinitions](basic-concepts-and-guides.md#creating-an-activitydefinition).

You can also find examples for all possible `requester` and `recipient` elements [here](basic-concepts-and-guides.md#examples-for-requester-and-recipient-elements).

***

### Task

The [FHIR Task](https://www.hl7.org/fhir/R4/task.html) resource enables the DSF's distributed communication. 
Whenever a BPMN process instance communicates with a different process instance, the DSF will create a Task resource 
based on parameters you set in the BPMN model and during execution. It will then 
automatically send the Task resource to the recipient to start or continue whatever process the Task resource referred to.
All Task resources used in the DSF derive from the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml).
This profile includes a splicing for `Task.input` with three additional [Input Parameters](basic-concepts-and-guides.md#task-input-parameters):  
- `message-name`
- `business-key`
- `correlation-key`

When creating your own plugin, you will want to create your own profiles based on the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml).

#### Task Input Parameters

Task Input Parameters allow you to add additional information to [Task](basic-concepts-and-guides.md#task) resources.
For example, if your particular data exchange requires additional medical data, you would add a slice to your Task profile in the same
way the [dsf-task-base](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml) adds slices to the original [FHIR Task](https://www.hl7.org/fhir/R4/task.html) resource. Notice that this also requires creating a [CodeSystem](basic-concepts-and-guides.md#codesystem) and
including it in a [ValueSet](basic-concepts-and-guides.md#valueset) to be able to use it in the Task resource.

If these instructions are insufficient you can check out the guide on [how to add Task Input Parameters](basic-concepts-and-guides.md#adding-task-input-parameters-to-task-profiles).

***

### CodeSystem

[CodeSystems](https://www.hl7.org/fhir/R4/codesystem.html) usually represent a set of concepts which 
can be assigned to a code (think LOINC). If you want to use a Code in a resource, you will usually include them in a 
[ValueSet](basic-concepts-and-guides.md#valueset).

Plugin development for the DSF requires the use of [CodeSystems](https://www.hl7.org/fhir/R4/codesystem.html) in two major ways:
1. Using existing [DSF CodeSystems](https://github.com/datasharingframework/dsf/tree/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem) in other FHIR resources like the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml).
2. Creating your own CodeSystem to add additional [Input Parameters](basic-concepts-and-guides.md#task-input-parameters) to your [Task](https://www.hl7.org/fhir/R4/task.html) profiles.

***

### ValueSet

[ValueSets](https://www.hl7.org/fhir/R4/valueset.html) bind codes from [CodeSystems](basic-concepts-and-guides.md#codesystem) to coded elements like
`code`, `Coding` or `CodeableConcept`.

[ValueSets](https://www.hl7.org/fhir/R4/valueset.html) are mostly needed to use the [Concepts](https://www.hl7.org/fhir/R4/codesystem-definitions.html#CodeSystem.concept) from [CodeSystems](basic-concepts-and-guides.md#codesystem)
in your [Task](basic-concepts-and-guides.md#task) profiles.

***

## DSF

This section will include all the DSF-specific functionality required to create basic process plugins.

### Tutorial Project

The tutorial project consists of three parts: 
1. A `test-data-generator` project used to generate X.509 certificates and FHIR resources
during the maven build of the project. The certificates and FHIR resources are needed to start DSF instances which simulate
installations at three different organizations. 
2. The DSF instances are created using [Docker](https://www.docker.com/) and configured using
a `docker-compose.yml` file in the `dev-setup` folder. The docker-compose dev setup uses a single PostgreSQL database server,
a single nginx reverse proxy, a keycloak instance as well as three separate DSF FHIR server instances and 3 separate DSF BPE server instances.
3. The `tutorial-process` project contains all resources (FHIR resources, BPMN models and Java code) for the actual
DSF process plugin. Java code for the `tutorial-process` project is located at `src/main/java`, FHIR resources and
BPMN process models at `src/main/resources` as well as prepared JUnit tests to verify your solution at `src/test/java`.

FHIR resources used in the DSF come formatted as XML. You can find them in the `tutorial-process/src/main/resources/fhir` directory.
When creating your own FHIR resources for DSF process plugins you also want to put them in a fitting subdirectory of `tutorial-process/src/main/resources/fhir`.
You may also use JSON instead of XML.

***

### Certificates

There is a number of certificates that need to be generated in order for DSF instances to communicate with each other securely.
You can find a comprehensive lists of certificates needed by the [DSF FHIR](https://dsf.dev/stable/maintain/fhir/configuration.html)
and [DSF BPE](https://dsf.dev/stable/maintain/bpe/configuration.html) servers on the DSF website.  
Certificates will be created by the `test-data-generator` project through Maven by the time of the `package` phase in your process plugin build.
You can also invoke the generation of certificates separately by running the Maven build of `test-data-generator` until (and including) the `package` phase.   
Since this tutorial comes with three preconfigured DSF instances, the only time you will need to interact with certificates
is when you want to make requests to the DSF FHIR server. Either for access to the web frontend under https://instance-host-name/fhir/,
or when [starting your process plugin](basic-concepts-and-guides.md#starting-a-process-via-task-resources).  
In case of the web frontend, you will need to add the CA certificate and client certificate of the DSF instance you want to access to your browser.
Certificates can be found in `test-data-generator/cert`.  

**Example:**  
You want to access the `dic` DSF FHIR server. You add the CA certificate located in `test-data-generator/cert/ca` to your 
browser's certificate store. You also add the client certificate for `dic` located in `test-data-generator/cert/dic-client`
to your browser's client certificates.

**Important: Passwords for .p12 files are always "password"**

***

### The Process Plugin Definition

In order for the DSF BPE server to load your plugin you need to provide it with the following information:
* A plugin [version](basic-concepts-and-guides.md#about-versions)
* A release date
* A plugin name
* The BPMN model files
* The FHIR resources grouped by BPMN process ID. Your plugin may have any number of BPMN models. Each has their own BPMN process ID and FHIR resources specific to that BPMN process (think [Task](basic-concepts-and-guides.md#task) resources needed for messages specific to that BPMN model)
* The Class holding your [Spring Configuration](basic-concepts-and-guides.md#spring-integration)

You will provide this information by implementing the `dev.dsf.bpe.ProcessPluginDefinition` interface.
The DSF BPE server then searches for classes implementing this interface using the
Java [ServiceLoader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html) mechanism. Therefore, you will have to register your interface implementation in the `src/main/resources/META-INF/services/dev.dsf.bpe.ProcessPluginDefinition` file.
For this tutorial, the class implementing the `ProcessPluginDefinition` interface, `TutorialProcessPluginDefinition`,
has already been added to the file. You can use it as a reference for later when you want to create your own plugin.

***

### About Versions, Placeholders and URLs

#### Version Pattern

Process plugin versions have to obey the pattern: 
```
\d+\.\d+\.\d+\.\d+  Example: 1.0.1.2
```

The first two numbers (`1.0`) are used in FHIR resources and signal changes which break compatibility with previous
process versions. The latter two (`1.2`) signal changes which do not break compatibility with previous process versions. Specifically,
the 4th number is reserved for bug-fixes and the 3rd number includes all other non-breaking changes.

#### Placeholders

To avoid the need to specify the version and release date for each [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition), [CodeSystem](basic-concepts-and-guides.md#codesystem),
[Task](basic-concepts-and-guides.md#task) profile and [ValueSet](basic-concepts-and-guides.md#valueset) resource, 
the placeholders `#{version}` and `#{date}` can be used when creating FHIR resources or even in BPMN models. 
They are replaced with the values returned by the methods `ProcessPluginDefinition#getResourceVersion` 
and `ProcessPluginDefinition#getReleaseDate` respectively during deployment of a process plugin by the DSF BPE server.

#### URLs

BPMN models have an ID we call process definition key. The BPMN process definition key needs to be specified following the pattern:
```
^[-a-zA-Z0-9]+_[-a-zA-Z0-9]+$   Example: domainorg_processKey
```
In addition, the BPMN model needs to specify a version. You should be using the ``#{version}`` [placeholder](basic-concepts-and-guides.md#placeholders) 
for this as well. The DSF will use the process definition key and the version specified in the BPMN model to create a
URL to refer to this specific process. Like this:
```
http://domain.org/bpe/Process/processKey|1.0
```

As you can see, the version in the URL ``|1.0`` only uses the resource version and omits the code base version.
As mentioned in [Version Pattern](basic-concepts-and-guides.md#version-pattern), this means that only changes to the first two
version numbers are significant to signal compatibility when communicating with other process plugin instances.

You will use the above URL as your instantiatesCanonical value for [Task](basic-concepts-and-guides.md#task) profile definitions as well as references
to [Task](basic-concepts-and-guides.md#task) profiles in other resources. 
You will also use it as the URL value for your [ActivityDefinitions](basic-concepts-and-guides.md#activitydefinition). In this case though, you
have to split up the URL into two parts. You will separate the version (``|1.0``) from the URL and use it as a value for the
`ActivityDefinition.version` element. Since it refers to the plugin's resource version, you should also use the `#{version}`
[placeholder](basic-concepts-and-guides.md#placeholders) here instead. Going by the example from above, you will be left with a URL that looks
like this:
```
http://domain.org/bpe/Process/processKey
```
This will be the value for your `ActivityDefinition.url` element with `#{version}` as the value for your `ActivityDefinition.version` element.

***

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
For this tutorial, the `TutorialConfig` class will take this role.

***

### Read Access Tag

Axiomatically, nobody is allowed to write FHIR resources (except [Task](basic-concepts-and-guides.md#task)) to the DSF FHIR server 
unless it is your own organization. By default, the same applies to reading FHIR resources
(again except [Task](basic-concepts-and-guides.md#task)). But since the DSF is often used to offer medical data in form of
FHIR resources, you will find yourself wanting other organizations to be allowed to read the resources you are offering.
The `Resource.meta.tag` element is used define access rules for all FHIR resources in the DSF, with the
exception of [Task](basic-concepts-and-guides.md#task) resources. We will explain the reason for this exception shortly. 
For example, allowing read access for all organizations, you would use the following `system` and `code` in your FHIR resource:

```xml
<meta>
   <tag>
      <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
      <code value="ALL" />
   </tag>
</meta>
```
You can find all codes for the Read Access Tag in its [CodeSystem](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-read-access-tag-1.0.0.xml).

The read access rules for [Task](basic-concepts-and-guides.md#task) resources are defined through the `requester` and `recipient` elements of the  [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml) in your plugin's
[ActivityDefinitions](basic-concepts-and-guides.md#activitydefinition). Therefore, no `read-access-tag` is needed.

It is also possible to restrict read access of FHIR resources to organizations with 
a specific role in a parent organization or a specific identifier.
If you want to find out more, you may look at the [guide on configuring the Read Access Tag](basic-concepts-and-guides.md#configuring-the-read-access-tag).

***

### Examples for Requester and Recipient Elements

Below you will find a set of examples for each Coding used by `requester` and `recipient` elements from
the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml). CodeSystems referenced in the examples can be found [here](https://github.com/datasharingframework/dsf/tree/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem). 
Use this collection as a reference point when creating your own [ActivityDefinitions](basic-concepts-and-guides.md#activitydefinition).

#### Requester
The `requester` element uses one of the following Codings: 
```xml
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-all|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-all-practitioner|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-organization|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-organization-practitioner|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-parent-organization-role|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-parent-organization-role-practitioner|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-remote-all|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-remote-organization|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-remote-parent-organization-role|1.0.0" />
```

##### Local All
```xml
<extension url="requester">
    <valueCoding>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ALL" />
    </valueCoding>
</extension>
```

##### Local All Practitioner
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-practitioner">
            <valueCoding>
                <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                <code value="DSF_ADMIN"/>   <!-- example, replace appropriately -->
            </valueCoding>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ALL_PRACTITIONER" />
    </valueCoding>
</extension>
```

##### Local Organization
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization">
            <valueIdentifier>
                <system value="http://dsf.dev/sid/organization-identifier"/>
                <value value="My_Organization"/>    <!-- example, replace appropriately -->
            </valueIdentifier>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ORGANIZATION" />
    </valueCoding>
</extension>
```

##### Local Organization Practitioner
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
            <extension url="organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Organization"/>    <!-- example, replace appropriately -->
                </valueIdentifier>
            </extension>
            <extension url="practitioner-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                    <code value="DSF_ADMIN"/>   <!-- example, replace appropriately -->
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ORGANIZATION_PRACTITIONER" />
    </valueCoding>
</extension>
```

##### Local Parent Organization Role
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role">
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Parent_Organization"/>     <!-- example, replace appropriately -->
                </valueIdentifier>
            </extension>
            <extension url="organization-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/organization-role"/>
                    <code value="DIC"/>     <!-- example, replace appropriately -->
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ROLE" />
    </valueCoding>
</extension>
```

##### Local Parent Organization Role Practitioner
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role-practitioner">
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Parent_Organization"/>     <!-- example, replace appropriately -->
                </valueIdentifier>
            </extension>
            <extension url="organization-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/organization-role"/>
                    <code value="DIC"/>     <!-- example, replace appropriately -->
                </valueCoding>
            </extension>
            <extension url="practitioner-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                    <code value="DSF_ADMIN"/>       <!-- example, replace appropriately -->
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ROLE_PRACTITIONER" />
    </valueCoding>
</extension>
```

##### Remote All
```xml
<extension url="requester">
    <valueCoding>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="REMOTE_ALL" />
    </valueCoding>
</extension>
```

##### Remote Organization
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization">
            <valueIdentifier>
                <system value="http://dsf.dev/sid/organization-identifier"/>
                <value value="My_Organization"/>    <!-- example, replace appropriately -->
            </valueIdentifier>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="REMOTE_ORGANIZATION" />
    </valueCoding>
</extension>
```

##### Remote Parent Organization Role
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role">
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Parent_Organization"/>     <!-- example, replace appropriately -->
                </valueIdentifier>
            </extension>
            <extension url="organization-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/organization-role"/>
                    <code value="DIC"/>     <!-- example, replace appropriately -->
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="REMOTE_ROLE" />
    </valueCoding>
</extension>
```

#### Recipient
The `recipeint` element uses one of the following Codings:
```xml
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-all|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-organization|1.0.0" />
<profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-parent-organization-role|1.0.0" />
```

##### Local All
```xml
<extension url="recipient">
    <valueCoding>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ALL" />
    </valueCoding>
</extension>
```

##### Local Organization
```xml
<extension url="recipient">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization">
            <valueIdentifier>
                <system value="http://dsf.dev/sid/organization-identifier"/>
                <value value="My_Organization"/>    <!-- example, replace appropriately -->
            </valueIdentifier>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ORGANIZATION" />
    </valueCoding>
</extension>
```

##### Local Parent Organization Role
```xml
<extension url="recipient">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-parent-organization-role">
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Parent_Organization"/>     <!-- example, replace appropriately -->
                </valueIdentifier>
            </extension>
            <extension url="organization-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/organization-role"/>
                    <code value="DIC"/>     <!-- example, replace appropriately -->
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
        <code value="LOCAL_ROLE" />
    </valueCoding>
</extension>
```

***

### BPMN Process Execution

The BPMN process execution is the in-memory representation of a running BPMN process.
It holds all the BPMN elements from the BPMN model as well as the [BPMN process variables](basic-concepts-and-guides.md#bpmn-process-variables)
and exists from the time when a deployed process plugin gets started until the time it stops running.
You have access to this representation in your Java code when overriding certain methods in [Service](basic-concepts-and-guides.md#service-delegates) / [Message](basic-concepts-and-guides.md#message-delegates) Delegates
like `doExecute` or `getAdditionalInputParameters` through the `execution` parameter.

***

### BPMN Process Variables

BPMN process variables hold additional information which has to be available during BPMN process execution. 
Variables can be directly related to BPMN elements like the boolean value for [Conditions](basic-concepts-and-guides.md#conditions), but
do not have to be. BPMN process variables are stored as key-value pairs with the key being the variable name. 
They are accessible during the entirety of the execution to all [Service](basic-concepts-and-guides.md#service-delegates) / 
[Message](basic-concepts-and-guides.md#message-delegates) Delegates.

You can learn how to access to the BPMN process variables [here](basic-concepts-and-guides.md#accessing-bpmn-process-variables).

***

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

***

### Message Delegates

Message Delegates are the Java representation of the [Message Events](basic-concepts-and-guides.md#messaging) in your BPMN model.
You link a Message Delegate to a certain [Message Event](basic-concepts-and-guides.md#messaging) by selecting the Message Event
in the [Camunda Modeler](https://camunda.com/download/modeler/) and adding a Java class to the `Implementation` field.
Make sure you use the fully qualified class name. Like this:
```
org.package.myClass
```

You will only need Message Delegates for [Message Send Events](basic-concepts-and-guides.md#messaging). Incoming messages will 
be resolved to the correct [BPMN process execution](basic-concepts-and-guides.md#bpmn-process-execution) automatically using [Message Correlation](basic-concepts-and-guides.md#message-correlation)
and the message inputs will be added to that execution's [process variables](basic-concepts-and-guides.md#bpmn-process-variables).  

To make a Message Delegate for [Message Send Events](basic-concepts-and-guides.md#messaging), your Java class needs to extend
`AbstractTaskMessageSend`. Most of the time, you will not be adding any processing logic to your Message Delegates, therefore you usually won't be overwriting
the `doExecute` method like with [Service Delegates](basic-concepts-and-guides.md#service-delegates). Instead, you most likely want to 
aggregate the information you processed in earlier steps and attach it to a message. For this you need to overwrite the 
`getAdditionalInputParamters` method. The DSF translates BPMN messages 
into FHIR [Task](basic-concepts-and-guides.md#task) resources to execute the communication modeled by your BPMN diagrams. The information
you are sending to another BPMN process is specified in the Task.input elements a.k.a. [Input Parameters](basic-concepts-and-guides.md#task-input-parameters),
hence the name of the method.

***

### Environment Variables

Environment variables offer a way to make configuration data available at the start of a [BPMN process execution](basic-concepts-and-guides.md#bpmn-process-execution).
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

***

### DSF Process API Package

The [DSF Process API package](https://mvnrepository.com/artifact/dev.dsf/dsf-bpe-process-api-v1) consists of a set of utility classes designed to provide easy access to solutions for process plugin use cases.
This includes for example the `Variables` class, which provides access to the [BPMN process variables](basic-concepts-and-guides.md#bpmn-process-variables).

#### Process Plugin Api
When creating [Service Delegates](basic-concepts-and-guides.md#service-delegates) or [MessageDelegates](basic-concepts-and-guides.md#message-delegates) you will 
notice that you need to provide a constructor which expects a `ProcessPluginApi` object and forward it to the superclasses' constructor.
This API instance provides a variety of utility classes: 
- `ProxyConfig`**:** forward proxy configuration
- `EndpointProvider`**:** access to Endpoint resources
- `FhirContext`**:** HAPI FHIR Context for parsing/serializing
- `FhirWebserviceClientProvider`**:** Webservice client to access DSF FHIR server
- `MailService`**:** for sending automatic E-Mails (if configured)
- `OrganizationProvider`**:** access to Organization resources
- `Variables`**:** access to BPMN execution variables

***

### Message Correlation

In order for messages to be able to be sent back and forth between organizations with potentially multiple of the 
same process plugin instances running at the same time and still arriving at the correct process instance,
we need some mechanism to map messages to their rightful process instance. 
This mechanism is called Message Correlation and requires attaching a unique identifier to every process instance.
This identifier is called the `business-key`. The `business-key` will get attached to every outgoing message automatically.  
  
It is possible that the `business-key` is insufficient to map messages to the correct process instance. This happens
when you use subprocesses in your BPMN model which all expect messages to be sent to them, not the parent process.
To solve this issue, [Task](basic-concepts-and-guides.md#task) resources also come with an [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) called `correlation-key`.
This is a secondary identifier you can attach to all messages if you need them to arrive at a specific subprocess.
You can learn more about how `correlation-keys` are used by studying the [Ping-Pong Process](https://github.com/datasharingframework/dsf-process-ping-pong).

***

### Draft Task Resources

[Task](basic-concepts-and-guides.md#task) resources with status `draft` are used to create the DSF FHIR server's functionality
of starting processes via its web interface. They are stored in `.../tutorial-process/src/main/resources/fhir/Task`.
Compared to regular [Task](basic-concepts-and-guides.md#task) resources used to
start BPMN processes, this type of [Task](basic-concepts-and-guides.md#task) resource requires the status `draft` instead the usual `requested`.
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
`processKey` should be the same one used in [URLs](basic-concepts-and-guides.md#urls).  
`task-name` can be any String you wish to identify this task with. E.g. you can use the file name of the Draft Task. 

For a complete example you can take a look at the Draft Task Resource in one of the solution branches
and compare it to the one needed for cURL. The [Task](basic-concepts-and-guides.md#task) resource created
for cURL can be found at `.../tutorial-process/src/main/resources/example-task.xml`.  

You might also want to check out [this guide](basic-concepts-and-guides.md#creating-task-resources-based-on-a-definition) 
if you do not know how to create [Task](basic-concepts-and-guides.md#task) resources in general.

***

## Guides

***

### Accessing BPMN Process Variables

After creating a [Service Delegate](basic-concepts-and-guides.md#service-delegates) or [Message Delegate](basic-concepts-and-guides.md#message-delegates), you might want to
retrieve data from or store data in the [BPMN process variables](basic-concepts-and-guides.md#bpmn-process-variables).
You can achieve this either through the [BPMN process execution](basic-concepts-and-guides.md#bpmn-process-execution) or via the `Variables` class.  
*It is very much recommended you use the latter method*.   
The `Variables` class provides lots of utility methods to read or write certain types
of [BPMN process variables](basic-concepts-and-guides.md#bpmn-process-variables). If for some reason you need to fall back on the [BPMN process execution](basic-concepts-and-guides.md#bpmn-process-execution)
to solve your problem, we would like to learn how the current API of the `Variables` class is limiting you. Contact us, and we might turn it into a feature request ([Contribute](https://dsf.dev/stable/contribute)).

***

### Accessing Task Resources During Execution

If you want access to the [Task](basic-concepts-and-guides.md#task) resources in your [Service](basic-concepts-and-guides.md#service-delegates) / [Message](basic-concepts-and-guides.md#message-delegates) Delegates, the `Variables` class will 
provide methods which return certain kinds of [Task](basic-concepts-and-guides.md#task) resources. The most commonly used ones are
the start [Task](basic-concepts-and-guides.md#task), referring to the [Task](basic-concepts-and-guides.md#task) / [Message Start Event](basic-concepts-and-guides.md#message-start-event) responsible for starting the process,
and the latest [Task](basic-concepts-and-guides.md#task), referring to most recently received [Task](basic-concepts-and-guides.md#task) / Message.  
In principle, this is sufficient to access all information in a [Task](basic-concepts-and-guides.md#task) resource, since you have 
the [Task](basic-concepts-and-guides.md#task) resource's Java object, but very cumbersome.
Instead of navigating the [Task](basic-concepts-and-guides.md#task) resource's element tree,
you should first try to use the [ProcessPluginApi's](basic-concepts-and-guides.md#process-plugin-api) `TaskHelper` in conjunction with the method above. The `TaskHelper` class
offers specific methods related to [Task](basic-concepts-and-guides.md#task) resources.  
The most common use case for this is retrieving data from a [Task's](basic-concepts-and-guides.md#task) [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) or creating a new [Input Parameter](basic-concepts-and-guides.md#task-input-parameters)
for a [Message Delegate's](basic-concepts-and-guides.md#message-delegates) `getAdditionalInputParameters` method.  
When retrieving data from a [Task's](basic-concepts-and-guides.md#task) Input Parameter you first have to get to the [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) you are looking to extract
data from. You can use one of the `TaskHelper's` getters for [Input Parameters](basic-concepts-and-guides.md#task-input-parameters) to find the right one. The methods will try to match
the provided [CodeSystem](basic-concepts-and-guides.md#codesystem) and Code to any [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) of the provided [Task](basic-concepts-and-guides.md#task) resource.
Depending on the method you chose you will for example receive all matches or just the first one.  
To create new [Input Parameters](basic-concepts-and-guides.md#task-input-parameters) to attach to a [Task](basic-concepts-and-guides.md#task) resource, you may invoke the `TaskHelper#createInput` method. This
is most often used when overriding the `getAdditionalInputParamters` method of you [Message Delegate](basic-concepts-and-guides.md#message-delegates).

***

### Setting Targets for Message Events

Setting a target for a message event requires a `Target` object. To create one, you require a target's organization identifier, endpoint identifier and endpoint address.
You can find these values by visiting the DSF FHIR server's web interface. In the top right corner, click
the `Show Bookmarks` button, then select `Endpoint`. You will be taken to a list of all Endpoints available to the FHIR server.  
There are two ways of adding `targets` to the BPMN execution variables:
#### 1. Adding the target in the message event implementation
In your message event implementation (the class extending `AbstractTaskMessageSend`), you can override `AbstractTaskMessageSend#doExecute`,
add your targets and then call the super-method.
#### 2. Adding the target in a service  task right before the message event
This is the preferred method of this tutorial but both methods will work perfectly fine. For our use cases, we usually prefer this one
since there is enough complexity to warrant putting it into a separate BPMN [Service Task](https://docs.camunda.org/manual/7.17/reference/bpmn20/tasks/service-task/).

In both cases you can access methods to create and set `targets` through the `Variables` instance.

***

### Managing Multiple Incoming Messages and Missing Messages

If an already running process instance is waiting for a message from another organization, the corresponding FHIR [Task](basic-concepts-and-guides.md#task) may never arrive.
Either because the other organization decides to never send the message or because some technical problem prohibits the [Task](basic-concepts-and-guides.md#task) resource from being posted to the DSF FHIR server.
This would result in stale process instances that never finish.

At the same time, you might also expect to receive one out of a number of different message types at once.

In order to solve both problems we can add an [Event Based Gateway](basic-concepts-and-guides.md#event-based-gateway) to the process waiting 
for a response and then either handle a [Task](basic-concepts-and-guides.md#task) resource with the response and finish the process in a success 
state or trigger a [Timer Intermediate Catching Event](basic-concepts-and-guides.md#timer-intermediate-catching-events) after a defined wait period and finish the process in an error state. 
The following BPMN collaboration diagram shows how the process at the first organization would look like if we wanted to react to multiple different messages 
or missing messages:

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="figures/exercise5_event_based_gateway_inverted.svg">
  <source media="(prefers-color-scheme: light)" srcset="figures/exercise5_event_based_gateway.svg">
  <img alt="BPMN collaboration diagram with an Event Based Gateway" src="figures/exercise5_event_based_gateway.svg">
</picture>

***

### Starting a Process via Task Resources

To start a BPMN process, you need to create new a [Task](basic-concepts-and-guides.md#task) resource in the DSF FHIR server
by sending an HTTP request according to the [FHIR RESTful API](https://www.hl7.org/fhir/R4/http.html). Specifically, you need to [create](https://www.hl7.org/fhir/R4/http.html#create)
a resource for the first time. Also, remember that the [Task](basic-concepts-and-guides.md#task)
resource you are sending needs to comply to the [Task](basic-concepts-and-guides.md#task) profile of the process you 
want to start and the [ActivityDefinition's](basic-concepts-and-guides.md#activitydefinition) authorization rules.   
There are two major ways of making this HTTP request:
1. Using cURL
2. Using the DSF FHIR server's web interface

#### Using cURL
Using cURL probably isn't as "pretty",
but since cURL requires the actual [Task](basic-concepts-and-guides.md#task) payload as an XML, it will prove useful to
gain more insight in how actual [Task](basic-concepts-and-guides.md#task) resources look like and how they relate to
your [Task](basic-concepts-and-guides.md#task) profiles and [ActivityDefinitions](basic-concepts-and-guides.md#activitydefinition). You will have to create
an appropriate [Task](basic-concepts-and-guides.md#task) resource for this. 
There already is a file called `example-task.xml` located in `tutorial-process/src/main/resources/fhir`.
You can use this as your starting point. You can try to follow [this guide](basic-concepts-and-guides.md#creating-task-resources-based-on-a-definition),
or you can check the solution branches for this 
file if you need ideas on how to fill it out properly.

Below are some cURL command skeletons. Replace all <>-Placeholders with appropriate values. Host name depends on the
instance you want to address. For this tutorial this is either one of `dic`, `cos` or `hrp`. [Certificates](basic-concepts-and-guides.md#certificates) can be found in 
`test-data-generator/cert`. Client [certificates](basic-concepts-and-guides.md#certificates) and private keys can be found 
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

#### Using the DSF FHIR Server's Web Interface

When visiting the web interface of a DSF FHIR server instance (e.g. https://instance-name/fhir), you
can query the DSF FHIR server using the [FHIR RESTful API](https://www.hl7.org/fhir/R4/http.html) to return a list of all [Draft Task Resources](basic-concepts-and-guides.md#draft-task-resources).
These [Task](basic-concepts-and-guides.md#draft-task-resources) resources act like a template you can use to 
instantiate [Task](basic-concepts-and-guides.md#task) resources which start BPMN processes.  
Instead of querying the DSF FHIR server manually, you can use a predefined bookmark
to navigate to the query URL. You can find a list of Bookmarks in the top right corner of
the web interface. Simply select the bookmark referencing `?_sort=_profile,identifier&status=draft` under 
the `Task` section, and you will be taken to the list of all [Draft Task Resources](basic-concepts-and-guides.md#draft-task-resources).  
Once there, you can select the one which starts your BPMN process. It will take you to a detailed view
of the resource where you will also have the chance to fill any [Task Input Parameters](basic-concepts-and-guides.md#task-input-parameters)
you might need to specify.  
If everything is filled out correctly, you may start your process by clicking `Start Process`.  
Keep in mind that, for [Draft Task Resources](basic-concepts-and-guides.md#draft-task-resources) to be 
available, you need to include them in your mapping for your BPMN process ID in `ProcessPluginDefinition#getFhirResourcesByProcessId`. 
Take a look at [the Process Plugin Definition](basic-concepts-and-guides.md#the-process-plugin-definition) if you need a reminder.

***

### Creating Task Resources Based on a Definition

This short guide should help you understand how you can create [Task](basic-concepts-and-guides.md#task)
resources for use in [Starting A Process Via Task Resources](basic-concepts-and-guides.md#starting-a-process-via-task-resources).
We will employ the use of the free version of [Forge](https://simplifier.net/forge?utm_source=firely-forge) to help 
with visualization. You are invited to create a free account and follow along, but we will include screenshots of relevant
views either way. Remember that the free version of Forge [must not be used commercially](https://simplifier.net/pricing).  
As an example, we will create a [Task](basic-concepts-and-guides.md#task) resource from the `task-start-dic-process.xml` profile.

#### 1st Step: Removing Placeholders
`task-start-dic-process.xml` includes placeholders for the `version` and `date` elements. For the duration of this guide, 
you can either remove or comment these elements, so Forge does not try to perform type checking on them, which
would result in an error and Forge not loading the file.

#### 2nd Step: Differential Chain
If the resource profile is only available as a [differential](https://www.hl7.org/fhir/R4/profiling.html#snapshot), like in our
case, we will want to aggregate the changes made to the base resource (in this case [Task](basic-concepts-and-guides.md#task)) by all profiles to make
it more readable.
To do this, we first need all the profiles involved. We already have `task-start-dic-process.xml` in our `StructureDefinition` folder.
It lists a resource called `task-base` in its `baseDefinition` element. This resource is part of the DSF and can be
found [here](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml).
Put it into the `StructureDefinition` folder. Since `task-base` has the original FHIR Task as its `baseDefinition`
element, we are done with this chain.
In forge, you should now be able to open the `StructureDefinition` folder and select the `task-start-dic-process.xml` profile.
It should look something like this:  

![Forge overview](figures/forge_overview.png)

#### 3rd Step: Building the Task Resource
We will now go through each element one by one and include it into our [Task](basic-concepts-and-guides.md#task)
resource, provided it is mandatory (cardinality at least `1..1`) according to the profile. It is important 
that you not use any placeholders like `#{version}` for resources not read by the DSF BPE server. This is the case
if we want a [Task](basic-concepts-and-guides.md#task) resource for use with [cURL](basic-concepts-and-guides.md#using-curl).
But, placeholders should be used in [Draft Task Resources](basic-concepts-and-guides.md#draft-task-resources) instead of actual values wherever possible, 
since those are read by the DSF BPE server. This guide will create a [Task](basic-concepts-and-guides.md#task) resource without placeholders.  
We will start out with the base element for all [Task](basic-concepts-and-guides.md#task) resources:  
```xml
<Task xmlns="http://hl7.org/fhir">

</Task>
```

Before we start adding any elements listed in Forge's element tree, we have to include the `Task.meta.profile` element.
Its requirement cannot be seen here which is why we mention it specifically. This is the only instance you will not see
it in the element tree. It should look like this:  
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-start-dic-process|1.0"/>
    </meta>
</Task>
```

The first element which can be found in the element tree is the `instantiatesCanonical` element. To add it, we
will create an XML element with the same name and the value according to [URLs](basic-concepts-and-guides.md#urls): 
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-start-dic-process|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/dicProcess|1.0" />
</Task>
```
We can continue this process for all primitive elements like these. Just make sure you pay attention to use the correct
data type (e.g. proper coding value for elements with `coding` type).

By now your [Task](basic-concepts-and-guides.md#task) resources should look something like this:
<details>
<summary>Suggested solution</summary>

```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-start-dic-process|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/dicProcess|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
</Task>
```
</details>

Let us look at a more complex element like the `requester` element:  

![Forge requester view](figures/forge_requester_view.png)

We will start the same way we started with primitive elements, by adding the `requester` element:  
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-start-dic-process|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/dicProcess|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
    <requester>
     
    </requester>
</Task>
```

Then, we will add primitive elements to `requester` like we did before for `Task`:  
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-start-dic-process|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/dicProcess|1.0" />
    <status value="requested"/>
    <intent value="order"/>
    <authoredOn value="2024-02-08T10:00:00+00:00" />
    <requester>
        <type value="Organization"/>
    </requester>
</Task>
```
*Important to note here that the value for the `status` will always be `requested` for Tasks being posted using cURL and the `type` element for `requester` and `recipient` will always have the value `Organization` in the DSF context.*

Next, we will add the `identifier` element and its primitive sub-elements just like we started out doing it for the `requester` element:  
```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-start-dic-process|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/dicProcess|1.0" />
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

You should now be able to fill out all elements in your [Task](basic-concepts-and-guides.md#task) resource until you reach
the [slicing](https://www.hl7.org/fhir/R4/profiling.html#slicing) for `Task.input`. Your [Task](basic-concepts-and-guides.md#task)
resource should look something like this:  
<details>
<summary>Suggested solution</summary>

```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-start-dic-process|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/dicProcess|1.0" />
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


[Slicings](https://www.hl7.org/fhir/R4/profiling.html#slicing) are a bit different from regular elements. Let us look at the 
slice `message-name`:  

![Forge slice message name](figures/forge_slice_message_name.png)

If we were to continue including slices to the [Task](basic-concepts-and-guides.md#task) resource like we did so far,
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

This approach however, would not work. FHIR processors do not use the name of the slice to map entries in
your [Task](basic-concepts-and-guides.md#task) resource to the correct slice. They use [discriminators](https://www.hl7.org/fhir/R4/profiling.html#discriminator).
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
        <valueString value="dicProcess" />
    </input>
</Task>
```

Now you should be able to add all remaining mandatory elements to your [Task](basic-concepts-and-guides.md#task) 
resource on your own. In the end, it should look something like this:  
<details>
<summary>Suggested solution</summary>

```xml
<Task xmlns="http://hl7.org/fhir">
    <meta>
        <profile value="http://dsf.dev/fhir/StructureDefinition/task-start-dic-process|1.0"/>
    </meta>
    <instantiatesCanonical value="http://dsf.dev/bpe/Process/dicProcess|1.0" />
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
        <valueString value="dicProcess"/>
    </input>
</Task>
```
</details>

**Do not forget to restore the version and date placeholders in `task-start-dic-process.xml`!**

***

### Adding Task Input Parameters to Task Profiles

When adding a new [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) to a [Task](basic-concepts-and-guides.md#task)
profile, you are essentially adding a new slice to `Task.input`. [Slicing](https://www.hl7.org/fhir/R4/profiling.html#slicing) is part
of [profiling](https://www.hl7.org/fhir/R4/profiling.html) in FHIR. Profiling lets you create your own
FHIR definitions based on pre-existing FHIR definitions. A slicing defines constraints on element lists
like `Task.input` e.g. by only allowing the elements to be of certain types. For example, you 
might have a list of fruits in a `FruitBasket` resource. Constraining that list to only include
fruits of type `Apple`, `Banana` and `Orange` would be considered [slicing](https://www.hl7.org/fhir/R4/profiling.html#slicing).  
This guide will not cover how slicing works in general, only for the case presented by the DSF FHIR resource
context. Our goal will be to add a new [Input Parameter](basic-concepts-and-guides.md#task-input-parameters)
of type `example-input` to the `task-start-dic-process.xml` profile which will be used to submit `integer` values to our `dicProcess`.

Let us start out by adding a slice to `task-start-dic-process.xml`. Since there is already a slicing defined
on `Task.input` by `task-start-dic-process.xml`'s `baseDefinition`, we have to check out this resource first. 
As a part of the [differential](https://www.hl7.org/fhir/R4/profiling.html#snapshot) statement, slicing also uses [Element Definitions](https://www.hl7.org/fhir/R4/elementdefinition.html). 
The slicing for `Task.input` is defined in this part of the `baseDefinition`:  
```xml
<element id="Task.input">
    <extension url="http://hl7.org/fhir/StructureDefinition/structuredefinition-explicit-type-name">
        <valueString value="Parameter" />
    </extension>
    <path value="Task.input" />
    <slicing>
        <discriminator>
            <type value="value" />
            <path value="type.coding.system" />
        </discriminator>
        <discriminator>
            <type value="value" />
            <path value="type.coding.code" />
        </discriminator>
        <rules value="openAtEnd" />
    </slicing>
    <min value="1" />
</element>
```
*The resource can be found [here](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-task-base-1.0.0.xml)*

We will only need to take a look at the `discrimitator` tag for now.
Discriminators define the elements a FHIR processor needs to distinguish slices by. In our case, a processor
would look at the values for `type.coding.system` and `type.coding.code` to determine which
slice this element belongs to. The discriminator type `value` implies that `type.coding.system` and `type.coding.code`
have to be present in all slices and need to have a fixed value. 
You can learn more about discriminators [here](https://www.hl7.org/fhir/R4/profiling.html#discriminator).

Let us revisit `task-start-dic-process.xml` and start adding a slice called `example-input` to it:  
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        ...
        <element id="Task.input:example-input">
            <path value="Task.input" />
            <sliceName value="example-input" />
            <min value="1" />
            <max value="1" />
        </element>
    </differential>
</StructureDefinition>
```
*Unnecessary elements for this guide are hidden by ... placeholders.*

We have now defined a slice on `Task.input` with the name and id of `example-input` and cardinality of `1..1`. You might
want a different cardinality for your use case. We recommend you also take a look at the documentation for [ElementDefinition.id](https://www.hl7.org/fhir/R4/elementdefinition.html#id)
and [ElementDefinition.path](https://www.hl7.org/fhir/R4/elementdefinition.html#path). They explain how to create the proper
values for these elements. Cardinality is also part of the [element definition](https://www.hl7.org/fhir/R4/elementdefinition.html)
hierarchy (see [ElementDefinition.min](https://www.hl7.org/fhir/R4/elementdefinition-definitions.html#ElementDefinition.min) and [ElementDefinition.max](https://www.hl7.org/fhir/R4/elementdefinition-definitions.html#ElementDefinition.max)).

Next up, we need to define the binding for `Task.input:example-input.type`. Because `Task.input.type`
is a `CodeableConcept` which uses codings from a [ValueSet](basic-concepts-and-guides.md#valueset),
the [discriminator](https://www.hl7.org/fhir/R4/profiling.html#discriminator) requires us to use `required` as the binding strength:  
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        ...
        <element id="Task.input:example-input">
            <path value="Task.input" />
            <sliceName value="example-input" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Task.input:example-input.type">
            <path value="Task.input.type" />
            <binding>
                <strength value="required"/>
                <valueSet value="http://dsf.dev/fhir/ValueSet/example" />
            </binding>
        </element>
    </differential>
</StructureDefinition>
```
As you can see, we referenced a [ValueSet](basic-concepts-and-guides.md#valueset) in this binding. 
When adding an actual slice for your use case, you will have to reference an existing [ValueSet](basic-concepts-and-guides.md#valueset) resource or create a new 
one. A guide on how to create them can be found [here](basic-concepts-and-guides.md#creating-valuesets-for-dsf-processes).

Since the [discriminator](https://www.hl7.org/fhir/R4/profiling.html#discriminator) requires 
`Task.input.coding.code` and `Task.input.coding.system` to be present, we will make `Task.input.coding` mandatory as well:  
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        ...
        <element id="Task.input:example-input">
            <path value="Task.input" />
            <sliceName value="example-input" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Task.input:example-input.type">
            <path value="Task.input.type" />
            <binding>
                <strength value="required"/>
                <valueSet value="http://dsf.dev/fhir/ValueSet/example" />
            </binding>
        </element>
        <element id="Task.input:example-input.type.coding">
            <path value="Task.input.type.coding"/>
            <min value="1" />
        </element>
    </differential>
</StructureDefinition>
```

In the beginning we mentioned how `Task.input.type.coding.system` and `Task.input.type.coding.code`
have to use fixed values. Here is how we accomplish this: 

```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        ...
        <element id="Task.input:example-input">
            <path value="Task.input" />
            <sliceName value="example-input" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Task.input:example-input.type">
            <path value="Task.input.type" />
            <binding>
                <strength value="required"/>
                <valueSet value="http://dsf.dev/fhir/ValueSet/example" />
            </binding>
        </element>
        <element id="Task.input:example-input.type.coding">
            <path value="Task.input.type.coding"/>
            <min value="1" />
        </element>
        <element id="Task.input:example-input.type.coding.system">
            <path value="Task.input.type.coding.system"/>
            <min value="1"/>
            <fixedUri value="http://dsf.dev/fhir/CodeSystem/example"/>
        </element>
        <element id="Task.input:example-input.type.coding.code">
            <path value="Task.input.type.coding.code"/>
            <min value="1"/>
            <fixedCode value="example-input" />
        </element>
    </differential>
</StructureDefinition>
```
*Notice that we also made the two elements mandatory because they are required by the discriminator.*

For the `type.coding.system` element we referenced a [CodeSystem](basic-concepts-and-guides.md#codesystem). 
The `type.coding.code` element uses a code from this [CodeSystem](basic-concepts-and-guides.md#codesystem) called `example-input`.
This is the mechanism by which you actually "name" your [Input Parameter](basic-concepts-and-guides.md#task-input-parameters). The
`type.coding.code` value will identify your [Input Parameter](basic-concepts-and-guides.md#task-input-parameters) when you use
it in an actual [Task](basic-concepts-and-guides.md#task) resource. Here is how this would look like: 

```xml
<Task xmlns="http://hl7.org/fhir">
    ...
    <input>
        <type>
            <coding>
                <system value="http://dsf.dev/fhir/CodeSystem/example"/>
                <code value="example-input" />
            </coding>
        </type>
     ...
    </input>
</Task>
```

When adding an actual slice for your use case, you will also need to reference an existing [CodeSystem](basic-concepts-and-guides.md#codesystem) resource or create a new one to reference.
A guide on how to create them can be found [here](basic-concepts-and-guides.md#creating-codesystems-for-the-dsf-processes).

`Task.input.value[x]` is the actual value you will submit using your Input Parameter. You can make it
any of [these](https://www.hl7.org/fhir/R4/datatypes.html#open) data types. This is because `Type.input.value[x]`  
refers to `*` instead of any particular type in its [definition](https://www.hl7.org/fhir/R4/task-definitions.html#Task.input.value_x_). Let us define it as an `integer` type`:

```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        ...
        <element id="Task.input:example-input">
            <path value="Task.input" />
            <sliceName value="example-input" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Task.input:example-input.type">
            <path value="Task.input.type" />
            <binding>
                <strength value="required"/>
                <valueSet value="http://dsf.dev/fhir/ValueSet/example" />
            </binding>
        </element>
        <element id="Task.input:example-input.type.coding">
            <path value="Task.input.type.coding"/>
            <min value="1" />
        </element>
        <element id="Task.input:example-input.type.coding.system">
            <path value="Task.input.type.coding.system"/>
            <min value="1"/>
            <fixedUri value="http://dsf.dev/fhir/CodeSystem/example"/>
        </element>
        <element id="Task.input:example-input.type.coding.code">
            <path value="Task.input.type.coding.code"/>
            <min value="1"/>
            <fixedCode value="example-input" />
        </element>
        <element id="Task.input:example-input.value[x]">
            <path value="Task.input.value[x]"/>
            <type>
                <code value="integer"/>
            </type>
        </element>
    </differential>
</StructureDefinition>
```

Now we have a new Input Parameter of type `example-input` which accepts any `integer` as its value.

***

### Creating ValueSets for DSF Processes

You might find yourself in the situation where you need to create a [ValueSet](basic-concepts-and-guides.md#valueset).
For example, when adding [Input Parameters](basic-concepts-and-guides.md#task-input-parameters) to DSF [Task](basic-concepts-and-guides.md#task)
resources, you will also have to reference a [ValueSet](basic-concepts-and-guides.md#valueset) resource in your 
binding for `Task.input.type` to be able to set the type of your [Input Parameter](basic-concepts-and-guides.md#task-input-parameters).
[ValueSets](basic-concepts-and-guides.md#valueset) for the DSF differ from regular [ValueSets](basic-concepts-and-guides.md#valueset)
in that some element's values are managed by the DSF BPE server. You can use the following template for your
[ValueSet](basic-concepts-and-guides.md#valueset):  
```xml
<ValueSet xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag> 
    </meta>
    <url value="http://dsf.dev/fhir/ValueSet/my-value-set"/>    <!--dummy value-->
    <!-- version managed by bpe -->
    <version value="#{version}" />
    <name value="My ValueSet"/>     <!--dummy value-->
    <title value="My ValueSet Title"/>  <!--dummy value-->
    <!-- status managed by bpe -->
    <status value="unknown" />
    <experimental value="false"/>
    <!-- date managed by bpe -->
    <date value="#{date}"/>
    <publisher value="DSF"/>    <!--dummy value-->
    <description value="ValueSet with all codes from my-code-system"/>      <!--dummy value-->
    <immutable value="true"/>
    <compose>
        <include>
            <system value="http://dsf.dev/fhir/CodeSystem/my-code-system"/>     <!--dummy value-->
            <version value="#{version}"/>   
        </include>  
    </compose>
</ValueSet> 
```
Replace dummy values with appropriate values of your own. Do not change elements managed by the DSF BPE server.  
The `compose` element defines the codes included in this [ValueSet](basic-concepts-and-guides.md#valueset).
It holds at least one `include` element. Each `include` element refers to a [CodeSystem](basic-concepts-and-guides.md#codesystem)
and contains a list of `concept` elements which in turn contain the actual `code` element.
Using one code from `my-code-system` and one code from `my-other-code-system` would result in the following `compose` element:  
```xml
<ValueSet xmlns="http://hl7.org/fhir">
    ...
    <compose>
        <include>
            <system value="http://dsf.dev/fhir/CodeSystem/my-code-system"/>
            <version value="#{version}"/>   
            <concept>
                <code value="my-code"/>
            </concept>
        </include>  
        <include>
            <system value="http://dsf.dev/fhir/CodeSystem/my-other-code-system"/>
            <version value="#{version}"/>
            <concept>
                <code value="my-other-code"/>
            </concept>
        </include>
    </compose>
</ValueSet>
```
The DSF BPE server will read your [ValueSet](basic-concepts-and-guides.md#valueset) from
`tutorial-process/src/main/resources/fhir/ValueSet`.

You might also want to check out [this guide](basic-concepts-and-guides.md#creating-codesystems-for-the-dsf-processes)
on how to create [CodeSystems](basic-concepts-and-guides.md#codesystem).

***

### Creating CodeSystems for DSF Processes

You might find yourself in a situation where you need to create a [CodeSystem](basic-concepts-and-guides.md#codesystem).
For example, when defining the type of an [Input Parameter](basic-concepts-and-guides.md#task-input-parameters).
[CodeSystems](basic-concepts-and-guides.md#codesystem) for the DSF differ from regular [CodeSystems](basic-concepts-and-guides.md#codesystem)
in that some element's values are managed by the DSF BPE server. You can use the following XML as a template:  
```xml
<CodeSystem xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />    
        </tag>  
    </meta>
    <url value="http://dsf.dev/fhir/CodeSystem/my-code-system" />       <!--dummy value-->
    <!-- version managed by bpe -->
    <version value="#{version}" />
    <name value="My CodeSystem" />      <!--dummy value-->
    <title value="My CodeSystem Title" />       <!--dummy value-->
    <!-- status managed by bpe -->
    <status value="unknown" />
    <experimental value="false" />
    <!-- date managed by bpe -->
    <date value="#{date}" />
    <publisher value="DSF" />       <!--dummy value-->
    <description value="CodeSystem with codes for me" />        <!--dummy value-->
    <caseSensitive value="true" />
    <hierarchyMeaning value="grouped-by" />
    <versionNeeded value="false" />
    <content value="complete" />
    <concept>
        <code value="my-code" />        <!--dummy value-->
        <display value="My Code" />     <!--dummy value-->
        <definition value="My code used for myself" />      <!--dummy value-->
    </concept>
</CodeSystem> 
```
Replace dummy values with appropriate values of your own. Do not change elements managed by the DSF BPE server.  
You can add as many codes as you like by defining more `concept` elements.

The DSF BPE server will read your [CodeSystem](basic-concepts-and-guides.md#codesystem) from
`tutorial-process/src/main/resources/fhir/CodeSystem`.

***

### Creating an ActivityDefinition

This guide will teach you how to create an ActivityDefinition based on the [dsf-activity-definition](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-activity-definition-1.0.0.xml) profile for your process plugin. 
It is divided into steps for each of the main components of ActivityDefinitions:
1. Read Access Tag
2. Extension: process authorization
3. BPE Managed Elements
4. Regular Elements

*Regular elements* are all elements not part of the first 3 main components.

*We will assume you know how to translate [ElementDefinitions](https://www.hl7.org/fhir/R4/elementdefinition.html) to actual elements in a FHIR resource. 
If you do not, you might want to check out the guide on [creating Task resources](basic-concepts-and-guides.md#creating-task-resources-based-on-a-definition) first.*

#### 1. Read Access Tag
Let us start out with an empty [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition):
```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
    
</ActivityDefinition>
```

The first element in DSF FHIR resources is always the [Read Access Tag](basic-concepts-and-guides.md#read-access-tag). It describes who is
allowed to read this resource through the DSF FHIR server's REST API. You can learn more complex configurations of the 
[Read Access Tag](basic-concepts-and-guides.md#read-access-tag) in [this guide](basic-concepts-and-guides.md#configuring-the-read-access-tag). In this case, we will allow read access to everyone:

```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag>
    </meta> 
</ActivityDefinition>
```

#### 2. Extension: Process Authorization
This part of your ActivityDefinition will tell the DSF who is allowed to request and receive messages ([Task](basic-concepts-and-guides.md#task) resources)
for your BPMN process. If your plugin contains more than one BPMN process, you will have to create one [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition)
for each BPMN process. It is important to note that you need to include authorization rules for **ALL** messages received in your BPMN process.
This includes the message starting your BPMN process initially.   
You can find the extension [here](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml).
Let us continue by adding the [extension element](http://hl7.org/fhir/R4/extensibility.html#extension) with the correct URL. You can get the
value for the URL from the `Extension.url` element: 
```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
   ...
    <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
     
    </extension>
</ActivityDefinition>
```
*Elements not relevant to the current component are hidden with ... to increase readability.*

The [differential](https://www.hl7.org/fhir/R4/profiling.html#snapshot) statement
starts by defining the [slicing](https://www.hl7.org/fhir/R4/profiling.html#snapshot) for the `Extension.extension` element:

```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
  ...
    <differential> 
        <element id="Extension">
            <path value="Extension" />
            <min value="1" />
        </element>
        <element id="Extension.extension">
            <path value="Extension.extension" />
            <slicing>
                <discriminator>
                    <type value="value" />
                    <path value="url" />
                </discriminator>
                <rules value="open" />
            </slicing>
        </element>
     ...
    </differential>
</StructureDefinition>
```

The above states that whenever this extension is used in a profile, the profile needs to include this extension at least once (`<min value="1" />`).
The [slicing](https://www.hl7.org/fhir/R4/profiling.html#snapshot) on `Extension.extension` tells us that elements of this [slicing](https://www.hl7.org/fhir/R4/profiling.html#snapshot)
are identified by the value of their URL (`<discriminator>`), which is always the case for extensions, and that other extensions can be added to the [slicing](https://www.hl7.org/fhir/R4/profiling.html#snapshot) (`<rules value="open" />`).
Since there is a [slicing](https://www.hl7.org/fhir/R4/profiling.html#snapshot) on `Extension.extension`, we are dealing with a nested extension.

After these initial element definitions come the elements relevant for your process plugin. The first one is the `message-name` slice:  
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
  ...
    <differential> 
     ...
        <element id="Extension.extension:message-name">
            <path value="Extension.extension" />
            <sliceName value="message-name" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Extension.extension:message-name.url">
            <path value="Extension.extension.url" />
            <fixedUri value="message-name" />
        </element>
        <element id="Extension.extension:message-name.value[x]">
            <path value="Extension.extension.value[x]" />
            <min value="1" />
            <type>
                <code value="string" />
            </type>
        </element>
     ...
    </differential>
</StructureDefinition>
```

This section tells us that we need to include exactly one extension element from the `message-name` slice in our [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition).
The extension element will have a URL value of `message-name`. If you remember the `discriminator` configuration, this URL value identifies the element to belong to the `message-name` slice on `Extension.extension`.
Lastly, the extension element includes a `valueString` element. In case you are wondering how `value[x]` turned into `valueString`,
FHIR does not allow using `value[x]` as actual element. The value in `value[x]` is always strictly bound to some kind of type.
FHIR uses the `value[x].type.code` value to determine this type and replaces `[x]` with an uppercase version of `element.type.code`.  
This results in the following extension element we will add to our [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition):
```xml
<extension url="message-name">
    <valueString value="myMessage"/>
</extension>
```

For your use case, you have to replace `myMessage` with the name of the [BPMN message event](basic-concepts-and-guides.md#messaging) that is expecting this message.

<details>
<summary>This is how your ActivityDefinition should look like so far</summary>

```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag>
    </meta>
    <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
        <extension url="message-name">
            <valueString value="myMessage"/>
        </extension>
    </extension>
</ActivityDefinition>
```
</details>

The next slice is called `task-profile`:  
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
  ...
    <differential> 
     ...
        <element id="Extension.extension:task-profile">
            <path value="Extension.extension" />
            <sliceName value="task-profile" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Extension.extension:task-profile.url">
            <path value="Extension.extension.url" />
            <fixedUri value="task-profile" />
        </element>
        <element id="Extension.extension:task-profile.value[x]">
            <path value="Extension.extension.value[x]" />
            <min value="1" />
            <type>
                <code value="canonical" />
            </type>
        </element>
     ...
    </differential>
</StructureDefinition>
```

This section has almost the same structure as `message-name`. The only difference is the value for `value[x].type.code`.
This means that instead of `valueString`, we will have to use a `valueCanonical` element for `task-profile.value[x]`.
Canonical values referring to [Task](basic-concepts-and-guides.md#task) profiles in ActivityDefinitions have to conform
to the rules outlined by the documentation on [URLs](basic-concepts-and-guides.md#urls).  
From the definition above, we will create the following extension element and add it to our [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition):
```xml
<extension url="task-profile">
    <valueCanonical value="http://dsf.dev/fhir/StructureDefinition/my-task|#{version}"/>
</extension>
```

<details>
<summary>This is how your ActivityDefinition should look like so far</summary>

```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag>
    </meta>
    <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
        <extension url="message-name">
            <valueString value="myMessage"/>
        </extension>
        <extension url="task-profile">
            <valueCanonical value="http://dsf.dev/fhir/StructureDefinition/my-task|#{version}"/>
        </extension>
    </extension>
</ActivityDefinition>
```
</details>

The next slice is `requester`:
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
  ...
    <differential> 
     ...
        <element id="Extension.extension:requester">
            <path value="Extension.extension" />
            <sliceName value="requester" />
            <min value="1" />
        </element>
        <element id="Extension.extension:requester.url">
            <path value="Extension.extension.url" />
            <fixedUri value="requester" />
        </element>
        <element id="Extension.extension:requester.value[x]">
            <path value="Extension.extension.value[x]" />
            <min value="1" />
            <type>
                <code value="Coding" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-all|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-all-practitioner|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-organization|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-organization-practitioner|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-parent-organization-role|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-parent-organization-role-practitioner|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-remote-all|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-remote-organization|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-remote-parent-organization-role|1.0.0" />
            </type>
            <binding>
                <strength value="required" />
                <valueSet value="http://dsf.dev/fhir/ValueSet/process-authorization-requester|1.0.0" />
            </binding>
        </element>
     ...
    </differential>
</StructureDefinition>
```
Instead of a `string` or `canonical` type for `value[x]` we now have a `Coding` type. See the [FHIR documentation on Codings](https://www.hl7.org/fhir/R4/datatypes.html#Coding)
for more in-depth information. `Codings` are elements which contain, among other things, a `code` and the `system` the code belongs to. In the same way we transformed `value[x]` into `valueString` or `valueCanonical` before, we will also
have to turn `value[x]` into `valueCoding`. To use `Codings` in `valueCoding` elements, they are usually bound to the element through a [ValueSet](basic-concepts-and-guides.md#valueset). This is the
responsibility of the `binding` element. You can also see that `value[x].type.profile` lists a number of profiles. Instead of defining the 
elements in the same file, they were defined in different files for better readability. Depending on your use case, you have to pick one of the profiles.
Here is what they mean:
- `local-all`: All local requests will be allowed. Local requests are identified by matching the requester's certificate to a thumbprint which was internally marked by the DSF FHIR server as belonging to a local organization.
- `local-organization`: All local requests made from an organization with a specific `organization-identifier` will be allowed. 
- `local-parent-organization-role`: All local requests made from an organization having a specific role inside a specific parent organization will be allowed.
- `remote` versions of the above rules work the same but the requester's certificate is instead required to match a thumbprint marked as a remote organization.
- `practitioner` suffixes all work the same. They include the same rules as their prefixes but now additionally require the requester to match a certain `practitioner-role`. A list of them
  can be found [here](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-practitioner-role-1.0.0.xml). This allows
  for more granularity when defining authorization rules within an organization and can be integrated into local user management via [OpenID Connect](https://dsf.dev/stable/maintain/fhir/access-control.html).

As you can see, there are no `practitioner` versions of `remote` authorization rules. From the perspective of the receiving DSF instance,
remote requests are always issued by an organization. They do not hold any information about the local user management of the requesting organization.  
You can also find examples of all Codings from above [here](basic-concepts-and-guides.md#examples-for-requester-and-recipient-elements).

It is also good to keep in mind that you are allowed to add any number of `requester` elements into your [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition).  
Let us start out by adding a `requester` element like we did for previous elements:

```xml
<extension url="requester">
    <valueCoding>
        
    </valueCoding>
</extension>
```

We now have to look at the elements that are defined in one of the profiles to fill in the remaining elements since they are not defined by the `requester` extension. For demonstration
purposes, we will choose the [dsf-coding-process-authorization-local-organization-practitioner](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-coding-process-authorization-local-organization-practitioner-1.0.0.xml) profile.
Since all elements listed in the [Coding definition](https://www.hl7.org/fhir/R4/datatypes.html#codesystem) are optional, we only have to look at the `differential` element from the profile we just selected:
<a id="coding-differential"></a>
```xml
<differential>
    <element id="Coding.extension">
        <path value="Coding.extension" />
        <slicing>
            <discriminator>
                <type value="value" />
                <path value="url" />
            </discriminator>
            <rules value="open" />
        </slicing>
    </element>
    <element id="Coding.extension:organization-practitioner">
        <path value="Coding.extension" />
        <sliceName value="organization-practitioner" />
        <min value="1" />
        <max value="1" />
        <type>
            <code value="Extension" />
            <profile value="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner|1.0.0" />
        </type>
    </element>
    <element id="Coding.system">
        <path value="Coding.system" />
        <min value="1" />
        <fixedUri value="http://dsf.dev/fhir/CodeSystem/process-authorization" />
    </element>
    <element id="Coding.code">
        <path value="Coding.code" />
        <min value="1" />
        <fixedCode value="LOCAL_ORGANIZATION_PRACTITIONER" />
    </element>
</differential>
```
It defines an extension called `organization-practitioner` which is identified through its url attribute. Again, the extension
is only referenced, its location is in a different file. You can find it [here](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-organization-practitioner-1.0.0.xml).
Let us look at its `differential` element in the extension file to see how we need to populate the extension:
```xml
<differential>
    <element id="Extension">
        <path value="Extension" />
        <min value="1" />
        <max value="1" />
    </element>
    <element id="Extension.extension">
        <path value="Extension.extension" />
        <slicing>  
            <discriminator>
                <type value="value" />
                <path value="url" />
            </discriminator>
            <rules value="open" />
        </slicing>
    </element>
    <element id="Extension.extension:organization">
        <path value="Extension.extension" />
        <sliceName value="organization" />
        <min value="1" />
        <max value="1" />
    </element>
    <element id="Extension.extension:organization.url">
        <path value="Extension.extension.url" />
        <fixedUri value="organization" />
    </element>
    <element id="Extension.extension:organization.value[x]">
        <path value="Extension.extension.value[x]" />
        <min value="1" />
        <type>
            <code value="Identifier" />
        </type>
    </element>
    <element id="Extension.extension:organization.value[x].system">
        <path value="Extension.extension.value[x].system" />
        <min value="1" />
        <fixedUri value="http://dsf.dev/sid/organization-identifier" />
    </element>
    <element id="Extension.extension:organization.value[x].value">
        <path value="Extension.extension.value[x].value" />
        <min value="1" />
    </element>
    <element id="Extension.extension:practitionerRole">
        <path value="Extension.extension" />
        <sliceName value="practitionerRole" />
        <min value="1" />
        <max value="1" />
    </element>
    <element id="Extension.extension:practitionerRole.url">
        <path value="Extension.extension.url" />
        <fixedUri value="practitioner-role" />
    </element>
    <element id="Extension.extension:practitionerRole.value[x]">
        <path value="Extension.extension.value[x]" />
        <min value="1" />
        <type>
            <code value="Coding" />
        </type>
    </element>
    <element id="Extension.extension:practitionerRole.value[x].system">
        <path value="Extension.extension.value[x].system" />
        <min value="1" />
    </element>
    <element id="Extension.extension:practitionerRole.value[x].code">
        <path value="Extension.extension.value[x].code" />
        <min value="1" />
    </element>
    <element id="Extension.url">
        <path value="Extension.url" />
        <fixedUri value="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner" />
    </element>
    <element id="Extension.value[x]">
        <path value="Extension.value[x]" />
        <max value="0" />
    </element>
</differential>
```

This extension does not reference any other files. This means we reached the "deepest" level. So now we can start working our way back up again from here, by translating this
definition into actual extension elements, then inserting it into the Coding we selected, translating the rest of the element
definitions from the Coding resource and adding everything to our [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition).

We will start with the `Extension.url` element, since the `Extension` element is the parent element for all slices on the `Extension.extension` elements:
```xml
<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">

</extension>
```

Next, we will add the `organization` slice: 
```xml
<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
    <extension url="organization">
        <valueIdentifier>
            <system value="http://dsf.dev/sid/organization-identifier"/>
            <value value="My_Organization"/>
        </valueIdentifier>
    </extension>
</extension>
```
Finally, we will add the `practitionerRole` slice:

```xml
<extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
    <extension url="organization">
        <valueIdentifier>
            <system value="http://dsf.dev/sid/organization-identifier"/>
            <value value="My_Organization"/>
        </valueIdentifier>
    </extension>
    <extension url="practitioner-role">
        <valueCoding>
            <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
            <code value="DSF_ADMIN"/>
        </valueCoding>
    </extension>
</extension>
```

Notice that there is no `binding` element specified for `practitionerRole.value[x]`. This is intentional. In the example we used a code from the
[dsf-practitioner-role](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-practitioner-role-1.0.0.xml) CodeSystem.
This CodeSystem includes a standard set of codes which are often sufficient for DSF use cases. You can freely add other CodeSystems if you find these codes
do not apply for your use case. The code you set here can be used in the [DSF role config](https://dsf.dev/stable/maintain/fhir/access-control.html)
to allow certain users with this `practitioner-role` to send requests.

Working our way back up to the Coding we selected, we will now add the extension we just created as the `Coding.extension:organization-practitioner` element:
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
            <extension url="organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Organization"/>
                </valueIdentifier>
            </extension>
            <extension url="practitioner-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                    <code value="DSF_ADMIN"/>
                </valueCoding>
            </extension>
        </extension>
    </valueCoding>
</extension>
```
Now might be a good time to look at the [differential](#coding-differential) from the Coding again.  
Our next elements to be added are `Coding.system` and `Coding.code`:  
```xml
<extension url="requester">
    <valueCoding>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
            <extension url="organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Organization"/>
                </valueIdentifier>
            </extension>
            <extension url="practitioner-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                    <code value="DSF_ADMIN"/>
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/process-authorization"/>
        <code value="LOCAL_ORGANIZATION_PRACTITIONER"/>
    </valueCoding>
</extension>
```
Now we are finished with the `requester` extension and can add it to our [ActivityDefinition](basic-concepts-and-guides.md#activitydefinition) under 
the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml).

<details>
<summary>This is how your ActivityDefinition should look like so far</summary>

```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag>
    </meta>
    <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
        <extension url="message-name">
            <valueString value="myMessage"/>
        </extension>
        <extension url="task-profile">
            <valueCanonical value="http://dsf.dev/fhir/StructureDefinition/my-task|#{version}"/>
        </extension>
        <extension url="requester">
            <valueCoding>
                <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
                    <extension url="organization">
                        <valueIdentifier>
                            <system value="http://dsf.dev/sid/organization-identifier"/>
                            <value value="My_Organization"/>
                        </valueIdentifier>
                    </extension>
                    <extension url="practitioner-role">
                        <valueCoding>
                            <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                            <code value="DSF_ADMIN"/>
                        </valueCoding>
                    </extension>
                </extension>
                <system value="http://dsf.dev/fhir/CodeSystem/process-authorization"/>
                <code value="LOCAL_ORGANIZATION_PRACTITIONER"/>
            </valueCoding>
        </extension>
    </extension>
</ActivityDefinition>
```
</details>

Now we are back to looking at the [dsf-extension-process-authorization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml) again.
The last slice for this extension is `recipient`:
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
  ...
    <differential> 
     ...
        <element id="Extension.extension:recipient">
            <path value="Extension.extension" />
            <sliceName value="recipient" />
            <min value="1" />
        </element>
        <element id="Extension.extension:recipient.url">
            <path value="Extension.extension.url" />
            <fixedUri value="recipient" />
        </element>
        <element id="Extension.extension:recipient.value[x]">
            <path value="Extension.extension.value[x]" />
            <min value="1" />
            <type>
                <code value="Coding" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-all|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-organization|1.0.0" />
                <profile value="http://dsf.dev/fhir/StructureDefinition/coding-process-authorization-local-parent-organization-role|1.0.0" />
            </type>
            <binding>
                <strength value="required" />
                <valueSet value="http://dsf.dev/fhir/ValueSet/process-authorization-recipient|1.0.0" />
            </binding>
        </element>
     ...
    </differential>
</StructureDefinition>
```

The `recipient` will decide which DSF instance is allowed to process that message. That is the reason why you will not find any Codings for `remote` or `practitioner` here. 
For `requester`, we already decided that we will only allow users with a certain role from our own (local) organization to send this message.
So now we will only allow the DSF instance run by that same local organization to process the message. The right Coding for this job is
the [coding-process-authorization-local-organization](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-coding-process-authorization-local-organization-1.0.0.xml).
The configuration of a local requester and local receiver is often used for the message that starts up the first BPMN process of the plugin.  
The process of adding the `recipient` slice is the exact same as it is for `requester`. You can follow the steps for the `requester` slice again
but just use a different Coding.

<details>
<summary>Using the Coding we just decided on, this is how your ActivityDefinition should look like</summary>

```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag>
    </meta>
    <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
        <extension url="message-name">
            <valueString value="myMessage"/>
        </extension>
        <extension url="task-profile">
            <valueCanonical value="http://dsf.dev/fhir/StructureDefinition/my-task|#{version}"/>
        </extension>
        <extension url="requester">
            <valueCoding>
                <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
                    <extension url="organization">
                        <valueIdentifier>
                            <system value="http://dsf.dev/sid/organization-identifier"/>
                            <value value="My_Organization"/>
                        </valueIdentifier>
                    </extension>
                    <extension url="practitioner-role">
                        <valueCoding>
                            <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                            <code value="DSF_ADMIN"/>
                        </valueCoding>
                    </extension>
                </extension>
                <system value="http://dsf.dev/fhir/CodeSystem/process-authorization"/>
                <code value="LOCAL_ORGANIZATION_PRACTITIONER"/>
            </valueCoding>
        </extension>
        <extension url="recipient">
            <valueCoding>
                <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization">
                    <valueIdentifier>
                        <system value="http://dsf.dev/sid/organization-identifier"/>
                        <value value="My_Organization"/>
                    </valueIdentifier>
                </extension>
                <system value="http://dsf.dev/fhir/CodeSystem/process-authorization"/>
                <code value="LOCAL_ORGANIZATION"/>
            </valueCoding>
        </extension>
    </extension>
</ActivityDefinition>
```
</details>

The last element defined in the [process authorization extension](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-process-authorization-1.0.0.xml)
is `Extension.url`. But since we added this element at the very beginning of the working through the extension, we are finished with it here.

#### 3. BPE Managed Elements

Some elements of [ActivityDefinitions](basic-concepts-and-guides.md#activitydefinition) are managed by the DSF BPE and replaced with certain values
at appropriate times.

The following elements are managed by the DSF BPE: 
- `ActivityDefinition.version` should use the [placeholder](basic-concepts-and-guides.md#placeholders) `#{version}`
- `ActivityDefinition.date` is not required, but should you decide to include it, use the [placeholder](basic-concepts-and-guides.md#placeholders) `#{date}`
- `ActivityDefinition.status` must have a value of `unknown`

<details>
<summary>Your ActivityDefinition should now look like this</summary>

```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag>
    </meta>
    <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
        <extension url="message-name">
            <valueString value="myMessage"/>
        </extension>
        <extension url="task-profile">
            <valueCanonical value="http://dsf.dev/fhir/StructureDefinition/my-task|#{version}"/>
        </extension>
        <extension url="requester">
            <valueCoding>
                <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
                    <extension url="organization">
                        <valueIdentifier>
                            <system value="http://dsf.dev/sid/organization-identifier"/>
                            <value value="My_Organization"/>
                        </valueIdentifier>
                    </extension>
                    <extension url="practitioner-role">
                        <valueCoding>
                            <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                            <code value="DSF_ADMIN"/>
                        </valueCoding>
                    </extension>
                </extension>
                <system value="http://dsf.dev/fhir/CodeSystem/process-authorization"/>
                <code value="LOCAL_ORGANIZATION_PRACTITIONER"/>
            </valueCoding>
        </extension>
        <extension url="recipient">
            <valueCoding>
                <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization">
                    <valueIdentifier>
                        <system value="http://dsf.dev/sid/organization-identifier"/>
                        <value value="My_Organization"/>
                    </valueIdentifier>
                </extension>
                <system value="http://dsf.dev/fhir/CodeSystem/process-authorization"/>
                <code value="LOCAL_ORGANIZATION"/>
            </valueCoding>
        </extension>
    </extension>
    <!-- version managed by bpe -->
    <version value="#{version}"/>
    <!-- date managed by bpe -->
    <date value="#{date}"/>
    <!-- status managed by bpe -->
    <status value="unknown"/>
</ActivityDefinition>
```
</details>

#### 4. Regular Elements

The only required elements in this set are `ActivityDefinition.url` and `ActivityDefinition.kind`.
Check out the documentation on [URLs](basic-concepts-and-guides.md#urls) on how to choose the correct value for `ActivityDefinition.url`. `ActivityDefinition.kind` 
must have the value `Task`.
All other elements can technically be omitted. Still, we recommend you include the following elements:  
- `AcitivityDefinition.name`
- `AcitivityDefinition.title`
- `AcitivityDefinition.subtitle`
- `AcitivityDefinition.experimental`
- `AcitivityDefinition.publisher`
- `AcitivityDefinition.contact`
- `AcitivityDefinition.description`

<details>
<summary>Your finished ActivityDefinition should now look something like this</summary>

```xml
<ActivityDefinition xmlns="http://hl7.org/fhir">
    <meta>
        <tag>
            <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag" />
            <code value="ALL" />
        </tag>
    </meta>
    <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization">
        <extension url="message-name">
            <valueString value="myMessage"/>
        </extension>
        <extension url="task-profile">
            <valueCanonical value="http://dsf.dev/fhir/StructureDefinition/my-task|#{version}"/>
        </extension>
        <extension url="requester">
            <valueCoding>
                <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization-practitioner">
                    <extension url="organization">
                        <valueIdentifier>
                            <system value="http://dsf.dev/sid/organization-identifier"/>
                            <value value="My_Organization"/>
                        </valueIdentifier>
                    </extension>
                    <extension url="practitioner-role">
                        <valueCoding>
                            <system value="http://dsf.dev/fhir/CodeSystem/practitioner-role"/>
                            <code value="DSF_ADMIN"/>
                        </valueCoding>
                    </extension>
                </extension>
                <system value="http://dsf.dev/fhir/CodeSystem/process-authorization"/>
                <code value="LOCAL_ORGANIZATION_PRACTITIONER"/>
            </valueCoding>
        </extension>
        <extension url="recipient">
            <valueCoding>
                <extension url="http://dsf.dev/fhir/StructureDefinition/extension-process-authorization-organization">
                    <valueIdentifier>
                        <system value="http://dsf.dev/sid/organization-identifier"/>
                        <value value="My_Organization"/>
                    </valueIdentifier>
                </extension>
                <system value="http://dsf.dev/fhir/CodeSystem/process-authorization"/>
                <code value="LOCAL_ORGANIZATION"/>
            </valueCoding>
        </extension>
    </extension>
    <!-- version managed by bpe -->
    <version value="#{version}"/>
    <!-- date managed by bpe -->
    <date value="#{date}"/>
    <!-- status managed by bpe -->
    <status value="unknown"/>
    <url value="http://dsf.dev/bpe/Process/myProcess"/>
    <kind value="Task"/>
    <name value="My Process"/>
    <title value="My Title For My Process"/>
    <subtitle value="Information Processing Process"/>
    <experimental value="false"/>
    <publisher value="DSF"/>
    <contact>
        <name value="DSF"/>
        <telecom>
            <system value="email"/>
            <value value="noreply@dsf.dev"/>
        </telecom>
    </contact>
    <description value="My Process processes information"/>
</ActivityDefinition>
```
</details>

***

### Configuring the Read Access Tag

To start off, you want to take a look at the [CodeSystem](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-read-access-tag-1.0.0.xml) defined for the [Read Access Tag](basic-concepts-and-guides.md#read-access-tag)
and choose one of the codes from it:
```xml
<CodeSystem xmlns="http://hl7.org/fhir">
    ...
    <url value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
	...
	<concept>
		<code value="LOCAL"/>
		<display value="Local"/>
		<definition value="Read access for local users"/>
	</concept>
	<concept>
		<code value="ORGANIZATION"/>
		<display value="Organization"/>
		<definition value="Read access for organization specified via extension http://dsf.dev/fhir/StructureDefinition/extension-read-access-organization"/>
	</concept>
	<concept>
		<code value="ROLE"/>
		<display value="Role"/>
		<definition value="Read access for member organizations with role in consortium (parent organization) specified via extension http://dsf.dev/fhir/StructureDefinition/extension-read-access-consortium-role"/>
	</concept>
	<concept>
		<code value="ALL"/>
		<display value="All"/>
		<definition value="Read access for remote and local users"/>
	</concept>
</CodeSystem> 
```

The codes `LOCAL` and `ALL` are trivial. Their [Read Access Tag](basic-concepts-and-guides.md#read-access-tag) would look like this:  
```xml
<meta>
    <tag>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ALL"/> <!-- or value="LOCAL" respectively-->
    </tag>
</meta>
```

Let us try to configure a Read Access Tag whose code uses an extension. We will choose `ROLE` for this example. We start out the same way as before:
```xml
<meta>
    <tag>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

The `definition` element of the `ROLE` code references an extension called [dsf-extension-read-access-parent-organization-role](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/StructureDefinition/dsf-extension-read-access-parent-organization-role-1.0.0.xml).

The most important part of it is the `differential` statement. It uses [element definitions](https://www.hl7.org/fhir/R4/elementdefinition.html) to describe how we need to implement the extension:
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        <element id="Extension.extension">
            <path value="Extension.extension" />
            <slicing>
                <discriminator>
                    <type value="value" />
                    <path value="url" />
                </discriminator>
                <rules value="open" />
            </slicing>
        </element>
        <element id="Extension.extension:parentOrganization">
            <path value="Extension.extension" />
            <sliceName value="parentOrganization" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Extension.extension:parentOrganization.url">
            <path value="Extension.extension.url" />
            <fixedUri value="parent-organization" />
        </element>
        <element id="Extension.extension:parentOrganization.value[x]">
            <path value="Extension.extension.value[x]" />
            <min value="1" />
            <type>
                <code value="Identifier" />
            </type>
        </element>
        <element id="Extension.extension:parentOrganization.value[x].system">
            <path value="Extension.extension.value[x].system" />
            <min value="1" />
            <fixedUri value="http://dsf.dev/sid/organization-identifier" />
        </element>
        <element id="Extension.extension:parentOrganization.value[x].value">
            <path value="Extension.extension.value[x].value" />
            <min value="1" />
        </element>
        <element id="Extension.extension:organizationRole">
            <path value="Extension.extension" />
            <sliceName value="organizationRole" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Extension.extension:organizationRole.url">
            <path value="Extension.extension.url" />
            <fixedUri value="organization-role" />
        </element>
        <element id="Extension.extension:organizationRole.value[x]">
            <path value="Extension.extension.value[x]" />
            <min value="1" />
            <type>
                <code value="Coding" />
            </type>
        </element>
        <element id="Extension.extension:organizationRole.value[x].system">
            <path value="Extension.extension.value[x].system" />
            <min value="1" />
        </element>
        <element id="Extension.extension:organizationRole.value[x].code">
            <path value="Extension.extension.value[x].code" />
            <min value="1" />
        </element>
        <element id="Extension.url">
            <path value="Extension.url" />
            <fixedUri value="http://dsf.dev/fhir/StructureDefinition/extension-read-access-parent-organization-role" />
        </element>
        <element id="Extension.value[x]">
            <path value="Extension.value[x]" />
            <max value="0" />
        </element>
    </differential>
</StructureDefinition>
```

All extensions for the [Read Access Tag](basic-concepts-and-guides.md#read-access-tag) CodeSystem are defined on the `meta.tag.extension` element through
the extension's `context` element:
```xml
<context>
    <type value="element" />
    <expression value="Coding" />   <!-- meta.tag is of type Coding-->
</context>
```

That is why the first element we are adding to `meta.tag` is an `extension` element:
```xml
<meta>
    <tag>
        <extenion>
         
        </extenion>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

We will now go through the `differential` statement one element at a time, starting at the top:
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        <element id="Extension.extension">
            <path value="Extension.extension" />
            <slicing>
                <discriminator>
                    <type value="value" />
                    <path value="url" />
                </discriminator>
                <rules value="open" />
            </slicing>
        </element>
        ...
    </differential>
</StructureDefinition>
```

It defines a [slicing](https://www.hl7.org/fhir/R4/profiling.html#slicing) for the `Extension.extension` element, meaning we are dealing
with a nested extension. The `discriminator` element tells us that slices will be identified by the value of their `url` attribute.
A `rules` element with value `open` means other types of slices may be added later on e.g. when creating a profile. We do not have to 
add any elements from here to the `meta.tag.extension` element.  
Next up is the first slice called `parentOrganization`:

```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        ...
        <element id="Extension.extension:parentOrganization">
            <path value="Extension.extension" />
            <sliceName value="parentOrganization" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Extension.extension:parentOrganization.url">
            <path value="Extension.extension.url" />
            <fixedUri value="parent-organization" />
        </element>
        <element id="Extension.extension:parentOrganization.value[x]">
            <path value="Extension.extension.value[x]" />
            <min value="1" />
            <type>
                <code value="Identifier" />
            </type>
        </element>
        <element id="Extension.extension:parentOrganization.value[x].system">
            <path value="Extension.extension.value[x].system" />
            <min value="1" />
            <fixedUri value="http://dsf.dev/sid/organization-identifier" />
        </element>
        <element id="Extension.extension:parentOrganization.value[x].value">
            <path value="Extension.extension.value[x].value" />
            <min value="1" />
        </element>
        ...
    </differential>
</StructureDefinition>
```

The first element defines a slice called `parentOrganization` on the `Extension.extension` element with cardinality `1..1`.
The second element defines the url attribute of the `parentOrganization` slice to be fixed to the value `parent-organization`.
With this information we can add the next element to `meta.tag`. Since it is defined on `Extension.extension` we will add it to 
`meta.tag.extension.extension` like this:
```xml
<meta>
    <tag>
        <extension>
            <extension url="parent-organization">
             
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

After that, it defines `parentOrganization.value[x]` to occur at least once and have a type of `Identifier`. To turn this into an
element to add to `meta.tag.extension.extension` we have to replace `[x]` with our code in `value[x].type`, which in this case is `Identifier`. It is important
to note, that should the value in the code element be lowercase, you will have make it uppercase before replacement. In our case this means we will have a
`meta.tag.extension.extension.valueIdentifier` element: 
```xml
<meta>
    <tag>
        <extension>
            <extension url="parent-organization">
                <valueIdentifier>
                    
                </valueIdentifier>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

The last two elements define a `system` element with a fixed value and `value` element we can fill in on our own, since it does not have any constraints applied. Notice that
the element definition still uses `value[x].system` and `value[x].value`. The replacement mentioned earlier does not happen in
the element definition, but since `value[x]` is defined to have the type `Identifier` it is inferred that we mean to reference `Identifier.system`
and `Identifier.value`.  
We will choose an arbitrary `Idenfier` value, but you should be using an actual organization identifier depending on who you want to allow read access to the resource.

```xml
<meta>
    <tag>
        <extension>
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Organization"/>
                </valueIdentifier>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

Next is the slice is called `organizationRole`:
```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        ...
        <element id="Extension.extension:organizationRole">
            <path value="Extension.extension" />
            <sliceName value="organizationRole" />
            <min value="1" />
            <max value="1" />
        </element>
        <element id="Extension.extension:organizationRole.url">
            <path value="Extension.extension.url" />
            <fixedUri value="organization-role" />
        </element>
        <element id="Extension.extension:organizationRole.value[x]">
            <path value="Extension.extension.value[x]" />
            <min value="1" />
            <type>
                <code value="Coding" />
            </type>
        </element>
        <element id="Extension.extension:organizationRole.value[x].system">
            <path value="Extension.extension.value[x].system" />
            <min value="1" />
        </element>
        <element id="Extension.extension:organizationRole.value[x].code">
            <path value="Extension.extension.value[x].code" />
            <min value="1" />
        </element>
        ...
    </differential>
</StructureDefinition>
```

Like with `parentOrganization`, we will add an extension element to `meta.tag.extension` with the fixed url value defined above:
```xml
<meta>
    <tag>
        <extension>
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Organization"/>
                </valueIdentifier>
            </extension>
            <extension url="organization-role">
             
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

Instead of `Identifier`, the `value[x]` element is now defined as a `Coding` type. This means we will add a `valueCoding` element to the extension:
```xml
<meta>
    <tag>
        <extension>
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Organization"/>
                </valueIdentifier>
            </extension>
            <extension url="organization-role">
                <valueCoding>
                 
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

A `Coding` has to belong to some [CodeSystem](basic-concepts-and-guides.md#codesystem). The DSF has a CodeSystem called [dsf-organization-role](https://github.com/datasharingframework/dsf/blob/main/dsf-fhir/dsf-fhir-validation/src/main/resources/fhir/CodeSystem/dsf-organization-role-1.0.0.xml).
Before creating your own CodeSystem, it is worth taking a look at it to see if an appropriate role already exists for your organization.
For demonstration purposes, we will be using the `DIC` role:
```xml
<meta>
    <tag>
        <extension>
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Organization"/>
                </valueIdentifier>
            </extension>
            <extension url="organization-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/organization-role"/>
                    <code value="DIC"/>
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

Now we only have two elements left in the `differential` statement:

```xml
<StructureDefinition xmlns="http://hl7.org/fhir">
    ...
    <differential>
        ...
        <element id="Extension.url">
            <path value="Extension.url" />
            <fixedUri value="http://dsf.dev/fhir/StructureDefinition/extension-read-access-parent-organization-role" />
        </element>
        <element id="Extension.value[x]">
            <path value="Extension.value[x]" />
            <max value="0" />
        </element>
    </differential>
</StructureDefinition>
```

The `Extension.url` element tells us to add a url attribute to `meta.tag.extension`. The last element makes it so we must not
add a `meta.tag.extension.value[x]` element. This leaves us with this final Read Access Tag:

```xml
<meta>
    <tag>
        <extension url="http://dsf.dev/fhir/StructureDefinition/extension-read-access-parent-organization-role">
            <extension url="parent-organization">
                <valueIdentifier>
                    <system value="http://dsf.dev/sid/organization-identifier"/>
                    <value value="My_Organization"/>
                </valueIdentifier>
            </extension>
            <extension url="organization-role">
                <valueCoding>
                    <system value="http://dsf.dev/fhir/CodeSystem/organization-role"/>
                    <code value="DIC"/>
                </valueCoding>
            </extension>
        </extension>
        <system value="http://dsf.dev/fhir/CodeSystem/read-access-tag"/>
        <code value="ROLE"/> 
    </tag>
</meta>
```

You can follow the same method to configure the other types of Read Access Tags as well.

***