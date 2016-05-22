package dependencyfinder;

import java.io.File;
import java.io.FileNotFoundException;
import org.ooLab.language.javaByteCode.JavaByteCodeException;

public class ClassPathReader {
         
    static void readClassPath(String classPath, final DependencyGraph graph) throws FileNotFoundException, JavaByteCodeException, Exception {
               
        File dir = new File(classPath);       
        File[] list = dir.listFiles();         
        
        for(File item: list) {
                                    
            if(item.isDirectory() == true)  {
                Package newPackage      = new Package();                                              
                newPackage.readPackage(item.getAbsolutePath());

                newPackage.packadgeName = item.getName();
                graph.packagesList.add(newPackage);                
            }                                      
        }        
    }    
}
