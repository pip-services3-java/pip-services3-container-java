# <img src="https://github.com/pip-services/pip-services/raw/master/design/Logo.png" alt="Pip.Services Logo" style="max-width:30%"> <br/> Basic portable abstractions for Java

This framework is a part of the Pip.Services project. It provides an inversion-of-control component container to facilitate the development of composable services and applications.

As all Pip.Services projects this framework is implemented in a variety of different languages: Java, .NET, Python, Node.js, Golang.

The framework provides a light-weight container that can be embedded inside a service or application, or can be run by itself, as a system process, for example. Container configuration serves as a recipe for instantiating and configuring components inside the container.
The default container factory provides generic functionality on-demand, such as logging and performance monitoring..

All functionality is decomposed into several packages:

- **Build** - Container default factory
- **Config** - Container configuration
- **Refer** - Container references

Quick Links:

* [Downloads](https://github.com/pip-services-java/pip-services-container-java/blob/master/doc/Downloads.md)
* [API Reference](http://htmlpreview.github.io/?https://github.com/pip-services-java/pip-services-container-java/blob/master/doc/api/index.html)
* [Building and Testing](https://github.com/pip-services-java/pip-services-container-java/blob/master/doc/Development.md)
* [Contributing](https://github.com/pip-services-java/pip-services-container-java/blob/master/doc/Development.md/#contrib)

## Acknowledgements

The initial implementation is done by **Sergey Seroukhov**. Pip.Services team is looking for volunteers to 
take ownership over Java implementation in the project.
