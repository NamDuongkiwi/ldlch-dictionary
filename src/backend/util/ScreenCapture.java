package backend.util;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author cuong
 */
public class ScreenCapture {
//
//    public static void main(String args[]) {
//        ScreenCapture capturing = new ScreenCapture();
//        System.out.println("Done!");
//    }

    public ScreenCapture() {
        
        try {
            Robot robot = new Robot();
            String format = "png";
            String fileName = "./tmp/DicScreenCapture." + format;
            
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage fullScreenImag = robot.createScreenCapture(screenRect);
            ImageIO.write(fullScreenImag, format, new File(fileName));
            
        } catch (AWTException | IOException ex) {
            Logger.getLogger(ScreenCapture.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
