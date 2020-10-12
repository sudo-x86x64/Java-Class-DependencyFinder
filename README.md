# Java-Class-DependencyFinder
DependencyFinder is an application that scans dependencies of any class within given *.jar. This application can find the following types of class dependencies: 
1. Inheritance dependencies; 
2. Class’s field dependencies; 
3. The return type of methods; 
4. Arguments type of methods within a given Class.

DependencyFinder has a few dependency detection limitations: 
1. Types of local variables; 
2. Out of class method call.

DependencyFinder’s input arguments: 
1. Jar file [Path to the directory that contains java class files or packages of classes. Avoid spaces and points characters within ClassPath]; 
2. ClassName [Name of the class for dependencies search].

Recommendations: Package name is required If the class name within the given ClassPath is not unique. If multiple packages contain classes with identical names then DependencyFinder searches the dependencies of the class with first name match in the dependency graph.
