package frames;

//import backend.DictionaryManager;
import database.DicAbstract;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by cuong on 10/21/2015.
 */
public class ModifyDictionaryFrame extends JFrame {
    private javax.swing.JButton btnSave;
    private javax.swing.JEditorPane epDefinition;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField tfWord;

    public final static int ADD_WORD = 1;
    public final static int MODIFY_WORD = 2;
    public final static int REMOVE_WORD = 3;

    private String word; // word for modifying
    private DicAbstract dic;
    private int type; // 1 for adding new, 2 for modifying, 3 for removing

    public ModifyDictionaryFrame(DicAbstract d, int t) {
        this(d, t, null);
    }

    public ModifyDictionaryFrame(DicAbstract d, int t, String w) {
        // config
        //setLookAndFeel("Nimbus");
        initGUIComponents();
        //this.setSize(640, 480);
        this.setResizable(false);

        // get and initialize data from parameters
        dic = d;
        word = w;
        type = t;

        // performAction
        prepare();
        performAction();
    }

    private void prepare() {
        if (type == REMOVE_WORD) { // perform removing word from dictionary
            this.setVisible(false);
            dic.removeWord(word);
            dic.setModified(true);
//            System.out.println(dic.getKeys());
            // set invisible
            this.dispose();

        } else if (type == ADD_WORD) { // adding action
            this.setVisible(true);
            this.setTitle("Adding new word");
            tfWord.setText("");
            epDefinition.setText("<html><head></head><body></body></html>");
            tfWord.requestFocus();

        } else if (type == MODIFY_WORD) { // modifying action
            this.setVisible(true);
            this.setTitle("Modifying word");
            tfWord.setText(word);
            epDefinition.setText(dic.getData().get(word));
        }
    }

    private void performAction() {

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tfWord.getText().trim().isEmpty() // empty filed
                    || epDefinition.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(ModifyDictionaryFrame.this,
                            "You are not allowed to  let any field empty!!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String rawDef = epDefinition.getText();
                rawDef = rawDef.replace("\n", "");
                rawDef = rawDef.replace("&lt;", "<");
                rawDef = rawDef.replace("&gt;", ">");

                if (type == ADD_WORD) {
                    String word = tfWord.getText().trim();
                    if (dic.getKeys().contains(word)) {
                        JOptionPane.showMessageDialog(ModifyDictionaryFrame.this,
                                "This word has already existed in dictionary!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return ;
                    }

                    dic.addWord(word, rawDef);
                    dic.setModified(true);


                } else if (type == MODIFY_WORD) {
                    dic.modifyWord(word, tfWord.getText().trim(), rawDef);
                    dic.setModified(true);

                }

                synchronized (ModifyDictionaryFrame.this) {
                    // set invisible
                    //ModifyDictionaryFrame.this.setVisible(false);
                    ModifyDictionaryFrame.this.dispose();
                    ModifyDictionaryFrame.this.notify();
                }

            }
        });

    }

//    private void setLookAndFeel(String lookAndFeel) {
//        try {
//            if (lookAndFeel.equals("Nimbus")) {
//                UIManager.setLookAndFeel(new NimbusLookAndFeel());
//            } else if (lookAndFeel.equals("Windows")) {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } else if (lookAndFeel.equals("Synthetica")) {
//                UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
//            }
//
//        } catch (UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MultipleThread.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }


    @SuppressWarnings("unchecked")
    private void initGUIComponents() {

        jPanel1 = new javax.swing.JPanel();
        tfWord = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();

        epDefinition = new javax.swing.JEditorPane();
        epDefinition.setContentType("text/html");

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnSave.setText("Save");

        jScrollPane1.setViewportView(epDefinition);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Word:");
        jLabel1.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Definition:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(tfWord, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jSeparator1)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                                        .addComponent(tfWord))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
}
