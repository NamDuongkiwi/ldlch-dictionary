package frames;

import backend.recorder.SoundRecorder;
import backend.speech.recognition.GSpeechRecognition;
import backend.speech.recognition.IBMSpeechRecognition;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author cuong
 */
public class SSTFrame extends javax.swing.JFrame {

    private Boolean isRecording = false;
    private final SoundRecorder recorder = new SoundRecorder();
    public final static String AUDIO_PATH = "./tmp/RecordAudio.wav";

    private final String VI_VI = "vi-vi";
    private final String EN_US = "en-us";

    private int serverType = SoundRecorder.IBM; // initialized with IBM engine
    private String lang = EN_US;

    private Icon stopIcon;
    private Icon startIcon;

    private MainFrame mainFrame;

    private final static SSTFrame instance = new SSTFrame();
    public static SSTFrame getInstance() { return instance; }

    private SSTFrame() {
        this.startIcon = new ImageIcon("./img/mic.png");
        this.stopIcon = new ImageIcon("./img/stop.png");
        initComponents();

        cbServer.setModel(new DefaultComboBoxModel<>(new String[]{"IBM", "Google"}));
        cbLang.setModel(new DefaultComboBoxModel<>(new String[]{EN_US}));

        this.setTitle("Ready for recording!");
//        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lbRecordStop.setIcon(startIcon);
    }

    public void setRootFrame(MainFrame rtFrame) {
        mainFrame = rtFrame;
    }

    public void execute() {
        addListeners();

    }

    private void addListeners() {

        lbRecordStop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isRecording) {
                    startRecord();

                } else {
                    stopRecord();

                    // send voice recognition
                    ArrayList<String> response = null;
                    if (serverType == SoundRecorder.GOOGLE) {
                        response = GSpeechRecognition.getInstance()
                                .sendPost(lang, AUDIO_PATH);

                    } else if (serverType == SoundRecorder.IBM) {
                        response = IBMSpeechRecognition.getInstance()
                                .sendPost("", AUDIO_PATH);

                    }

                    if (response != null) {
                        // push received data to JList
                        System.out.println(response);
                        displayResponseToList(response);

                        // request the MultipleThread to find received text data immediately
                        mainFrame.getTfSearchBar().setText(response.get(0));
                        mainFrame.filterDictionaryList();
                        mainFrame.doSearchWithVK_Enter();
                    }
                }
            }
        });
        cbServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String server_ = (String) cbServer.getSelectedItem();
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

                if (server_.equals("Google")) {
                    serverType = SoundRecorder.GOOGLE;
                    model.addElement(EN_US);
                    model.addElement(VI_VI);

                } else if (server_.equals("IBM")) {
                    serverType = SoundRecorder.IBM;
                    model.addElement(EN_US);
                }

                cbLang.setModel(model);
            }
        });
        cbLang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lang = (String) cbLang.getSelectedItem();
            }
        });
        lResut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedWord = (String) lResut.getSelectedValue();
                mainFrame.getTfSearchBar().setText(selectedWord);
                mainFrame.filterDictionaryList();
                mainFrame.doSearchWithVK_Enter();
            }
        });
    }

    public void displayResponseToList(ArrayList<String> data) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String e : data) {
            model.addElement(e);
        }

        lResut.setModel(model);
    }

    private void startRecord() {
        Thread recordThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    SSTFrame.this.setTitle("Recording now...");
                    lbRecordStop.setIcon(stopIcon);
                    isRecording = true;
                    recorder.start(serverType);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        recordThread.start();
    }

    private void stopRecord() {
        try {
            SSTFrame.this.setTitle("Done record! Processing...");
            lbRecordStop.setIcon(startIcon);
            isRecording = false;
            recorder.stop();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void initComponents() {

        panel = new javax.swing.JPanel();
        optionsPanel = new javax.swing.JPanel();
        cbServer = new javax.swing.JComboBox();
        cbLang = new javax.swing.JComboBox();
        lbRecordStop = new javax.swing.JLabel();
        resultPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lResut = new javax.swing.JList();

        cbServer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Server 1", "Server 2" }));

        cbLang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "LANG 1", "LANG 2" }));

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
                optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cbServer, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbLang, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                                .addComponent(lbRecordStop, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        optionsPanelLayout.setVerticalGroup(
                optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(optionsPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lbRecordStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbServer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbLang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cbLang.getAccessibleContext().setAccessibleName("");

        lResut.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lResut.setForeground(new java.awt.Color(153, 0, 153));
        lResut.setModel(new DefaultListModel<>());
        jScrollPane1.setViewportView(lResut);

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
                resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, resultPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)
                                .addContainerGap())
        );
        resultPanelLayout.setVerticalGroup(
                resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(resultPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(optionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

//    public static void main(String args[]) {
//        new SSTFrame().setVisible(true);
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbRecordStop;
    private javax.swing.JComboBox cbLang;
    private javax.swing.JComboBox cbServer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lResut;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel resultPanel;
    // End of variables declaration//GEN-END:variables
}
