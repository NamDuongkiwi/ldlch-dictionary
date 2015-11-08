package demo;

import java.awt.*;
import java.net.*;
import javax.swing.*;

/**
 * Created by cuong on 10/20/2015.
 */
public class DemoSearchByWikitionary {

    public static void main(String args[]) {
        WikitionaryBrower browser = new WikitionaryBrower("school");

        browser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        browser.setVisible(true);
    }

}

class WikitionaryBrower extends JFrame {

    // Pattern link for search word on Wikitionary
    private final String WIKI_SEARCH = "https://vi.wiktionary.org/w/index.php?title={word}&printable=yes";

    // Editor pane for displaying pages.
    private JEditorPane displayEditorPane;

    // Constructor for Mini Web Browser.
    public WikitionaryBrower(String searchedWord) {
        // Set application title.
        super("Wikitionary Browser");

        // Set window size.
        setSize(640, 480);

        // Set up page display.
        displayEditorPane = new JEditorPane();
        displayEditorPane.setContentType("text/html");
        displayEditorPane.setEditable(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(displayEditorPane),
                BorderLayout.CENTER);

        // try to display retrieved data on Editor Pane
        try {
            this.showPage(new URL(WIKI_SEARCH.replace("{word}", searchedWord)));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /* Show the specified page and add it to
       the page list if specified. */
    public void showPage(URL pageUrl) {
        // Show hour glass cursor while crawling is under way.
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            // Load and display specified page.
            displayEditorPane.setPage(pageUrl);
            //System.out.println(displayEditorPane.getText());

        } catch (Exception e) {
            // Show error messsage.
            System.out.println(e.getMessage());
        } finally {
            // Return to default cursor.
            setCursor(Cursor.getDefaultCursor());
        }
    }
}

