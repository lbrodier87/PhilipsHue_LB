package BasicApp;

//This class is used to read 'Objects' in a file
//The objects must be serializable (the class must implements java.io.Serializable)
//You must input a file path (String) into 'file' with folder path, file name and extenstion
//The function readFileObject will return an Object or an ArrayList<Object>

//Example: 
//MyObject myObject = (MyObject)fileReaderLB(); or
//ArrayList<MyObject> myObject = (ArrayList<MyObject>)fileReaderLB();

//@author Laurent Brodier
//@date 10.04.2015
//@version 1.0
public class FileReaderLB {
    //INSTANCE VARIABLES
    protected String file;
    //CONSTRUCTORS
    FileReaderLB(){
        file = "";
    }
    FileReaderLB(String path){
        file = path;
    }
    //ACCESORS
    public String getFilePath(){
        return file;
    }
    public void setFilePath(String path){
        file = path;
    }
    //FUNCTIONS
    public Object readFileObject(String path){
        file = path;
        try{
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(file));
            Object obj = in.readObject();
            in.close();
            return obj;
        }catch(java.io.IOException e){
            System.out.println("IO exception: " + e.getMessage());
        }catch(java.lang.ClassNotFoundException e){
            System.out.println("Class not found exception: " + e.getMessage());
        }
        return null;
    }
    public Object readFileObject(){
        if (file != ""){
            try{
                java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(file));
                Object obj = in.readObject();
                in.close();
                return obj;
            }catch(java.io.IOException e){
                System.out.println("IO exception: " + e.getMessage());
            }catch(java.lang.ClassNotFoundException e){
                System.out.println("Class not found exception: " + e.getMessage());
            }
        }else{
            System.out.println("Error: file path is not specified.");
        }
        return null;    
    }
}
