package frames;

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
 *
 * @author cuong, loc
 */
public class ImagePreview extends JFrame implements MouseListener, MouseMotionListener {
    private JPanel panel;
    private BufferedImage image;

    private boolean dragStatus;
    private int firstX = 0, firstY = 0;
    private int lastX = 0, lastY = 0;

    public ImagePreview(String path) {
        super("Image Preview");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try {
            image = ImageIO.read(new File(path));
            
        } catch (IOException ex) {
            Logger.getLogger(ImagePreview.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final double SCALE_RATE = 0.8;
        int drawWidth = (int)(image.getWidth() * SCALE_RATE);
        int drawHeight = (int)(image.getHeight() * SCALE_RATE);

        this.setSize(drawWidth + 20, drawHeight + 30);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0,
                        this.getWidth(),
                        this.getHeight(),
                        null);

                if (lastX > firstX && lastY > firstY && dragStatus) {

//                    lastX = (lastX > drawWidth) ? drawWidth : lastX;
//                    lastY = (lastY > drawHeight) ? drawHeight : lastY;

                    int weightRec = (lastX > firstX) ? (lastX - firstX) : (firstX - lastX);
                    int heightRec = (lastY > firstY) ? (lastY - firstY) : (firstY - lastY);

                    g.drawRect(firstX, firstY, weightRec, heightRec);
                    Color dragColor = new Color(255, 255, 255, 70);
//                    Color finishColor = new Color(0, 0, 0, 0);

                    g.setColor(dragColor);
                    g.fillRect(firstX, firstY, weightRec, heightRec);
                }
            }
        };

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);

        this.add(panel);
    }

    private static BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        return bi;
    }

    public void crop() {

        BufferedImage orginalImage = createImage(this.panel);

        if (lastX == 0 && lastY == 0) { // select entire image
            lastX = orginalImage.getWidth();
            lastY = orginalImage.getHeight();
        }

        try {
            BufferedImage croppedImage = orginalImage.getSubimage(firstX, firstY, lastX-firstX, lastY-firstY);
            File f = new File("./tmp/crop_ocr.png");
            if (!f.exists()) {
                f.createNewFile();
            }
            ImageIO.write(croppedImage, "png", f);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStatus = false;
        repaint();
        firstX = e.getX();
        firstY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        repaint();
        if(dragStatus) {
            lastX = e.getX();
            lastY = e.getY();
        }
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
