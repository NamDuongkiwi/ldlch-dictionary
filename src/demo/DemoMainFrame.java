package demo;

import javax.swing.*;

/**
 * Created by cuong on 10/19/2015.
 */
public class DemoMainFrame {

    public static void main(String args[]) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}

class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MultipleThread
     */
    public MainFrame() {
        setLookAndFeel();
        initComponents();

    }

    private void setLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rootPanel = new javax.swing.JPanel();
        northBigPanel = new javax.swing.JPanel();
        firstTopPanel = new javax.swing.JPanel();
        separatorPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        dicSwitchingPanel = new javax.swing.JPanel();
        tgbtEnVi = new javax.swing.JToggleButton();
        tgbtViEn = new javax.swing.JToggleButton();
        separatorPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        searchBarPanel = new javax.swing.JPanel();
        tfSearchBar = new javax.swing.JTextField();
        lbClear = new javax.swing.JLabel();
        southBigPanel = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        scrollPaneForList = new javax.swing.JScrollPane();
        listDictionary = new javax.swing.JList();
        scrollPaneForTextArea = new javax.swing.JScrollPane();
        taDefinition = new javax.swing.JTextArea();
        lbCopyright = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        mnFile = new javax.swing.JMenu();
        mniExit = new javax.swing.JMenuItem();
        mnEdit = new javax.swing.JMenu();
        mniAddWord = new javax.swing.JMenuItem();
        mniModifyWord = new javax.swing.JMenuItem();
        mniRemoveWord = new javax.swing.JMenuItem();
        mnUtilities = new javax.swing.JMenu();
        mniGoogleTranslate = new javax.swing.JMenuItem();
        mniCaptureScreen = new javax.swing.JMenuItem();
        mnHelp = new javax.swing.JMenu();
        mniHelp = new javax.swing.JMenuItem();
        mniAboutUs = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        rootPanel.setLayout(new java.awt.BorderLayout());

        northBigPanel.setEnabled(false);
        northBigPanel.setLayout(new java.awt.BorderLayout());

        firstTopPanel.setLayout(new javax.swing.BoxLayout(firstTopPanel, javax.swing.BoxLayout.LINE_AXIS));

        separatorPanel1.setLayout(new java.awt.GridBagLayout());

        jSeparator1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 5);
        separatorPanel1.add(jSeparator1, gridBagConstraints);

        firstTopPanel.add(separatorPanel1);

        dicSwitchingPanel.setMaximumSize(new java.awt.Dimension(200, 32));
        dicSwitchingPanel.setMinimumSize(new java.awt.Dimension(0, 23));
        dicSwitchingPanel.setPreferredSize(new java.awt.Dimension(300, 32));
        dicSwitchingPanel.setRequestFocusEnabled(false);
        dicSwitchingPanel.setLayout(new java.awt.GridBagLayout());

        tgbtEnVi.setText("English->Vietnamese");
        tgbtEnVi.setToolTipText("");
        dicSwitchingPanel.add(tgbtEnVi, new java.awt.GridBagConstraints());

        tgbtViEn.setText("Vietnamese->English");
        dicSwitchingPanel.add(tgbtViEn, new java.awt.GridBagConstraints());

        firstTopPanel.add(dicSwitchingPanel);

        separatorPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 10);
        separatorPanel2.add(jSeparator2, gridBagConstraints);

        firstTopPanel.add(separatorPanel2);

        northBigPanel.add(firstTopPanel, java.awt.BorderLayout.NORTH);

        searchBarPanel.setPreferredSize(new java.awt.Dimension(800, 32));

        tfSearchBar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tfSearchBar.setForeground(new java.awt.Color(51, 51, 255));
        tfSearchBar.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tfSearchBar.setName(""); // NOI18N

        lbClear.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbClear.setToolTipText("");
        lbClear.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lbClear.setIcon(new ImageIcon("./src/img/clear.png"));

        javax.swing.GroupLayout searchBarPanelLayout = new javax.swing.GroupLayout(searchBarPanel);
        searchBarPanel.setLayout(searchBarPanelLayout);
        searchBarPanelLayout.setHorizontalGroup(
                searchBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(searchBarPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tfSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbClear, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        searchBarPanelLayout.setVerticalGroup(
                searchBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tfSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        northBigPanel.add(searchBarPanel, java.awt.BorderLayout.SOUTH);

        rootPanel.add(northBigPanel, java.awt.BorderLayout.NORTH);

        listDictionary.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollPaneForList.setViewportView(listDictionary);

        splitPane.setLeftComponent(scrollPaneForList);

        taDefinition.setColumns(20);
        taDefinition.setRows(5);
        taDefinition.setOpaque(false);
        scrollPaneForTextArea.setViewportView(taDefinition);

        splitPane.setRightComponent(scrollPaneForTextArea);

        lbCopyright.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lbCopyright.setForeground(new java.awt.Color(51, 102, 255));
        lbCopyright.setText("Made with love by ldlch");

        javax.swing.GroupLayout southBigPanelLayout = new javax.swing.GroupLayout(southBigPanel);
        southBigPanel.setLayout(southBigPanelLayout);
        southBigPanelLayout.setHorizontalGroup(
                southBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(southBigPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(southBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                                        .addGroup(southBigPanelLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(lbCopyright, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        southBigPanelLayout.setVerticalGroup(
                southBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(southBigPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbCopyright))
        );

        rootPanel.add(southBigPanel, java.awt.BorderLayout.CENTER);

        mnFile.setMnemonic('F');
        mnFile.setText("File");

        mniExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        mniExit.setText("Exit");
        mniExit.setToolTipText("");
        mnFile.add(mniExit);

        menuBar.add(mnFile);

        mnEdit.setMnemonic('E');
        mnEdit.setText("Edit");
        mnEdit.setToolTipText("");

        mniAddWord.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniAddWord.setText("Add word");
        mnEdit.add(mniAddWord);

        mniModifyWord.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniModifyWord.setText("Modify Word");
        mniModifyWord.setToolTipText("");
        mnEdit.add(mniModifyWord);

        mniRemoveWord.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniRemoveWord.setText("Remove Word");
        mnEdit.add(mniRemoveWord);

        menuBar.add(mnEdit);

        mnUtilities.setMnemonic('U');
        mnUtilities.setText("Utilities");
        mnUtilities.setToolTipText("");

        mniGoogleTranslate.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniGoogleTranslate.setText("Search with Google");
        mnUtilities.add(mniGoogleTranslate);

        mniCaptureScreen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniCaptureScreen.setText("Capture screen");
        mnUtilities.add(mniCaptureScreen);

        menuBar.add(mnUtilities);

        mnHelp.setMnemonic('A');
        mnHelp.setText("Help");
        mnHelp.setToolTipText("");

        mniHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mniHelp.setText("Help");
        mnHelp.add(mniHelp);

        mniAboutUs.setText("About us");
        mnHelp.add(mniAboutUs);

        menuBar.add(mnHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel dicSwitchingPanel;
    private javax.swing.JPanel firstTopPanel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbClear;
    private javax.swing.JLabel lbCopyright;
    private javax.swing.JList listDictionary;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu mnEdit;
    private javax.swing.JMenu mnFile;
    private javax.swing.JMenu mnHelp;
    private javax.swing.JMenu mnUtilities;
    private javax.swing.JMenuItem mniAboutUs;
    private javax.swing.JMenuItem mniAddWord;
    private javax.swing.JMenuItem mniCaptureScreen;
    private javax.swing.JMenuItem mniExit;
    private javax.swing.JMenuItem mniGoogleTranslate;
    private javax.swing.JMenuItem mniHelp;
    private javax.swing.JMenuItem mniModifyWord;
    private javax.swing.JMenuItem mniRemoveWord;
    private javax.swing.JPanel northBigPanel;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JScrollPane scrollPaneForList;
    private javax.swing.JScrollPane scrollPaneForTextArea;
    private javax.swing.JPanel searchBarPanel;
    private javax.swing.JPanel separatorPanel1;
    private javax.swing.JPanel separatorPanel2;
    private javax.swing.JPanel southBigPanel;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextArea taDefinition;
    private javax.swing.JTextField tfSearchBar;
    private javax.swing.JToggleButton tgbtEnVi;
    private javax.swing.JToggleButton tgbtViEn;
    // End of variables declaration
}

