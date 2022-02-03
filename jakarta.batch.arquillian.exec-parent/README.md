<!--- 
Copyright (c) 2021 Contributors to the Eclipse Foundation

See the NOTICE file distributed with this work for additional information regarding copyright 
ownership. Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. You may 
obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
Unless required by applicable law or agreed to in writing, software distributed 
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES 
OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
the specific language governing permissions and limitations under the License. 
SPDX-License-Identifier: Apache-2.0
--->
# Instructions how to run the TCK using this module

## How it works

1. Uses the standard Arquillian dependencies as for JUnit4, except the JUnit4 runner
2. Uses the official Arquillian JUnit5 extension and enables it globally via a service loader file
3. Enables the Arquillian JUnit5 extension by setting `junit.jupiter.extensions.autodetection.enabled` system property to true in pom.xml
4. Uses an Arquillian extension specific for Batch TCK to create a deployment for each test 
5. Generates the `test.properties` file with default test properties, which can be later modified and versioned. Existing `test.properties` file won't be overwritten.

The Batch TCK Arquillian extension is a separate module. It contains 

* The Arquillian extension class
* A service loader file to register the extension with Arquillian
* A service loader file to register the Arquillian JUnit 5 extension with JUnit 5 (because it's not included in the extension module)

# How to run the TCK

Create a new maven project that:

* Uses this maven artifact as the parent
* Contains the Arquillian container for the target runtime, including all its dependencies and configuration for it
* (Optionally but strongly recommended) Contains the `maven-dependency-plugin` in the list of plugins. This plugin 
is pre-configured in the parent POM to copy test resources from the TCK artifact
* (Optionally) Specify to exclude some artifacts on the maven test classpath from the Arquillian test 
deployment with the `artifact-group-prefixes-to-ignore` system property if they cause problems
* (Optionally) Execute `mvn pre-integration-test` and then modify the generated `test.properties` file to adjust system properties for the tests

An example for GlassFish:

```
    <parent>
        <groupId>jakarta.batch</groupId>
        <artifactId>jakarta.batch.arquillian.exec-parent</artifactId>
        <version>2.1.0-M2-SNAPSHOT</version>
    </parent>

    <artifactId>glassfish-batch-tck-execution</artifactId>
    <packaging>pom</packaging>
    <name>Jakarta Batch GlassFish Execution</name>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <configuration>
                    <systemPropertyVariables>
                        <artifact-group-prefixes-to-ignore>org.glassfish</artifact-group-prefixes-to-ignore>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <dependency>
            <groupId>org.jboss.arquillian.container</groupId>
            <artifactId>arquillian-glassfish-remote-6</artifactId>
            <version>1.0.0.Alpha1</version>
        <dependency>
    </dependencies>
```

For a complete example, see the example project in the `jakarta.batch.arquillian.exec` directory in the Batch TCK sources.

Then run the test suite with the following executed in the new project:

```
mvn clean verify
```
