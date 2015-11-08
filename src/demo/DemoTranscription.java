package demo;

import backend.transcribe.IPATranscription;

import javax.swing.*;

/**
 * Created by cuong on 10/27/2015.
 */
public class DemoTranscription {


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception evt) {}

        JFrame f = new JFrame("Demo Transcription");

        JEditorPane pane = new JTextPane();
        pane.setContentType("text/html");

        String originText = "I love you more than I can say very very very much. Sometimes, I wanna you to" +
                " be mine";
        String res = IPATranscription.getInstance().getResult(originText);
        pane.setText("<h2 color=\"gray\">" + res + "</h2>" +
                "<hr/>");

        f.getContentPane().add(new JScrollPane(pane));
        f.setSize(400, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
