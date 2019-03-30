# CIMS Validate

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.cims-bioko/validate/badge.svg?style=flat)](http://mvnrepository.com/artifact/com.github.cims-bioko/validate)

A non-visual, tool-centric ODK xform validation library. Unlike ODK Validate, 
this library does not bundle any dependencies, such as a logging 
implementation. It also does not attempt to provide a user-interface, instead
deferring that to other downstream projects.

## Why?

The canonical validate project was developed as a graphical tool, as is evident
from the fact that it was built with Java's Swing toolkit. It also bundles an
slf4j provider implementation within the jar, making it unsuitable for use
with projects that use slf4j (most projects). This library is essentially the
same thing, but built for a single purpose: providing a reliable validation
library for downstream tools.

## Requirements

To build this library, you'll need:

  * JDK 8+
  * Apache Maven

## Building

To build the library, you just need to run:

```
mvn clean install
```
