package BasicApp;

//This class is used to write object into a file
//The 'Object' must be serializable (the class must 'implements java.io.Serializable')
//You must input a file path (String) with folder path, file name and extenstion
//You must input  an 'Object' or an 'ArrayList<Object>' that will be written to file

//@author Laurent Brodier
//@date 10.04.2015
//@version 1.0

public class FileWriterLB {
    //INSTANCE VARIABLES
    protected String file; //this is the file path ex: C:/users/user/myfile.obj
    //CONSTRUCTORS
    FileWriterLB(){
        file = "";
    }
    FileWriterLB(String path){
        file = path;
    }    
    //ACCESORS
    public String getFilePath(){
        return file;
    }
    public void setFilePath(String path){
        file = path;
    }
    
    //Functions
    public void writeObjectToFile(Object obj){
        if (file != ""){
            try{
                java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file));
                out.writeObject(obj);
                out.flush();
                out.close();
                System.out.println("The file has been saved to " + file);
            }catch(java.io.IOException e){
                System.out.println("IO Exception: " + e.getMessage());
            }catch(Exception e){
                System.out.println("Unknown error: " + e.getMessage());
            }
        }else{
            System.out.println("Error: file path is not specified.");
        }
    } 
    public void writeObjectToFile(String path, Object obj){
        try{
            file = path;
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file));
            out.writeObject(obj);
            out.flush();
            out.close();
        }catch(java.io.IOException e){
            System.out.println("IO Exception: " + e.getMessage());
        }catch(Exception e){
            System.out.println("Unknown error: " + e.getMessage());
        }
    } 
}
