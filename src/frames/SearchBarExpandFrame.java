package frames;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 *
 * @author cuong
 */
public class SearchBarExpandFrame extends JFrame {
    private JTextArea taContent;
    private JScrollPane scrollPane;
    private JPanel panel;
    private MainFrame mainFrame;

    private final static SearchBarExpandFrame instance = new SearchBarExpandFrame();
    public static SearchBarExpandFrame getInstance() { return instance; }

//    public static void main(String args[]) {
//        SearchBarExpandFrame f = new SearchBarExpandFrame(new JTextField());
//
//    }
    
    private SearchBarExpandFrame() {
        super("Search bar expanding...");
//        setVisible(true);
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setRootFrame(MainFrame rtFrame) {
        mainFrame = rtFrame;
    }

    public void execute() {
        panel = new JPanel(new BorderLayout());

        taContent = new JTextArea();
        taContent.setText(mainFrame.getTfSearchBar().getText());
        taContent.setLineWrap(true);
        taContent.setFont(new Font("Tahoma", Font.PLAIN, 26));

        taContent.setComponentPopupMenu(mainFrame.getPmnSearchBar());

        scrollPane = new JScrollPane(taContent);

        panel.add(scrollPane, BorderLayout.CENTER);
        this.add(panel);

        addListeners();
    }

    private void addListeners() {
        taContent.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                mainFrame.getTfSearchBar().setText(taContent.getText());
                mainFrame.filterDictionaryList();
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    mainFrame.getTfSearchBar().setText(taContent.getText());
                    mainFrame.filterDictionaryList();
                }

                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mainFrame.doSearchWithVK_Enter();
                }
            }
        
        });
    }
    
}
