### Certificates

There is a number of certificates that need to be generated in order for DSF instances to communicate with each other securely.
You can find a comprehensive lists of certificates needed by the [DSF FHIR](https://dsf.dev/stable/maintain/fhir/configuration.html)
and [DSF BPE](https://dsf.dev/stable/maintain/bpe/configuration.html) servers on the DSF website.  
Certificates will be created by the `test-data-generator` project through Maven by the time of the `package` phase in your process plugin build.
You can also invoke the generation of certificates separately by running the Maven build of `test-data-generator` until (and including) the `package` phase.   
Since this tutorial comes with three preconfigured DSF instances, the only time you will need to interact with certificates
is when you want to make requests to the DSF FHIR server. Either for access to the web frontend under https://instance-host-name/fhir/,
or when [starting your process plugin](../../guides/starting-a-process-via-task-resources.md).  
In case of the web frontend, you will need to add the CA certificate and client certificate of the DSF instance you want to access to your browser.
Certificates can be found in `test-data-generator/cert`.

**Example:**  
You want to access the `dic` DSF FHIR server. You add the CA certificate located in `test-data-generator/cert/ca` to your
browser's certificate store. You also add the client certificate for `dic` located in `test-data-generator/cert/dic-client`
to your browser's client certificates.

**Important: Passwords for .p12 files are always "password"**
