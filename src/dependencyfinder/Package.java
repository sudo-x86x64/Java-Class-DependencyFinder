package dependencyfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.ooLab.language.javaByteCode.JavaByteCodeException;


public class Package {
    public  ArrayList<ClassInfo>    classes;    
    public  ArrayList<Package>      subPackages;          
    public  String                  packadgeName;                    
    
    Package() {
        classes     = new ArrayList<>();
        subPackages = new ArrayList<>();      
    }
    
    public void readPackage(String path) throws FileNotFoundException, JavaByteCodeException {
        File dir        = new File(path);
        File[] list     = dir.listFiles();                             
               
        for(File item: list) {                
            if(item.isDirectory()) {
                Package tmp = new Package();
                
                tmp.packadgeName = item.getName();
                tmp.readPackage(item.getPath());
                
                subPackages.add(tmp);                
            }
            else if(item.isFile() && item.toString().endsWith(".class")) {
                ClassInfo myClass   = new ClassInfo();                    
                myClass.readClass(item);

                classes.add(myClass);
            }
        }
    }          
}
