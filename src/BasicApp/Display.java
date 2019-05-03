package BasicApp;
import java.awt.Color;
import java.awt.Graphics;


public class Display extends javax.swing.JPanel{
    //INSTANCE VARIABLES
    protected java.awt.Image img; //the image that is drawn in paintComponnent method
    protected int width, height; //size of the graphic panel
    
    int CX, CY;
    
    //CONSTRUCTOR
    Display(int w, int h){
        super();
        width = w;
        height = h;
        img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB); //adapt image to size
        this.setPreferredSize(new java.awt.Dimension(width, height)); //set panel size (the class calling this method should use: jFrame.add(display); jFrame.pack();
        this.setDoubleBuffered(true);
    }
    
    public int getMarkerX(){
        return CX;
    }
    public int getMarkerY(){
        return CY;
    }
    public void setMarkerPos(int X, int Y){
        CX = X;
        CY = Y;
        paintImage(img);
    }
            
    public void paintImage(java.awt.Image I){
        img = I;        
        this.repaint();
    } //receive an image as argument and paint it on componnent using repaint() that calls paintComponnent() that itself draw the image img
    public java.awt.Image getImage(){
        return img;
    }
    public void resetImg(){
        Graphics g = img.getGraphics();
        g.setColor(Color.black);
        g.fillRect(1, 1, width, height);
    }
    public void resizeDisplay(int w, int h){
        width = w;
        height = h;
        img = new java.awt.image.BufferedImage(width, height, 1);
        this.setPreferredSize(new java.awt.Dimension(width, height));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);     
        g.drawImage(img, 0, 0, this);
        //g.setColor(Color.white);
        //g.fillRect(CX-5, CY-1, 11, 3);
        //g.fillRect(CX-1, CY-5, 3, 11);
        g.setColor(Color.black);
        g.drawLine(CX-5, CY, CX+5, CY);
        g.drawLine(CX, CY-5, CX, CY+5);
    }
}
