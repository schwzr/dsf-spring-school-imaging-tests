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