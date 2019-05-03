package BasicApp;

import java.util.*;

import com.philips.lighting.hue.sdk.*;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class HueEngine_LB { 
    //VARIABLES
    PHHueSDK phHueSDK; //Main PhHue class
    com.philips.lighting.hue.sdk.PHSDKListener listener; //PhHue event listener - to do specific actions on events
    PHBridgeSearchManager sm; //PhHue search manager - to discover bridge on local network
    
    PHAccessPoint ap; //composed of IP addredd and username, is used for connection to a bridge
    PHBridge bridge; //a bridge on local network
    List<PHLight> myLights; //list of light found on bridge
    
    String ipAddress = ""; 
    String username = "";
    
    HueControlForm CF; //user form to interact with the software <Control Form>
    boolean autoLogin = true; //is is set to true, try to connect automatically to last valid <IP, username> as saved in settings file
    
    //Main - create an instance and initialize it
    public static void main(String arg[]){
        HueEngine_LB PH = new HueEngine_LB();
        PH.init();
    }  
    //INITIALIZATION - starting point of the software
    public void init(){  
        //Create main instance for light control
        phHueSDK = PHHueSDK.create();
        phHueSDK.setAppName("AppDeLaurent");
        phHueSDK.setDeviceName("PcLB");
        
        //Listener for diverse events related to Philips Hue...
        listener = new com.philips.lighting.hue.sdk.PHSDKListener() {
            @Override
            public void onCacheUpdated(List<Integer> list, PHBridge phb) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            @Override
            public void onBridgeConnected(PHBridge phb) {
                //WHEN CONNECTED TO BRIDGE...
                CF.log("Connected to bridge " + ap.getIpAddress() + " as " + ap.getUsername());
                bridge = phb;
                PHBridgeResourcesCache cache = bridge.getResourceCache();
                myLights = cache.getAllLights();  
                CF.log("Light found: " + myLights.size());
                CF.setLights(myLights);                
                CF.enableControl();
                Settings s = new Settings(username, ipAddress);
                s.writeSettings();
            }
            @Override
            public void onAuthenticationRequired(PHAccessPoint phap) {
                //When new user connects, propose a PUSH and Link procedure
                ap = phap;
                phHueSDK.startPushlinkAuthentication(phap);
                CF.log("Starting PUSH-LINK authentification as " + phap.getUsername() + " on " + phap.getIpAddress());
            }
            @Override
            public void onAccessPointsFound(List<PHAccessPoint> list) {
                //When search and find a bridge on local network, auto-copy IP to user form
                if(list == null || list.size()==0){
                    System.out.println("Acces Point is empty...");
                    ipAddress = "";
                    return;
                }
                ipAddress = list.get(0).getIpAddress();
                CF.setIpAddress(ipAddress);
                CF.log("Bridge found at " + list.get(0).getIpAddress());
            }
            @Override
            public void onError(int i, String string) {
                //Log on errors
                CF.log("ERROR: " + string);
                CF.enableConnectionButtons();
            }
            @Override
            public void onConnectionResumed(PHBridge phb) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            @Override
            public void onConnectionLost(PHAccessPoint phap) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            @Override
            public void onParsingErrors(List<PHHueParsingError> list) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };        
        phHueSDK.getNotificationManager().registerSDKListener(listener);
        
        //Try to read from settings.ini file the last valid <IP, usrname> ("C:/HueLB/settings.ini")
        try{
            Settings s = new Settings("username", "ip address");
            s.readSettings();
            username = s.username;
            ipAddress = s.ip;
        }catch(java.lang.Exception e){
            username = "username";
            ipAddress = "ip address";
            autoLogin = false;            
        }
        
        //set windows look and feel if available
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        //Create the user form and initialize it with <IP> and <username>
        CF = new HueControlForm(this);        
        CF.setIpAddress(ipAddress);
        CF.setUsername(username);
        CF.setVisible(true);
        CF.log("Application sucessfully initialized!");
        if(autoLogin==true){
            CF.log("Automatic connection started:");
            CF.disableConnectionButtons();
            CF.connect();
        }
        
        //HueAnimationForm haf = new HueAnimationForm(this);
        //haf.setVisible(true);
        
    }
    
    //Creates an access point to connect with provided <IP> and <username>
    public void connectAs(String ip, String username){
        ap = new PHAccessPoint();
        ap.setIpAddress(ip);        
        ap.setUsername(username);
        phHueSDK.connect(ap);
        CF.log("Trying to connect to " + ip + " as " + username);
    }
    //Search for bridges on local network. The search result will be executed in PhHue listener <onAccessPointFound>
    public void findBridgeAdress(){
        sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);                
        sm.search(true, true);
        CF.log("Searching bridge on local network...");
    }
    
    //Set a light defined by lightIndex with brightness, hue, and saturation and log or not the change in CF
    public void setLight(int lightIndex, int bri, int hue, int sat, boolean log){
        myLights = bridge.getResourceCache().getAllLights();
        PHLight light = myLights.get(lightIndex);
        PHLightState state = new PHLightState();
        state.setBrightness(bri);
        state.setHue(hue);  
        state.setSaturation(sat);
        bridge.updateLightState(light, state);
        if(log==true){
            CF.log("Light changed: brightness = " + bri + ", hue = " + hue + ", saturation = " + sat);
        }
    } 
    //Set a light defined by lightIndex with brightness, hue, and saturation and log or not the change in CF
    public void setLight(int lightIndex, int bri, int hue, int sat, int transition, boolean log){
        myLights = bridge.getResourceCache().getAllLights();
        PHLight light = myLights.get(lightIndex);
        PHLightState state = new PHLightState();
        state.setBrightness(bri);
        state.setHue(hue);  
        state.setSaturation(sat);
        state.setTransitionTime(transition);
        bridge.updateLightState(light, state);
        if(log==true){
            CF.log("Light changed: brightness = " + bri + ", hue = " + hue + ", saturation = " + sat);
        }
    } 
    
    //Turns a light on or off
    public void setOnOff(int lightIndex, boolean on){
        myLights = bridge.getResourceCache().getAllLights();
        PHLight light = myLights.get(lightIndex);
        PHLightState state = new PHLightState();
        state.setOn(on);        
        bridge.updateLightState(light, state);
        if(on == true){
            CF.log("Light turned ON");
        }else{
            CF.log("Light turned OFF");
        }
    }
    //recovers the light state of a light defined by lightIndex
    public PHLightState getLightState(int lightIndex){
        myLights = bridge.getResourceCache().getAllLights();
        PHLight light = myLights.get(lightIndex);
        PHLightState state = light.getLastKnownLightState();
        return state;
    }
    
}
