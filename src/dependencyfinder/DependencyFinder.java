package dependencyfinder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import org.ooLab.language.javaByteCode.JavaByteCodeException;

public class DependencyFinder {  
           
    public static String[] pathSeparator(String path) {
        String directoryPathSeparator = "/";
        String[] separatedPath = path.split(directoryPathSeparator);
        
        return separatedPath;
    }
    
    public static void main(String[] args) throws FileNotFoundException, JavaByteCodeException, Exception {
        
        if(args[0].isEmpty() || args[1].isEmpty()) throw new Exception("Incorrect input arguments!");
            
        String          className       = args[1];
        ClassInfo       requiredClass   = new ClassInfo();                
        DependencyGraph graph           = new DependencyGraph();                        
        
        ClassPathReader.readClassPath(args[0], graph);        
        graph.linkGraphbyInheritanceDependencies(graph.packagesList);
        graph.linkGraphbyFeauturesDependencies(graph.packagesList);
        
        if(className.contains(".class")) {
            className = className.replace(".class", "");            
        }
        
        if(className.contains("/")) {
            ArrayList<String> classPackages = new ArrayList<>();                                          
            
            String[] tmpArray = pathSeparator(className);                        
            classPackages.addAll(Arrays.asList(tmpArray));                     
            
            requiredClass = graph.getClass(graph.packagesList, classPackages);
        }
        else{
            requiredClass = graph.getClass(graph.packagesList, className);   
        }
        
        System.out.println("Class " + requiredClass.className + " from "  + requiredClass.packageName + " package has dependencies with:");
        System.out.println("");
                
        graph.startNode = requiredClass;
        graph.printClassDependencies(requiredClass);        
    }
}

      
    
    
