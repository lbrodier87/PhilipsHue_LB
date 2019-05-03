package BasicApp;
//Setting of the HuenEngine_LB app
//Stores the last valid <IP> and <username> to attempt an automatic connection on startup

public class Settings implements java.io.Serializable{
    //VARIABLES
    String username;
    String ip;
    static String path = "settings.ini"; //setting file location is defined here ! "C:/HueLB/settings.ini"
    
    //CONSTRUCTOR
    public Settings(String user, String IP){
        username = user;
        ip = IP;
    }
    
    //WRITE THE SETTINGS
    public void writeSettings(){
        FileWriterLB FW = new FileWriterLB();
        FW.setFilePath(path);
        FW.writeObjectToFile(this);
    }
    //READS THE SETTINGS
    public void readSettings(){
        FileReaderLB FR = new FileReaderLB();
        FR.setFilePath(path);
        Settings s = (Settings)FR.readFileObject();
        ip = s.ip;
        username = s.username;
    }
}
