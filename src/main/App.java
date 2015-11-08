package main;

import frames.MainFrame;

import javax.swing.*;

/**
 * Created by cuong on 10/20/2015.
 */
public class App {

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });

    }

}
