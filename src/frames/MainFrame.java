package frames;

import backend.transcribe.IPATranscription;
import backend.translator.GTranslator;
import backend.translator.MTranslator;
import backend.tts.MTTS;
import backend.tts.TTS_VALUES;
import backend.util.HistoryStack;
import backend.util.InternetCon;
import backend.util.LANG;
import backend.util.Pair;
import backend.util.ScreenCapture;
import backend.tts.WinTTS;
import database.*;
import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by cuong on 10/19/2015.
 */
public class MainFrame extends JFrame {

    private DicDatabase dicDatabase = new DicDatabase();
    private DicAbstract currentDic;

    private HistoryStack hStack = new HistoryStack();
    private Pair lastSearch = new Pair();

    private User user = new User();

    private String OnlTransSrcLang = LANG.ENGLISH;
    private String OnlTransDesLang = LANG.VIETNAMESE;

    private int ttsEnginee = TTS_VALUES.FREE_TTS;

    public MainFrame() {
        super("LDLCH - Dictionary");

        if (user.isValidUser()) {
            setLookAndFeel("Synthetica");
        } else {
            setLookAndFeel("Windows");
        }

        initGUIComponents();
        addListeners();
        loadInitialState();

        reloadStateForRegistration();


        // action for closing button
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                updateDic(dicDatabase.getDictionary(0));
                updateDic(dicDatabase.getDictionary(1));
            }

        });

        // set location
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    private void updateDic(DicAbstract d) {
        if (d.isModified()) {
            int choice = JOptionPane.showConfirmDialog(MainFrame.this, "You modified dictionaries\n" +
                            "Do you wanna update them?",
                    "Update dictionaries",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.OK_OPTION) {
                dicDatabase.getDictionary(0).update();
                dicDatabase.getDictionary(1).update();
            }
        }
    }

    private void addListeners() {
        // for toggle buttons
        MyToggleButtonListener tgbtListener = new MyToggleButtonListener();
        tgbtEnVi.addMouseListener(tgbtListener);
        tgbtViEn.addMouseListener(tgbtListener);

        // for words JList
        DictionaryListListener listLister = new DictionaryListListener();
        listDictionary.addMouseListener(listLister);
        listDictionary.addKeyListener(listLister);

        // for search panel
        SearchBarListener searchBarListener = new SearchBarListener();
        lbSpeaker.addMouseListener(searchBarListener);
        lbBack.addMouseListener(searchBarListener);
        tfSearchBar.addKeyListener(searchBarListener);
        lbRecorder.addMouseListener(searchBarListener);

        //! for menu bar
        MenuItemsListener menuItemsListener = new MenuItemsListener();
        mniUpdateDictionary.addActionListener(menuItemsListener);
        mniExit.addActionListener(menuItemsListener);
        // edit menu
        mniAddWordEV.addActionListener(menuItemsListener);
        mniAddWordVE.addActionListener(menuItemsListener);
        mniModifyWord.addActionListener(menuItemsListener);
        mniRemoveWord.addActionListener(menuItemsListener);
        // utilities menu
        mniWikitionaryDef.addActionListener(menuItemsListener);
        mniGoogleTranslate.addActionListener(menuItemsListener);
        mniMicrosoftTranslator.addActionListener(menuItemsListener);
        mniTranscribe.addActionListener(menuItemsListener);
        rbmniFreeTTS.addActionListener(menuItemsListener);
        rbmniMTTS.addActionListener(menuItemsListener);
        mniSTT.addActionListener(menuItemsListener);
        mniITT.addActionListener(menuItemsListener);
        mniCaptureScreen.addActionListener(menuItemsListener);
        mniNimbus.addActionListener(menuItemsListener);
        mniSystemTheme.addActionListener(menuItemsListener);
        mniSynthetica.addActionListener(menuItemsListener);
        // help menu
        mniHelp.addActionListener(menuItemsListener);
        mniAboutUs.addActionListener(menuItemsListener);
        mniRegister.addActionListener(menuItemsListener);

        //! for popup menu on Definition Pane
        PmnDPListener pmnDPListener = new PmnDPListener();
        pmniSearchDP.addActionListener(pmnDPListener);
        pmniSpeakDP.addActionListener(pmnDPListener);
        pmniCopyDP.addActionListener(pmnDPListener);
        pmniGoogleTranslateDP.addActionListener(pmnDPListener);
        pmniMicrosoftTranslatorDP.addActionListener(pmnDPListener);
        pmniWikitionaryDefDP.addActionListener(pmnDPListener);
        pmniTranscribeDP.addActionListener(pmnDPListener);
        pmniAddWordEVDP.addActionListener(pmnDPListener);
        pmniAddWordVEDP.addActionListener(pmnDPListener);
        pmniModifyDP.addActionListener(pmnDPListener);
        pmniRemoveDP.addActionListener(pmnDPListener);
        pmniSelectAllDP.addActionListener(pmnDPListener);
        pmniSwapLangForOnlTransDP.addActionListener(pmnDPListener);

        //! for popup menu on Search Bar
        PmnSBListener pmnSBListener = new PmnSBListener();
        pmniCopySB.addActionListener(pmnSBListener);
        pmniPasteSB.addActionListener(pmnSBListener);
        pmniExpandSB.addActionListener(pmnSBListener);

        //! for popup menu on Words List
        PmnLDListener pmnLDListener = new PmnLDListener();
        pmniAddWordEVLD.addActionListener(pmnLDListener);
        pmniAddWordVELD.addActionListener(pmnLDListener);
        pmniModifyLD.addActionListener(pmnLDListener);
        pmniRemoveLD.addActionListener(pmnLDListener);
        pmniRemoveAllLD.addActionListener(pmnLDListener);
    }


    private void loadInitialState() {
        tgbtEnVi.setSelected(false);
        tgbtViEn.setSelected(false);

        reloadStateForNewDic();
    }

    private void reloadStateForNewDic() {
        epDefinition.setText("");
        tfSearchBar.setText("");
        tfSearchBar.setRequestFocusEnabled(true);
        tfSearchBar.requestFocus();
        displayDictionaryKeys();
    }

    private void reloadStateForRegistration() {
        if (user.isValidUser()) {

            pmnOnlTransDP.setEnabled(true);

            mnOnlTrans.setEnabled(true);
            rbmniMTTS.setEnabled(true);
            mniSTT.setEnabled(true);
            mniITT.setEnabled(true);
            mniSynthetica.setEnabled(true);

            lbRecorder.setEnabled(true);
        }
    }

    private void displayWordDefinition(String searchedKey) {
        // update recent words list
        if (currentDic.getName().equals("E_V")) { // only available for E_V
            RecentWords rw = (RecentWords) dicDatabase.getDictionary(2);
            rw.addWord(searchedKey);
        }

        String def = currentDic.getData().get(searchedKey);
        epDefinition.setText(def);
        tfSearchBar.setText(searchedKey);

        //update history stack
        if (lastSearch.getK() != null) {
            hStack.push(lastSearch);
        }
        lastSearch = new Pair(searchedKey, def);

        listDictionary.requestFocus();
    }

    private void displayWordDefinition(String searchedKey, DicAbstract dic) {
        RecentWords rw = (RecentWords) dicDatabase.getDictionary(2);
        rw.addWord(searchedKey);

        tfSearchBar.setText(searchedKey);
        String def = dic.getData().get(searchedKey);
        epDefinition.setText(def);

        //update history stack
        if (lastSearch.getK() != null) {
            hStack.push(lastSearch);
        }
        lastSearch = new Pair(searchedKey, def);
    }

    private void displayDictionaryKeys() {
        if (tgbtEnVi.isSelected()) {
            currentDic = dicDatabase.getDictionary(0);
            pmniSwapLangForOnlTransDP.setText("Onl Trans: EN > VI");
            OnlTransSrcLang = LANG.ENGLISH;
            OnlTransDesLang = LANG.VIETNAMESE;

        } else if (tgbtViEn.isSelected()) {
            currentDic = dicDatabase.getDictionary(1);
            OnlTransSrcLang = LANG.VIETNAMESE;
            OnlTransDesLang = LANG.ENGLISH;
            pmniSwapLangForOnlTransDP.setText("Onl Trans: VI > EN");

        } else {
            currentDic = dicDatabase.getDictionary(2);
            OnlTransSrcLang = LANG.ENGLISH;
            OnlTransDesLang = LANG.VIETNAMESE;
            pmniSwapLangForOnlTransDP.setText("Onl Trans: EN > VI");

        }

        listDictionary.setListData(currentDic.getKeys().toArray());
    }

    private void searchInWikitioanry(String word) {
        if (word.isEmpty()) {
            JOptionPane.showMessageDialog(MainFrame.this, "Which word do you wanna search for?\n" +
                    "Please type it in the search bar!");
            return ;
        }

        doDisplayLoadingMessage("Loading... Please wait!");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (!InternetCon.getInstance().isConnected()) {
                    showErrorCon();
                    return null;
                }

                WikitionaryBrowser browser = new WikitionaryBrowser(word);
                browser.showPageExternally(epDefinition);
                browser.dispose();

                return null;
            }
        };
        worker.execute();
    }

    // classes for listeners below
    private class MyToggleButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == tgbtEnVi && tgbtViEn.isSelected()) {
                tgbtViEn.setSelected(false);
            } else if (e.getSource() == tgbtViEn && tgbtEnVi.isSelected()) {
                tgbtEnVi.setSelected(false);
            }

            reloadStateForNewDic();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    private class DictionaryListListener implements MouseListener, KeyListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            String selectedWord = (String) listDictionary.getSelectedValue();
            displayWordDefinition(selectedWord);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String selectedWord = (String) listDictionary.getSelectedValue();
                displayWordDefinition(selectedWord);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    private class SearchBarListener implements MouseListener, KeyListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel lb = (JLabel) e.getSource();
            if (lb == lbSpeaker) {
                String text = tfSearchBar.getText().trim();

                if (!text.isEmpty()) {
                    if (ttsEnginee == TTS_VALUES.FREE_TTS) {
                        WinTTS.getInstance().speak(text);

                    } else if (ttsEnginee == TTS_VALUES.MTTS) {
                        doMTTS(text);

                    }
                }

            } else if (lb == lbRecorder && user.isValidUser()) {
                SSTFrame f = SSTFrame.getInstance();

                Point mainFrameLoc = MainFrame.this.getLocation();
                Dimension mainFrameSize = MainFrame.this.getSize();
                f.setLocation(mainFrameLoc.x + mainFrameSize.width,
                        mainFrameLoc.y + 360);

                f.setRootFrame(MainFrame.this);
                f.setVisible(true);
                f.execute();

            } else if (lb == lbBack) {
                Pair last = hStack.pop();

                if (last != null) {
                    tfSearchBar.setText(last.getK());
                    epDefinition.setText(last.getV());
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                doSearchWithVK_Enter();

            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                listDictionary.setSelectedIndex(0);
                listDictionary.requestFocus();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_DOWN
                    && e.getKeyCode() != KeyEvent.VK_ENTER) { // reduce number of filtering
                MainFrame.this.filterDictionaryList();
            }
        }
    }
    private class MenuItemsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();

            if (item == mniUpdateDictionary) {
                if (currentDic instanceof Dictionary) {
                    currentDic.update();
                }
            } else if (item == mniExit) {
                System.exit(0);

            } else if (item == mniAddWordEV) {
                MainFrame.this.callAddingWord(dicDatabase.getDictionary(0));

            } else if (item == mniAddWordVE) {
                MainFrame.this.callAddingWord(dicDatabase.getDictionary(1));

            } else if (item == mniModifyWord) {
                MainFrame.this.callModifyingWord();

            } else if (item == mniRemoveWord) {
                new ModifyDictionaryFrame(currentDic, ModifyDictionaryFrame.REMOVE_WORD,
                        (String) listDictionary.getSelectedValue());
                MainFrame.this.reloadStateForNewDic();

            } else if (item == mniWikitionaryDef) {
                searchInWikitioanry(tfSearchBar.getText().trim());

            } else if (item == mniGoogleTranslate) {
                doGTrans(tfSearchBar.getText(), OnlTransSrcLang, OnlTransDesLang);

            } else if (item == mniMicrosoftTranslator) {
                doMTrans(tfSearchBar.getText(), OnlTransSrcLang, OnlTransDesLang);

            } else if (item == mniTranscribe) {
                doTranscribe(tfSearchBar.getText());

            } else if (item == mniSTT) {
                SSTFrame f = SSTFrame.getInstance();

                Point mainFrameLoc = MainFrame.this.getLocation();
                Dimension mainFrameSize = MainFrame.this.getSize();
                f.setLocation(mainFrameLoc.x + mainFrameSize.width,
                        mainFrameLoc.y + 360);
                f.setVisible(true);
                f.setRootFrame(MainFrame.this);
                f.execute();


            } else if (item == mniITT) {
//                JOptionPane.showMessageDialog(MainFrame.this, "This feature is not released");
                OCRFrame f = new OCRFrame(MainFrame.this);
                f.setLocationRelativeTo(MainFrame.this);

            } else if (item == rbmniFreeTTS) {
                //rbmniFreeTTS.setEnabled(true);
                ttsEnginee = TTS_VALUES.FREE_TTS;

            } else if (item == rbmniMTTS) {
                //rbmniMTTS.setEnabled(true);
                ttsEnginee = TTS_VALUES.MTTS;

            } else if (item == mniCaptureScreen) {
                new ScreenCapture();
                String path = "./tmp/DicScreenCapture.png";
                new ImagePreview(path);
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Your screenshot was saved in \"tmp\" :)",
                        "Screenshot saved",
                        JOptionPane.INFORMATION_MESSAGE);

            } else if (item == mniNimbus) {
                setLookAndFeel("Nimbus");
                SwingUtilities.updateComponentTreeUI(MainFrame.this);

            } else if (item == mniSystemTheme) {
                setLookAndFeel("Windows");
                SwingUtilities.updateComponentTreeUI(MainFrame.this);

            } else if (item == mniSynthetica) {
                setLookAndFeel("Synthetica");
                SwingUtilities.updateComponentTreeUI(MainFrame.this);

            } else if (item == mniHelp) {
                JOptionPane.showMessageDialog(MainFrame.this, "We don't support this" +
                        " function in demo version!");

            } else if (item == mniAboutUs) {
                AboutUsFrame aboutFrame = new AboutUsFrame();
                aboutFrame.setLocationRelativeTo(MainFrame.this);
            } else if (item == mniRegister) {
                RegisterBox box = new RegisterBox(user);
                box.setLocationRelativeTo(MainFrame.this);

                Thread t = new Thread() {
                    public void run() {
                        synchronized (box) {
                            while (box.isVisible()) {
                                try {
                                    System.out.println("\nWorking on register box");
                                    box.wait();

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Finish working on register box");
                                reloadStateForRegistration();
                            }
                        }
                    }
                };
                t.start();

            }
        }
    }
    private class PmnDPListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();
            String selectedStr = epDefinition.getSelectedText();

            if (selectedStr != null) {
                selectedStr = selectedStr.trim();

                if (item == pmniSearchDP) {
                    tfSearchBar.setText(selectedStr);
                    filterDictionaryList();

                    if (dicDatabase.getDictionary(0).getKeys().contains(selectedStr)) {
                        displayWordDefinition(selectedStr, dicDatabase.getDictionary(0));

                    } else {
                        doSearchWithVK_Enter();

                    }

                } else if (item == pmniSpeakDP) {
                    if (ttsEnginee == TTS_VALUES.FREE_TTS) {
                        WinTTS.getInstance().speak(selectedStr);

                    } else if (ttsEnginee == TTS_VALUES.MTTS) {
                        doMTTS(selectedStr);

                    }

                } else if (item == pmniCopyDP) {
                    epDefinition.copy();

                } else if (item == pmniGoogleTranslateDP) {
                    doGTrans(epDefinition.getSelectedText(), OnlTransSrcLang, OnlTransDesLang);

                } else if (item == pmniMicrosoftTranslatorDP) {
                    doMTrans(epDefinition.getSelectedText(), OnlTransSrcLang, OnlTransDesLang);

                } else if (item == pmniWikitionaryDefDP) {
                    searchInWikitioanry(selectedStr);

                } else if (item == pmniTranscribeDP) {
                    doTranscribe(selectedStr);
                }
            }

            if (item == pmniAddWordEVDP) {
                MainFrame.this.implicitilyAddWord(dicDatabase.getDictionary(0));

            } else if (item == pmniAddWordVEDP) {
                MainFrame.this.implicitilyAddWord(dicDatabase.getDictionary(1));

            } else if (item == pmniModifyDP) {
                MainFrame.this.callModifyingWord();

            } else if (item == pmniRemoveDP) {
                new ModifyDictionaryFrame(currentDic, ModifyDictionaryFrame.REMOVE_WORD,
                        (String)listDictionary.getSelectedValue());
                MainFrame.this.reloadStateForNewDic();

            } else if (item == pmniSelectAllDP) {
                epDefinition.selectAll();

            } else if (item == pmniSwapLangForOnlTransDP) {
                if (OnlTransSrcLang.equals(LANG.ENGLISH)) {
                    item.setText("Onl Trans: VI > EN");
                    OnlTransSrcLang = LANG.VIETNAMESE;
                    OnlTransDesLang = LANG.ENGLISH;
                } else {
                    item.setText("Onl Trans: EN > VI");
                    OnlTransSrcLang = LANG.ENGLISH;
                    OnlTransDesLang = LANG.VIETNAMESE;
                }
            }
        }
    }
    private class PmnSBListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();

            if (item == pmniCopySB) {
                tfSearchBar.copy();

            } else if (item == pmniPasteSB) {
                String clipboardText = getClipboardText();
                if (clipboardText != null) {
                    tfSearchBar.setText(clipboardText);
                    MainFrame.this.filterDictionaryList();
                }
            } else if (item == pmniExpandSB) {
                Point mainFrameLoc = MainFrame.this.getLocation();
//                System.out.println(mainFrameLoc);

                SearchBarExpandFrame expandFrame =
                        SearchBarExpandFrame.getInstance();
                Dimension mainFrameSize = MainFrame.this.getSize();
                expandFrame.setLocation(mainFrameLoc.x + mainFrameSize.width,
                        mainFrameLoc.y + 50);

                expandFrame.setRootFrame(MainFrame.this);
                expandFrame.setVisible(true);
                expandFrame.execute();
            }
        }
    }

    private class PmnLDListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();
            if (item == pmniAddWordEVLD) {
                MainFrame.this.callAddingWord(dicDatabase.getDictionary(0));
                MainFrame.this.reloadStateForNewDic();

            }
            else if (item == pmniAddWordVELD) {
                MainFrame.this.callAddingWord(dicDatabase.getDictionary(1));
                MainFrame.this.reloadStateForNewDic();

            } else if (item == pmniModifyLD) {
                MainFrame.this.callModifyingWord();

            } else if (item == pmniRemoveLD) {
                new ModifyDictionaryFrame(currentDic, ModifyDictionaryFrame.REMOVE_WORD,
                        (String)listDictionary.getSelectedValue());
                MainFrame.this.reloadStateForNewDic();

            } else if (item == pmniRemoveAllLD) {
                if (currentDic instanceof Dictionary) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Please consider to empty our dictionaries!" +
                                    "\nYou should empty only recent words list!",
                            "Not allow for emptying dictionaries data",
                            JOptionPane.ERROR_MESSAGE);
                    return ;
                }

                dicDatabase.getDictionary(2).removeAll();
                MainFrame.this.reloadStateForNewDic();
            }

        }
    }
    // end listeners class

    // implicitly add word to dic
    // key from tfSearchBar, definition from epDefinition
    public void implicitilyAddWord(DicAbstract dic) {
        if (!tfSearchBar.getText().isEmpty()) {
            String word = tfSearchBar.getText().trim();
            String rawDef = epDefinition.getText();
            rawDef = rawDef.replace("\n", "");

            if (dic.getKeys().contains(word)) {
                Object options[] = {"Override", "No"};
                int choice = JOptionPane.showOptionDialog(
                        MainFrame.this,
                        "This word has already existed in dictionary" +
                                "\nDo you wish to override it?",
                        "Adding existed word",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        options[1]
                );

                if (choice == 1) { return; }

                // else
                dic.modifyWord(word, word, rawDef);

            } else {
                dic.addWord(word, rawDef);

            }

            dic.setModified(true);
            reloadStateForNewDic();
        }
    }

    public void doSearchWithVK_Enter() {
        if (listDictionary.getModel().getSize() == 0) { // can't find out word on offline data

            Object options[] = {"Yes, sure", "No, I have no internet =.="};
            int choice = JOptionPane.showOptionDialog(MainFrame.this,
                    "This word is not available on offline database" +
                            "\nDo you wish to look up it online?",
                    "Search online",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                doMTrans(tfSearchBar.getText(), OnlTransSrcLang, OnlTransDesLang);
            }

        } else {
            listDictionary.setSelectedIndex(0);
            String selectedWord = (String) listDictionary.getSelectedValue();

            if (selectedWord != null) {

                displayWordDefinition(selectedWord);
            }
        }
    }

    private String getClipboardText() {
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            String res = (String) clipboard.getData(DataFlavor.stringFlavor);

            return res;
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void filterDictionaryList() {
        String filter = tfSearchBar.getText();
        listDictionary.setModel(getFilteredModel(filter));
    }

    private void doGTrans(String src, String srcLang, String desLang) {
        if (src == null || src.equals("")) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "The input must not be empty!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return ;
        }

        doDisplayLoadingMessage("Loading... Please wait!");

        System.out.printf("\nGTrans (%s -> %s):\n", srcLang, desLang);
        System.out.println("Request: " + src);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (!InternetCon.getInstance().isConnected()) {
                    showErrorCon();
                    return null;
                }

                String res = GTranslator.getInstance().translate_a(src.trim(), srcLang, desLang);

                System.out.println("Reponse: " + res);

                String def = "<h1 color=\"blue\">" + res + "</h1>" +
                        "<hr/>" +
                        "<h2 color=\"green\">" + src + "</h2>" +
                        "<hr/>" +
                        "<p align=\"right\"><i> Powered by Google Translate     <i></p>";

                epDefinition.setText(def);
                //update history stack
                if (lastSearch.getK() != null) {
                    hStack.push(lastSearch);
                }
                lastSearch = new Pair(src, def);
                return null;
            }
        };
        worker.execute();
    }

    private void doMTrans(String src, String srcLang, String desLang) {
        if (src == null || src.equals("")) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "The input must not be empty!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return ;
        }

        doDisplayLoadingMessage("Loading... Please wait!");

        System.out.printf("\nMTrans (%s -> %s):\n", srcLang, desLang);
        System.out.println("Request: " + src);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (!InternetCon.getInstance().isConnected()) {
                    showErrorCon();
                    return null;
                }

                String res = MTranslator.getInstance().execute(src.trim(), srcLang, desLang);

                System.out.println("Reponse: " + res);

                String def = "<h1 color=\"blue\">" + res + "</h1>" +
                        "<hr/>" +
                        "<h2 color=\"green\">" + src + "</h2>" +
                        "<hr/>" +
                        "<p align=\"right\"><i> Powered by Microsoft Translator <i></p>";

                epDefinition.setText(def);

                //update history stack
                if (lastSearch.getK() != null) {
                    hStack.push(lastSearch);
                }
                lastSearch = new Pair(src, def);

                return null;
            }
        };
        worker.execute();
    }

    private void doTranscribe(String src) {
        if (src == null || src.equals("")) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "The input must not be empty!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return ;
        }

        doDisplayLoadingMessage("Loading... Please wait!");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (!InternetCon.getInstance().isConnected()) {
                    showErrorCon();
                    return null;
                }

                String res = IPATranscription.getInstance().getResult(src.trim());

                String def = "<h1 color=\"gray\">" + res + "</h1>" +
                        "<hr/>" +
                        "<h2 color=\"green\">" + src + "</h2>";

                epDefinition.setText(def);
                //update history stack
                if (lastSearch.getK() != null) {
                    hStack.push(lastSearch);
                }
                lastSearch = new Pair(src, def);
                return null;
            }
        };
        worker.execute();
    }

    private void doMTTS(String srcText) {
        if (!InternetCon.getInstance().isConnected()) {
            showErrorCon();
            return;
        }

        System.out.println("\nMicrosoft TTS:");
        System.out.println("Request text: " + srcText);

        MTTS.getIntance().execute(srcText);

        System.out.println("Done!");

    }

    private void doDisplayLoadingMessage(String text) {
        epDefinition.setText("<html>" +
                "<br/>" +
                "<br/>" +
                "<center>" +
                "<img src ='file:./img/cube.gif' width='150' height='150'>" +
                "<h2 color='#FF0099'>" + text + "</h2>" +
                "</center>" +
                "</html>");
    }

    private void callModifyingWord() {
        if (!(currentDic instanceof Dictionary)) { // not allow RecentWords add word by this way
            return;
        }

        ModifyDictionaryFrame f = new ModifyDictionaryFrame(currentDic, ModifyDictionaryFrame.MODIFY_WORD,
                (String)listDictionary.getSelectedValue());

        f.setLocationRelativeTo(MainFrame.this);

        Thread t = new Thread() {
            public void run() {
                synchronized (f) {
                    while (f.isVisible()) {
                        try {
                            System.out.println("Working on dictionary now");
                            f.wait();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Finish modifying dictionary");
                        reloadStateForNewDic();
                        System.out.println("Updated dictionary to Main Frame");
                    }
                }
            }
        };
        t.start();
    }

    private void callAddingWord(final DicAbstract dic) {

        ModifyDictionaryFrame f = new ModifyDictionaryFrame(dic,
                ModifyDictionaryFrame.ADD_WORD);
        f.setLocationRelativeTo(MainFrame.this);

        Thread t = new Thread() {
            public void run() {
                synchronized (f) {
                    while (f.isVisible()) {
                        try {
                            System.out.println("Working on dictionary now");
                            f.wait();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Finish modifying dictionary");
                        reloadStateForNewDic();
                        System.out.println("Updated dictionary to Main Frame");
                    }
                }
            }
        };
        t.start();
    }

    public JTextField getTfSearchBar() {
        return tfSearchBar;
    }

    public JMenuItem getPmniExpandSB() {
        return pmniExpandSB;
    }

    public JPopupMenu getPmnSearchBar() {

        return pmnSearchBar;
    }

    // get the model after filtered for dictionary list
    //! not using optimized algorithm
    private DefaultListModel<String> getFilteredModel(String filter) {

        DefaultListModel<String> filteredModel = new DefaultListModel<>();
        ArrayList<String> keysList = currentDic.getKeys();

        if (filter.isEmpty()) {
            for (String s : keysList) {
                filteredModel.addElement(s);
            }
            return filteredModel;
        }

        if (currentDic instanceof RecentWords) {
            Collections.sort(keysList);
        }

        int matchedIndex = binarySearch(filter, keysList);

        if (matchedIndex != -1) {
            String matchedKey = keysList.get(matchedIndex);

            while (matchedKey.startsWith(filter)) {
                filteredModel.addElement(matchedKey);
                if (++matchedIndex >= keysList.size()) {
                    break;
                }
                matchedKey = keysList.get(matchedIndex);
            }
        }
        return filteredModel;
    }

    public static int binarySearch(String s, ArrayList<String> k){
        /**
         * d là đầu
         * c là cuối
         * g là giữa
         */
        int d = 0;
        int c = k.size();
        while (d<c-1){
            int g = (c+d)/2;
            if (k.get(g).compareTo(s)>=0) c=g; else d=g;
        }
        if (c == k.size()) return -1;
        return c;
    }

    public void showErrorCon() {
        javax.swing.JOptionPane.showMessageDialog(null,
                "You're not being connected to the internet",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void setLookAndFeel(String lookAndFeel) {
        try {
            if (lookAndFeel.equals("Nimbus")) {
                UIManager.setLookAndFeel(new NimbusLookAndFeel());

            } else if (lookAndFeel.equals("Windows")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            } else if (lookAndFeel.equals("Synthetica")) {
                UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());

            }

        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initGUIComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pmnDefPane = new javax.swing.JPopupMenu();
        pmniSearchDP = new javax.swing.JMenuItem();
        pmniSpeakDP = new javax.swing.JMenuItem();
        pmniCopyDP = new javax.swing.JMenuItem();
        pmniWikitionaryDefDP = new javax.swing.JMenuItem();
        pmnOnlTransDP = new javax.swing.JMenu();
        pmniGoogleTranslateDP = new javax.swing.JMenuItem();
        pmniMicrosoftTranslatorDP = new javax.swing.JMenuItem();
        pmniTranscribeDP = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        pmnAddWordDP = new javax.swing.JMenu();
        pmniAddWordEVDP = new javax.swing.JMenuItem();
        pmniAddWordVEDP = new javax.swing.JMenuItem();
        pmniModifyDP = new javax.swing.JMenuItem();
        pmniRemoveDP = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        pmniSelectAllDP = new javax.swing.JMenuItem();
        pmniSwapLangForOnlTransDP = new javax.swing.JMenuItem();
        pmnSearchBar = new javax.swing.JPopupMenu();
        pmniCopySB = new javax.swing.JMenuItem();
        pmniPasteSB = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        pmniExpandSB = new javax.swing.JMenuItem();
        pmnListDic = new javax.swing.JPopupMenu();
        pmnAddWordLD = new javax.swing.JMenu();
        pmniAddWordEVLD = new javax.swing.JMenuItem();
        pmniAddWordVELD = new javax.swing.JMenuItem();
        pmniModifyLD = new javax.swing.JMenuItem();
        pmniRemoveLD = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        pmniRemoveAllLD = new javax.swing.JMenuItem();
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
        lbSpeaker = new javax.swing.JLabel();
        lbRecorder = new javax.swing.JLabel();
        lbBack = new javax.swing.JLabel();
        southBigPanel = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        scrollPaneForList = new javax.swing.JScrollPane();
        listDictionary = new javax.swing.JList();
        scrollPaneForDefinition = new javax.swing.JScrollPane();
        epDefinition = new javax.swing.JEditorPane();
        lbCopyright = new javax.swing.JLabel();
        lbHeart = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        mnFile = new javax.swing.JMenu();
        mniUpdateDictionary = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mniExit = new javax.swing.JMenuItem();
        mnEdit = new javax.swing.JMenu();
        mnAddWord = new javax.swing.JMenu();
        mniAddWordEV = new javax.swing.JMenuItem();
        mniAddWordVE = new javax.swing.JMenuItem();
        mniModifyWord = new javax.swing.JMenuItem();
        mniRemoveWord = new javax.swing.JMenuItem();
        mnUtilities = new javax.swing.JMenu();
        mniWikitionaryDef = new javax.swing.JMenuItem();
        mnOnlTrans = new javax.swing.JMenu();
        mniGoogleTranslate = new javax.swing.JMenuItem();
        mniMicrosoftTranslator = new javax.swing.JMenuItem();
        mniTranscribe = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mniSTT = new javax.swing.JMenuItem();
        mnTTS = new javax.swing.JMenu();
        rbmniFreeTTS = new javax.swing.JRadioButtonMenuItem();
        rbmniMTTS = new javax.swing.JRadioButtonMenuItem();
        mniITT = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        mniCaptureScreen = new javax.swing.JMenuItem();
        mnChangeTheme = new javax.swing.JMenu();
        mniNimbus = new javax.swing.JMenuItem();
        mniSynthetica = new javax.swing.JMenuItem();
        mniSystemTheme = new javax.swing.JMenuItem();
        mnHelp = new javax.swing.JMenu();
        mniHelp = new javax.swing.JMenuItem();
        mniRegister = new javax.swing.JMenuItem();
        mniAboutUs = new javax.swing.JMenuItem();

        pmniSearchDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK));
        pmniSearchDP.setText("Search");
        pmnDefPane.add(pmniSearchDP);

        pmniSpeakDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pmniSpeakDP.setText("Speak");
        pmnDefPane.add(pmniSpeakDP);

        pmniCopyDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        pmniCopyDP.setText("Copy");
        pmnDefPane.add(pmniCopyDP);

        pmniWikitionaryDefDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.SHIFT_MASK));
        pmniWikitionaryDefDP.setText("Wikitionary Definition");
        pmnDefPane.add(pmniWikitionaryDefDP);

        pmnOnlTransDP.setText("Online Translators");

        pmniGoogleTranslateDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_MASK));
        pmniGoogleTranslateDP.setText("Google Translate");
        pmnOnlTransDP.add(pmniGoogleTranslateDP);

        pmniMicrosoftTranslatorDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK));
        pmniMicrosoftTranslatorDP.setText("Microsoft Translator");
        pmnOnlTransDP.add(pmniMicrosoftTranslatorDP);

        pmnDefPane.add(pmnOnlTransDP);

        pmniTranscribeDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_MASK));
        pmniTranscribeDP.setText("Transcribe");
        pmnDefPane.add(pmniTranscribeDP);
        pmnDefPane.add(jSeparator5);

        pmnAddWordDP.setText("Add this word...");

        pmniAddWordEVDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK));
        pmniAddWordEVDP.setText("to EV Dic");
        pmnAddWordDP.add(pmniAddWordEVDP);

        pmniAddWordVEDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_MASK));
        pmniAddWordVEDP.setText("to VE Dic");
        pmnAddWordDP.add(pmniAddWordVEDP);

        pmnDefPane.add(pmnAddWordDP);

        pmniModifyDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pmniModifyDP.setText("Modify");
        pmnDefPane.add(pmniModifyDP);

        pmniRemoveDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pmniRemoveDP.setText("Remove");
        pmnDefPane.add(pmniRemoveDP);
        pmnDefPane.add(jSeparator6);

        pmniSelectAllDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        pmniSelectAllDP.setText("Select All");
        pmnDefPane.add(pmniSelectAllDP);

        pmniSwapLangForOnlTransDP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.CTRL_MASK));
        //pmniSwapLangForOnlTransDP.setText("Online Trans: E > V");
        pmnDefPane.add(pmniSwapLangForOnlTransDP);

        pmniCopySB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        pmniCopySB.setText("Copy");
        pmnSearchBar.add(pmniCopySB);

        pmniPasteSB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pmniPasteSB.setText("Paste");
        pmnSearchBar.add(pmniPasteSB);
        pmnSearchBar.add(jSeparator8);

        pmniExpandSB.setText("Expand Search Bar");
        pmnSearchBar.add(pmniExpandSB);

        pmnAddWordLD.setText("Add word...");

        pmniAddWordEVLD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pmniAddWordEVLD.setText("to EV Dic");
        pmniAddWordEVLD.setToolTipText("");
        pmnAddWordLD.add(pmniAddWordEVLD);

        pmniAddWordVELD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pmniAddWordVELD.setText("to VE Dic");
        pmnAddWordLD.add(pmniAddWordVELD);

        pmnListDic.add(pmnAddWordLD);

        pmniModifyLD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pmniModifyLD.setText("Modify");
        pmnListDic.add(pmniModifyLD);

        pmniRemoveLD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pmniRemoveLD.setText("Remove");
        pmnListDic.add(pmniRemoveLD);
        pmnListDic.add(jSeparator7);

        pmniRemoveAllLD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pmniRemoveAllLD.setText("Remove all");
        pmnListDic.add(pmniRemoveAllLD);

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

        searchBarPanel.setAutoscrolls(true);
        searchBarPanel.setPreferredSize(new java.awt.Dimension(800, 32));

        tfSearchBar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tfSearchBar.setForeground(new java.awt.Color(51, 51, 255));
        tfSearchBar.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tfSearchBar.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        tfSearchBar.setName(""); // NOI18N

        lbSpeaker.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbSpeaker.setToolTipText("");
        lbSpeaker.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        lbRecorder.setToolTipText("");

        lbBack.setToolTipText("");

        javax.swing.GroupLayout searchBarPanelLayout = new javax.swing.GroupLayout(searchBarPanel);
        searchBarPanel.setLayout(searchBarPanelLayout);
        searchBarPanelLayout.setHorizontalGroup(
                searchBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(searchBarPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbBack, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 661, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbSpeaker, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbRecorder, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        searchBarPanelLayout.setVerticalGroup(
                searchBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbSpeaker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbRecorder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(searchBarPanelLayout.createSequentialGroup()
                                .addGroup(searchBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(tfSearchBar, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                                        .addComponent(lbBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        northBigPanel.add(searchBarPanel, java.awt.BorderLayout.SOUTH);

        rootPanel.add(northBigPanel, java.awt.BorderLayout.NORTH);

        listDictionary.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i]; }
        });
        scrollPaneForList.setViewportView(listDictionary);

        splitPane.setLeftComponent(scrollPaneForList);

        scrollPaneForDefinition.setViewportView(epDefinition);

        splitPane.setRightComponent(scrollPaneForDefinition);

        lbCopyright.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lbCopyright.setForeground(new java.awt.Color(255, 0, 0));
        lbCopyright.setText("Made with");

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("by DailyHeroes");

        javax.swing.GroupLayout southBigPanelLayout = new javax.swing.GroupLayout(southBigPanel);
        southBigPanel.setLayout(southBigPanelLayout);
        southBigPanelLayout.setHorizontalGroup(
                southBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(southBigPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(southBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                                        .addGroup(southBigPanelLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(lbCopyright, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lbHeart, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        southBigPanelLayout.setVerticalGroup(
                southBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(southBigPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                                .addGap(2, 2, 2)
                                .addGroup(southBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(southBigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lbCopyright, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lbHeart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jLabel1)))
        );

        rootPanel.add(southBigPanel, java.awt.BorderLayout.CENTER);

        mnFile.setMnemonic('F');
        mnFile.setText("File");

        mniUpdateDictionary.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniUpdateDictionary.setText("Update dictionary");
        mnFile.add(mniUpdateDictionary);
        mnFile.add(jSeparator4);

        mniExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        mniExit.setText("Exit");
        mniExit.setToolTipText("");
        mnFile.add(mniExit);

        menuBar.add(mnFile);

        mnEdit.setMnemonic('E');
        mnEdit.setText("Edit");
        mnEdit.setToolTipText("");

        mnAddWord.setText("Add word...");

        mniAddWordEV.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniAddWordEV.setText("to EV Dictionary");
        mnAddWord.add(mniAddWordEV);

        mniAddWordVE.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniAddWordVE.setText("to VE Dictionary");
        mnAddWord.add(mniAddWordVE);

        mnEdit.add(mnAddWord);

        mniModifyWord.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniModifyWord.setText("Modify Word");
        mniModifyWord.setToolTipText("");
        mnEdit.add(mniModifyWord);

        mniRemoveWord.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniRemoveWord.setText("Remove Word");
        mnEdit.add(mniRemoveWord);

        menuBar.add(mnEdit);

        mnUtilities.setMnemonic('U');
        mnUtilities.setText("Utilities");
        mnUtilities.setToolTipText("");

        mniWikitionaryDef.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniWikitionaryDef.setText("Wikitionary definition");
        mnUtilities.add(mniWikitionaryDef);

        mnOnlTrans.setText("Online Translator");

        mniGoogleTranslate.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniGoogleTranslate.setText("Google Translate");
        mnOnlTrans.add(mniGoogleTranslate);

        mniMicrosoftTranslator.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniMicrosoftTranslator.setText("Microsoft Translator");
        mnOnlTrans.add(mniMicrosoftTranslator);

        mnUtilities.add(mnOnlTrans);

        mniTranscribe.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniTranscribe.setText("Transcribe to IPA");
        mnUtilities.add(mniTranscribe);
        mnUtilities.add(jSeparator3);

        mniSTT.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
        mniSTT.setText("Speech to text");
        mnUtilities.add(mniSTT);

        mnTTS.setText("Text to speech");

        btgOnlTrans.add(rbmniFreeTTS);
        rbmniFreeTTS.setSelected(true);
        rbmniFreeTTS.setText("FreeTTS (System)");
        mnTTS.add(rbmniFreeTTS);

        btgOnlTrans.add(rbmniMTTS);
        rbmniMTTS.setText("MTTS (Microsoft)");
        mnTTS.add(rbmniMTTS);

        mnUtilities.add(mnTTS);

        mniITT.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniITT.setText("Image to text");
        mnUtilities.add(mniITT);
        mnUtilities.add(jSeparator9);

        mniCaptureScreen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mniCaptureScreen.setText("Capture screen");
        mnUtilities.add(mniCaptureScreen);

        mnChangeTheme.setText("Change theme");

        mniNimbus.setText("Nimbus");
        mnChangeTheme.add(mniNimbus);

        mniSynthetica.setText("Synthetica");
        mnChangeTheme.add(mniSynthetica);

        mniSystemTheme.setText("System theme");
        mnChangeTheme.add(mniSystemTheme);

        mnUtilities.add(mnChangeTheme);

        menuBar.add(mnUtilities);

        mnHelp.setMnemonic('H');
        mnHelp.setText("Help");
        mnHelp.setToolTipText("");

        mniHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mniHelp.setText("Help");
        mnHelp.add(mniHelp);

        mniRegister.setText("Register");
        mnHelp.add(mniRegister);

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


        //!!!!! IMPORTANT
        lbSpeaker.setIcon(new ImageIcon("./img/speaker.png"));
        lbHeart.setIcon(new ImageIcon("./img/heart.png"));
        lbRecorder.setIcon(new ImageIcon("./img/recorder.png"));
        lbBack.setIcon(new ImageIcon("./img/back.png"));

        // set HTML-displayable ability for DefinitionEditPane
        epDefinition.setContentType("text/html");
        epDefinition.setEditable(false);
        // set popup menu for Definition Pane
        epDefinition.setComponentPopupMenu(pmnDefPane);
        // for Words List
        listDictionary.setComponentPopupMenu(pmnListDic);
        // for search bar
        tfSearchBar.setComponentPopupMenu(pmnSearchBar);

        // set weight rate for component in splitPane
        splitPane.setResizeWeight(0.15);

        // with trial version, we do not supply these feature
        pmnOnlTransDP.setEnabled(false);

        mnOnlTrans.setEnabled(false);
        rbmniMTTS.setEnabled(false);
        mniSTT.setEnabled(false);
        mniITT.setEnabled(false);
        mniSynthetica.setEnabled(false);

        lbRecorder.setEnabled(false);


        pack();
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel dicSwitchingPanel;
    private javax.swing.JEditorPane epDefinition;
    private javax.swing.JPanel firstTopPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JLabel lbBack;
    private javax.swing.JLabel lbCopyright;
    private javax.swing.JLabel lbHeart;
    private javax.swing.JLabel lbRecorder;
    private javax.swing.JLabel lbSpeaker;
    private javax.swing.JList listDictionary;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu mnAddWord;
    private javax.swing.JMenu mnChangeTheme;
    private javax.swing.JMenu mnEdit;
    private javax.swing.JMenu mnFile;
    private javax.swing.JMenu mnHelp;
    private javax.swing.JMenu mnOnlTrans;
    private javax.swing.JMenu mnTTS;
    private javax.swing.JMenu mnUtilities;
    private javax.swing.JMenuItem mniAboutUs;
    private javax.swing.JMenuItem mniAddWordEV;
    private javax.swing.JMenuItem mniAddWordVE;
    private javax.swing.JMenuItem mniCaptureScreen;
    private javax.swing.JMenuItem mniExit;
    private javax.swing.JMenuItem mniGoogleTranslate;
    private javax.swing.JMenuItem mniHelp;
    private javax.swing.JMenuItem mniITT;
    private javax.swing.JMenuItem mniMicrosoftTranslator;
    private javax.swing.JMenuItem mniModifyWord;
    private javax.swing.JMenuItem mniNimbus;
    private javax.swing.JMenuItem mniRegister;
    private javax.swing.JMenuItem mniRemoveWord;
    private javax.swing.JMenuItem mniSTT;
    private javax.swing.JMenuItem mniSynthetica;
    private javax.swing.JMenuItem mniSystemTheme;
    private javax.swing.JMenuItem mniTranscribe;
    private javax.swing.JMenuItem mniUpdateDictionary;
    private javax.swing.JMenuItem mniWikitionaryDef;
    private javax.swing.JPanel northBigPanel;
    private javax.swing.JMenu pmnAddWordDP;
    private javax.swing.JMenu pmnAddWordLD;
    private javax.swing.JPopupMenu pmnDefPane;
    private javax.swing.JPopupMenu pmnListDic;
    private javax.swing.JMenu pmnOnlTransDP;
    private javax.swing.JPopupMenu pmnSearchBar;
    private javax.swing.JMenuItem pmniAddWordEVDP;
    private javax.swing.JMenuItem pmniAddWordEVLD;
    private javax.swing.JMenuItem pmniAddWordVEDP;
    private javax.swing.JMenuItem pmniAddWordVELD;
    private javax.swing.JMenuItem pmniCopyDP;
    private javax.swing.JMenuItem pmniCopySB;
    private javax.swing.JMenuItem pmniExpandSB;
    private javax.swing.JMenuItem pmniGoogleTranslateDP;
    private javax.swing.JMenuItem pmniMicrosoftTranslatorDP;
    private javax.swing.JMenuItem pmniModifyDP;
    private javax.swing.JMenuItem pmniModifyLD;
    private javax.swing.JMenuItem pmniPasteSB;
    private javax.swing.JMenuItem pmniRemoveAllLD;
    private javax.swing.JMenuItem pmniRemoveDP;
    private javax.swing.JMenuItem pmniRemoveLD;
    private javax.swing.JMenuItem pmniSearchDP;
    private javax.swing.JMenuItem pmniSelectAllDP;
    private javax.swing.JMenuItem pmniSpeakDP;
    private javax.swing.JMenuItem pmniSwapLangForOnlTransDP;
    private javax.swing.JMenuItem pmniTranscribeDP;
    private javax.swing.JMenuItem pmniWikitionaryDefDP;
    private javax.swing.JRadioButtonMenuItem rbmniFreeTTS;
    private javax.swing.JRadioButtonMenuItem rbmniMTTS;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JScrollPane scrollPaneForDefinition;
    private javax.swing.JScrollPane scrollPaneForList;
    private javax.swing.JPanel searchBarPanel;
    private javax.swing.JPanel separatorPanel1;
    private javax.swing.JPanel separatorPanel2;
    private javax.swing.JPanel southBigPanel;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField tfSearchBar;
    private javax.swing.JToggleButton tgbtEnVi;
    private javax.swing.JToggleButton tgbtViEn;
    // End of variables declaration
    private javax.swing.ButtonGroup btgOnlTrans = new javax.swing.ButtonGroup();
}

