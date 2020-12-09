# <img src="https://uploads-ssl.webflow.com/5ea5d3315186cf5ec60c3ee4/5edf1c94ce4c859f2b188094_logo.svg" alt="Pip.Services Logo" width="200"> <br/> IoC container for Java

This module is a part of the [Pip.Services](http://pipservices.org) polyglot microservices toolkit. It provides an inversion-of-control (IoC) container to facilitate the development of services and applications composed of loosely coupled components.

The module containes a basic in-memory container that can be embedded inside a service or application, or can be run by itself.
The second container type can run as a system level process and can be configured via command line arguments.
Also it can be used to create docker containers.

The containers can read configuration from JSON or YAML files use it as a recipe for instantiating and configuring components.
Component factories are used to create components based on their locators (descriptor) defined in the container configuration.
The factories shall be registered in containers or dynamically in the container configuration file.

The module contains the following packages:
- **Core** - Basic in-memory and process containers
- **Build** - Default container factory
- **Config** - Container configuration components
- **Refer** - Inter-container reference management (implementation of the Referenceable pattern inside an IoC container)

<a name="links"></a> Quick links:

* [Configuration Pattern](https://www.pipservices.org/recipies/configuration) 
* [API Reference](https://pip-services3-java.github.io/pip-services3-container-java/)
* [Change Log](CHANGELOG.md)
* [Get Help](https://www.pipservices.org/community/help)
* [Contribute](https://www.pipservices.org/community/contribute)

## Use

Go to the pom.xml file in Maven project and add dependencies::
```xml
<dependency>
  <groupId>org.pipservices3</groupId>
  <artifactId>pip-services3-container</artifactId>
  <version>3.0.0</version>
</dependency>
```

## Develop

For development you shall install the following prerequisites:
* Java SE Development Kit 8+
* Eclipse Java Photon or another IDE of your choice
* Docker
* Apache Maven

Build the project:
```bash
mvn install
```

Run automated tests:
```bash
mvn test
```

Generate API documentation:
```bash
./docgen.ps1
```

Before committing changes run dockerized build and test as:
```bash
./build.ps1
./test.ps1
./clear.ps1
```

## Contacts

The initial implementation is done by **Sergey Seroukhov**. Pip.Services team is looking for volunteers to 
take ownership over Java implementation in the project.
