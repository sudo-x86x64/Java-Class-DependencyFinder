DependencyFinder is application that scan dependencies of required class with other classes within a Java project.

This application can find following types of class dependencies: 1. Inheritance dependencies; 2. Class’s fields dependencies; 3. Return type of methods; 4. Arguments type of methods within given Class.

DependencyFinder has few limitations: 1. Types of local variables; 2. Out of class method call.

DependencyFinder’s input arguments: 1. ClassPath [Path to directory that contain java class files or packages of classes. Avoid space and points characters within ClassPath, please]; 2. ClassName [Name of the class for dependencies search].

Recommendations: Package name is required If class name within given ClassPath is not unique. If multiple packages contains classes with identical names then DependencyFinder search the dependencies of the class with first name match in the dependency graph.