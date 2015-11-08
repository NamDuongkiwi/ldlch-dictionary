package frames;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cuong on 10/22/2015.
 */
public class WikitionaryBrowser extends JFrame {

    // Pattern link for search word on Wikitionary
    private final String WIKI_SEARCH = "https://vi.wiktionary.org/w/index.php?title=%s&printable=yes";

    // Editor pane for displaying pages.
    private JEditorPane displayEditorPane;
    private String searchWord;

    // Constructor for Mini Web Browser.
    public WikitionaryBrowser(String searchedWord) {
        // Set application title.
        super("Wikitionary Browser");
        this.searchWord = searchedWord;

        // Set window size.
        setSize(640, 480);
        this.setVisible(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set up page display.
        displayEditorPane = new JEditorPane();
        displayEditorPane.setContentType("text/html");
        displayEditorPane.setEditable(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(displayEditorPane),
                BorderLayout.CENTER);

    }

    public void showPageExternally(JEditorPane externalPane) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            // Load and display specified page.
            externalPane.setPage(new URL(String.format(WIKI_SEARCH, searchWord.replace(" ", "_"))));
            //System.out.println(displayEditorPane.getText());

        } catch (Exception e) {
            // Show error messsage.
            System.err.println(e.getMessage());
        } finally {
            // Return to default cursor.
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public void run() {
        // try to display retrieved data on Editor Pane
        try {
            this.setVisible(true);
            this.showPage(new URL(String.format(WIKI_SEARCH, searchWord.replace(" ", "_"))));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /* Show the specified page and add it to
       the page list if specified. */
    private void showPage(URL pageUrl) {
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

