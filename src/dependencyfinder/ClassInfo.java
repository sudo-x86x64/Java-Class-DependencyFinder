package dependencyfinder;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.ooLab.language.javaByteCode.ClassFile;
import org.ooLab.language.javaByteCode.FieldInfo;
import org.ooLab.language.javaByteCode.JavaByteCodeException;
import org.ooLab.language.javaByteCode.MethodInfo;

public class ClassInfo {
    public  ArrayList<ClassInfo>    outBoundDependencies;   
    public  ArrayList<String>       classFeauturesTypes;
    
    public  String      className;
    public  String      superClassName;       
    public  String      packageName;    
    
    private final  String[]    baseTypes   = {"B", "C", "D", "F", "I", "J", "S", "Z"};
    
    public boolean alreadyVisitThisNode;
        
    public ClassInfo() {
        outBoundDependencies        = new  ArrayList<>();         
        classFeauturesTypes         = new  ArrayList<>();
        alreadyVisitThisNode        = false;                
    }       
    
    public void readClass(File item) throws FileNotFoundException, JavaByteCodeException{
        String classFileName    = item.getAbsolutePath();                                        
        String nameOfParentPackage;                                                                  
        
        FileInputStream fStream = new FileInputStream(classFileName);
        ClassFile classFile     = new ClassFile(new DataInputStream(fStream));

        className       = item.getName();
        className       = className.replace(".class", "");
        superClassName  = classFile.getSuperClass().getName().getText();                     

        nameOfParentPackage = classFile.getThisClass().getName().getText();
        nameOfParentPackage = nameOfParentPackage.replace("/" + className, "");                    
        packageName         = nameOfParentPackage;                                                           

        for(FieldInfo field: classFile.getFields()) {
            String tmp = field.getDescriptor().getText();
                        
            if(!checkType(tmp)) {
                tmp = tmp.substring(1,tmp.length()-1);
                classFeauturesTypes.add(tmp);           
            }
        }                                       

        for(MethodInfo method: classFile.getMethods()) {
            String tmp = method.getDescriptor().getText();
            ArrayList<String> tmpList = readMethodSignature(tmp);

            classFeauturesTypes.addAll(tmpList);                        
        }                    

        ArrayList<String>   nonDuplicatesList   = new ArrayList<>();
        Iterator<String>    dupIter             = classFeauturesTypes.iterator();

        while(dupIter.hasNext())    {
            String dupWord = dupIter.next();
            if(nonDuplicatesList.contains(dupWord)) {
                dupIter.remove();
            }
            else    {
                nonDuplicatesList.add(dupWord);
            }
        }                  

        classFeauturesTypes.clear();
        classFeauturesTypes = nonDuplicatesList;
    }
    
    private boolean checkType(String line) {
       for(String type: baseTypes) {
            if(type.equals(line)) {
                return true;
            }
        }                
       return false;
    }
    
    private ArrayList<String> readMethodSignature(String line) {
        ArrayList<String> tmp   = new ArrayList<>();        
        int closingBracket      = line.indexOf(")");
               
        if(!"V".equals(line.substring(closingBracket+1)) && !(checkType(line.substring(closingBracket+1)))) tmp.add(line.substring(closingBracket+2, line.length()-1));
        
        line = line.substring(1, closingBracket);
        
        if(!line.equals("")){                                        
            
            String[] tmpArray = line.split(";");

            int i= 0;
            for(String item: tmpArray){
                tmpArray[i] = item.substring(1);
                i++;
            }        
            
            i= 0;
            for(String item: tmpArray){
                if(item.startsWith("L"))tmpArray[i] = item.substring(1);
                i++;
            }

            
            tmp.addAll(Arrays.asList(tmpArray));                     
        }        
        
        return tmp;
    }
}