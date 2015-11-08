package demo;

import frames.ImagePreview;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dinht_000 on 11/3/2015.
 */
public class DemoCrop extends JFrame implements MouseListener, MouseMotionListener{

    boolean dragStatus;
    int firstX, firstY;
    int lastX, lastY;
    boolean isPress;

    BufferedImage image;

    public DemoCrop(ImageIcon img){

        JLabel imgLabel = new JLabel("", img, JLabel.CENTER);
        getContentPane().add(imgLabel, BorderLayout.CENTER);
        setVisible(true);
        setSize(600,400) ;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);
        addMouseMotionListener(this);

        try {
            image = ImageIO.read(new File("./img/"));

        } catch (IOException ex) {
            Logger.getLogger(ImagePreview.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args){
        ImageIcon img = new ImageIcon("./src/img/icon.png");
        DemoCrop test = new DemoCrop(img);
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        isPress = true;
        dragStatus = false;
        repaint();
        firstX = e.getX();
        firstY = e.getY();
        System.out.println("Mouse Pressed");
        System.out.println(firstX + " " + firstY);
        System.out.println("DragStatus: " + dragStatus);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPress = false;
        repaint();
        if(dragStatus) {
            lastX = e.getX();
            lastY = e.getY();
        }
        System.out.println("Mouse Released");
        System.out.println(lastX + " " + lastY);
        System.out.println("DragStatus: " + dragStatus);

        //crop Image
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        repaint();
        dragStatus = true;
        lastX = e.getX();
        lastY = e.getY();
        System.out.println("Mouse Dragged");
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public static void drawPoint(Graphics g, int x, int y, int r){
        x = x-(r/2);
        y = y-(r/2);
        g.fillOval(x,y,r,r);

    }

    public void paint(Graphics g){
        super.paint(g);
        if(lastX > firstX && lastY > firstY && dragStatus){
            int weightRec = (lastX > firstX) ? (lastX - firstX) : (firstX - lastX);
            int heightRec = (lastY > firstY) ? (lastY - firstY) : (firstY - lastY);

            g.drawRect(firstX, firstY, weightRec, heightRec);
            Color dragColor = new Color(255, 255, 255, 70);
            Color finishColor = new Color(0, 0, 0, 0);

            if(isPress)
                g.setColor(dragColor);
            else
                g.setColor(finishColor);

            g.fillRect(firstX, firstY, weightRec, heightRec);


            System.out.println("Draw Rec at X:" + firstX + ", Y:" + firstY);
        }
    }
}
