package dependencyfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DependencyGraph {
    public  ArrayList<Package>  packagesList; 
    public  ClassInfo           startNode;
    private String              targetClass; 
    
    public DependencyGraph() {
       packagesList = new ArrayList<>();
    }   
    
    private void setInheritanceDependencies(ClassInfo dependentClass) throws Exception{
            
        String[] graphPath = DependencyFinder.pathSeparator(dependentClass.superClassName);
        
        if("java".equals(graphPath[0])){                   
        }
        else {                         
            
            ArrayList<String> pathToClass = new ArrayList<>();
            pathToClass.addAll(Arrays.asList(graphPath));
                                    
            ClassInfo dependableClass = getClass(packagesList, pathToClass);                                    
            
            dependentClass.outBoundDependencies.add(dependableClass);                      
        }
    }       
    
    private boolean checkDependency(ClassInfo firstNode) {       
        
        firstNode.alreadyVisitThisNode = true;
        
        for(ClassInfo targetClassNode : firstNode.outBoundDependencies){
            if(targetClassNode.alreadyVisitThisNode) continue;
            else{
                if((targetClassNode.packageName + "/" + targetClassNode.className).equals(targetClass)){
                    return true;
                }else{
                    return checkDependency(targetClassNode);
                }
            }                       
        }
        
        return false;
    }        
    
    public void resetVisitedFlags(ArrayList<Package> Packages){
         for(Package item: Packages) {                        
            for(ClassInfo classItem: item.classes) {
                classItem.alreadyVisitThisNode = false;                              
            }
            resetVisitedFlags(item.subPackages);
        }    
    }
            
    private void setClassFeaturesTypesDependencies(ClassInfo dependentClass) throws Exception {
        
        for(String item :dependentClass.classFeauturesTypes){
            String[] graphPath = DependencyFinder.pathSeparator(item);
            
            if("Ljava".equals(graphPath[0]) || "java".equals(graphPath[0])){ 
            //We do not need connect dependencies with classes from rt.jar         
            }
            else {  
                targetClass = item;
                if(checkDependency(dependentClass)){
                    
                }
                else{
                    ArrayList<String> pathToClass = new ArrayList<>();
                    pathToClass.addAll(Arrays.asList(graphPath));
                                    
                    ClassInfo dependableClass = getClass(packagesList, pathToClass);                                                
                    dependentClass.outBoundDependencies.add(dependableClass);                    
                }
                resetVisitedFlags(packagesList);
            }            
        }        
    }
           
    public ClassInfo getClass(List<Package> packages, String className) throws Exception {
        for(Package item: packages) {
            for(ClassInfo targetClass: item.classes) {
                if(targetClass.className.equals(className)) {
                    return targetClass;
                }
            }
            return getClass(item.subPackages, className);
        }
       
        throw new Exception("Class not found in graph!"); 
    }
    
    public ClassInfo getClass(List<Package> packages, ArrayList<String> className) throws Exception {    
        if ( className.size() == 1 ) {               
            for ( Package item: packages ) {
                for(ClassInfo searchClass: item.classes){
                    if(searchClass.className.equals(className.get(0))) return searchClass;
                }
            }                
        }
        else 
            if ( className.size() > 1 ){
                for ( Package item: packages ) {
                    if(item.packadgeName.equals(className.get(0))) {
                        className.remove(0);
                        if(className.size() == 1)
                            return getClass(packages, className);
                        else
                            return getClass(item.subPackages, className);                                                                        
                    }
                }
            }            
        throw new Exception("Class not found in graph!");        
    }
                
    public void linkGraphbyInheritanceDependencies(ArrayList<Package> Packages) throws Exception {
      
        for(Package item: Packages) {                        
            for(ClassInfo classItem: item.classes) {
                setInheritanceDependencies(classItem);                
            }
            linkGraphbyInheritanceDependencies(item.subPackages);
        }                                      
    }
    
    public void linkGraphbyFeauturesDependencies(ArrayList<Package> Packages) throws Exception {      
        for(Package item: Packages) {                        
            for(ClassInfo classItem: item.classes) {
                setClassFeaturesTypesDependencies(classItem);                
            }
            linkGraphbyFeauturesDependencies(item.subPackages);
        }                                      
    }    
    
    public void printClassDependencies(ClassInfo className) {
        className.alreadyVisitThisNode = true;
        
        for(ClassInfo outBoundDep: className.outBoundDependencies) {            
            if(outBoundDep.equals(startNode))   continue;
            if(!outBoundDep.alreadyVisitThisNode){
                outBoundDep.alreadyVisitThisNode = true;
                
                System.out.println("\t-->depends on class " + outBoundDep.packageName + "/" + outBoundDep.className + ".class");
                printClassDependencies(outBoundDep);           
            }
        }
    }   
}