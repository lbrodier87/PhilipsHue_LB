package BasicApp;

import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

public class HueControlForm extends javax.swing.JFrame {
    //VARIABLES
    HueEngine_LB LB; //The main HueEngine is copied here for communications purposes between the user interface and the engine
    String log = ""; //a log text to show in log area of the form
    javax.swing.JFrame f; //main frame of color picker
    Display d; //display contained in f (color picker)
    
    //Constructor - requires a PhHueEngine
    public HueControlForm(HueEngine_LB lb) {
        initComponents();   
        this.setLocationRelativeTo(null);
        f = new javax.swing.JFrame("Color Picker");
        d = new Display(456, 251); 
        f.addMouseListener(new java.awt.event.MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                                      
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    e.translatePoint(-f.getInsets().left, -f.getInsets().top);
                    d.setMarkerPos(e.getX(), e.getY());                    
                    int hue = (int)((double)e.getX()/455 * 65280);
                    int sat = (int)((double)e.getY()/250 * 254);
                    jSlider2.setValue(hue);
                    jSlider3.setValue(sat);
                    LB.setLight(jList1.getSelectedIndex(), Integer.parseInt(jTextField3.getText()), hue, sat, false);  
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    e.translatePoint(-f.getInsets().left, -f.getInsets().top);
                    d.setMarkerPos(e.getX(), e.getY());                    
                    int hue = (int)((double)e.getX()/455 * 65280);
                    int sat = (int)((double)e.getY()/250 * 254);
                    jSlider2.setValue(hue);
                    jSlider3.setValue(sat);
                    LB.setLight(jList1.getSelectedIndex(), Integer.parseInt(jTextField3.getText()), hue, sat, true);
                }
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });
        f.addMouseMotionListener(new java.awt.event.MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                e.translatePoint(-f.getInsets().left, -f.getInsets().top);
                d.setMarkerPos(e.getX(), e.getY());                    
                int hue = (int)((double)e.getX()/455 * 65280);
                int sat = (int)((double)e.getY()/250 * 254);
                jSlider2.setValue(hue);
                jSlider3.setValue(sat);
                LB.setLight(jList1.getSelectedIndex(), Integer.parseInt(jTextField3.getText()), hue, sat, false);
            }
            @Override
            public void mouseMoved(MouseEvent e) {}
        });
        try{
            java.awt.Image tmpImg = javax.imageio.ImageIO.read(getClass().getResource("Hue_Color.png"));  //(new java.io.File("C:/HueLB/Hue_Color.png"            
            java.awt.image.BufferedImage Img = new java.awt.image.BufferedImage(456, 251, java.awt.image.BufferedImage.TYPE_INT_RGB);        
            Img.getGraphics().drawImage(tmpImg, 0, 0, null);
            d.paintImage(tmpImg);
        }catch(java.io.IOException e){
            System.out.println("IOException " + e.getMessage());
        }
        f.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);                
        f.add(d); //add display to main frame  
        f.pack(); //arrange display in frame
        f.setLocationRelativeTo(null); //open in center screen
        
        LB = lb;
        jSlider1.setMaximum(254);
        jSlider2.setMaximum(65280);
        jSlider3.setMaximum(254);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                jTextField3.setText(""+jSlider1.getValue());                
                if(jCheckBox1.isSelected()){
                    changeLight(jList1.getSelectedIndex(), false);
                }
            }
        });
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                jTextField4.setText(""+jSlider2.getValue());
                if(jCheckBox1.isSelected()){
                    changeLight(jList1.getSelectedIndex(), false);
                }
            }
        });
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                jTextField5.setText(""+jSlider3.getValue());
                if(jCheckBox1.isSelected()){
                    changeLight(jList1.getSelectedIndex(), false);
                }
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                getLightState(e.getLastIndex());
            }
        });
    }
    
    //set the IP value in TextBox when bridge is found (HueEngineLB, PhHue listener), or when settings file is loaded
    public void setIpAddress(String ip){
        jTextField1.setText(ip);
    }
    //Set username when settings file is loaded
    public void setUsername(String user){
        jTextField2.setText(user);
    }   
    //Display a log on log area of the form
    public void log(String text){
        java.util.Date now = new java.util.Date();
        java.text.SimpleDateFormat simpleDate = new java.text.SimpleDateFormat("hh:mm:ss");
        log =  log + simpleDate.format(now) + " - " + text + "\n"; 
        jTextArea1.setText(log);
    }
    
    //When connected to a bridge, disable connection part and enable light conrol part
    public void enableControl(){
        disableConnectionButtons();        
        jToggleButton1.setEnabled(true);
        jButton3.setEnabled(true); 
        jButton4.setEnabled(true);
        jCheckBox1.setEnabled(true);
        jTextField3.setEnabled(true);
        jTextField4.setEnabled(true);
        jTextField5.setEnabled(true);
        jSlider1.setEnabled(true);
        jSlider2.setEnabled(true);
        jSlider3.setEnabled(true);
        getLightState(0);
    }
    //Update the light list with a list provided as argument
    public void setLights(java.util.List<PHLight> l){
        jList1.removeAll();
        ArrayList<String> names =  new ArrayList<>();
        int index = 0;
        for(PHLight light : l){
            names.add(l.get(index).getName() + " (" + l.get(index).getLightType().toString() + ")");
            index++;
        }
        jList1.setListData(names.toArray());   
        jList1.setSelectedIndex(0);
    }
    //Update the control part with the light state of the light with the corresponding index
    public void getLightState(int lightIndex){
        PHLightState state = LB.getLightState(lightIndex);
        jTextField3.setText(""+state.getBrightness());
        jTextField4.setText(""+state.getHue());
        jTextField5.setText(""+state.getSaturation());
        jToggleButton1.setSelected(state.isOn());        
        jSlider1.setValue(state.getBrightness());        
        jSlider2.setValue(state.getHue());        
        jSlider3.setValue(state.getSaturation());
    }
    //read values and sends it to HueControl to set a light to these values
    public void changeLight(int lightIndex, boolean log){
        int bri = Integer.parseInt(jTextField3.getText());
        int hue = Integer.parseInt(jTextField4.getText()); 
        int sat = Integer.parseInt(jTextField5.getText());
        LB.setLight(lightIndex, bri, hue, sat, log);
        jSlider1.setValue(bri);
        jSlider2.setValue(hue);
        jSlider3.setValue(sat);
        updateColorPickerMarker();
    }
    //Moves the marker in Color picker form
    public void updateColorPickerMarker(){
        int hue = Integer.parseInt(jTextField4.getText()); 
        int sat = Integer.parseInt(jTextField5.getText());
        int x = (int)((double)hue/65280*455);
        int y = (int)((double)sat/254*250);
        try{
            d.setMarkerPos(x, y);
        }catch(java.lang.Exception e){}
    }
    //Click on button connect, try to connect with IP and username in TextBoxes
    public void connect(){
        String username = jTextField2.getText();
        String ip = jTextField1.getText();
        LB.username = username;
        LB.ipAddress = ip;        
        LB.connectAs(ip, username);
    }
    //Disable connections buttons when connected, or when trying to connect
    public void disableConnectionButtons(){
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jTextField1.setEditable(false);
        jTextField2.setEditable(false);
    }
    //re-enable connection when tried unsucessfully to connect
    public void enableConnectionButtons(){
        jButton1.setEnabled(true);
        jButton2.setEnabled(true);
        jTextField1.setEditable(true);
        jTextField2.setEditable(true);
    }
    
    public int getSelectedLightIndex(){
        return jList1.getSelectedIndex();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton3 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jSlider3 = new javax.swing.JSlider();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Philips Hue - LB Control App 01");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("brightness");

        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.setEnabled(false);

        jLabel4.setText("hue");

        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.setEnabled(false);

        jToggleButton1.setText("On / Off");
        jToggleButton1.setEnabled(false);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("update");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jSlider1.setEnabled(false);

        jSlider2.setEnabled(false);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("live");
        jCheckBox1.setEnabled(false);

        jLabel5.setText("saturation");

        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.setEnabled(false);

        jSlider3.setEnabled(false);

        jButton4.setText("cp");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(jTextField4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(jSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSlider3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jToggleButton1)
                        .addGap(12, 12, 12)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jCheckBox1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("IP address");

        jLabel2.setText("username");

        jTextField1.setText("192.168.0.20");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setText("LaurentBrodier87");

        jButton1.setText("search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("connect");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        LB.findBridgeAdress();
    }//GEN-LAST:event_jButton1ActionPerformed
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        disableConnectionButtons();
        connect();
    }//GEN-LAST:event_jButton2ActionPerformed
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        changeLight(jList1.getSelectedIndex(), true);
    }//GEN-LAST:event_jButton3ActionPerformed
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        if(jToggleButton1.isSelected()){
            LB.setOnOff(jList1.getSelectedIndex(), true);
        }else{
            LB.setOnOff(jList1.getSelectedIndex(), false);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        f.setVisible(true);            
        updateColorPickerMarker();        
    }//GEN-LAST:event_jButton4ActionPerformed
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if(evt.getButton() == 3 || evt.getButton() == 2 || evt.isControlDown()){
            System.out.println("Click");
            HueAnimationForm HAF = new HueAnimationForm(LB);
            HAF.lightIndex = jList1.getSelectedIndex();
            HAF.setTitle("Animation: " + LB.myLights.get(HAF.lightIndex).getName());
            HAF.setVisible(true);
        }
    }//GEN-LAST:event_jList1MouseClicked
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
