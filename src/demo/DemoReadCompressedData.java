package demo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by cuong on 10/16/2015.
 */
public class DemoReadCompressedData {

    public static void main(String args[]) {
        ReadDataTask readDataTask = new ReadDataTask();

        HashMap<String, String> data =  readDataTask.readData();
        ArrayList<String> keys = readDataTask.getKeys();
        DisplayData frame = new DisplayData(data, keys);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


class DisplayData extends JFrame {

    private JPanel panel;
    private JList list;
    private JScrollPane scrollPane;
    private HashMap<String, String> data;

    public DisplayData(HashMap<String, String> data, ArrayList<String> keys) {

        this.setTitle("Demo Display data to JList");
        this.setSize(480, 640);

        panel = new JPanel();

        this.data = data;
        list = new JList(keys.toArray());
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(570, 800));
        scrollPane.setViewportView(list);

        this.add(panel);
        panel.add(scrollPane);

        list.addListSelectionListener(new ListListener());
        list.addMouseListener(new ListListener());

        this.pack();
    }

    class ListListener implements ListSelectionListener, MouseListener {

        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            //String selected = list.getSelectedValue().toString();
            //new DisplayDefinitionTask(data.get(selected));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                String selectedKey = list.getSelectedValue().toString();
                //String def = "";
                new DisplayDefinitionTask(data.get(selectedKey));
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
    }

}

class DisplayDefinitionTask extends JFrame {

    JLabel lbDefin;

    public DisplayDefinitionTask(String defin) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        lbDefin = new JLabel(defin);
        this.add(lbDefin);
        this.pack();
    }

}

class ReadDataTask {

    private ArrayList<String> data = new ArrayList<>();
    private HashMap<String, String> data2 = new HashMap<>();
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> defs = new ArrayList<>();



    public HashMap<String, String> readData() {

        FileInputStream file = null;
        ZipInputStream zipStream = null;
        ZipEntry entry = null;
        BufferedReader reader = null;

        try {
            file = new FileInputStream("./data/E_V.zip");
            zipStream = new ZipInputStream(file);
            entry = zipStream.getNextEntry();

            reader = new BufferedReader(new InputStreamReader(zipStream));

            String line;
            int wordsNum = 0;
            while ((line = reader.readLine()) != null) {
                //System.out.printf("%s\n----------------------\n", line);
                int index = line.indexOf("<i>");

                if (index != -1) {
                    String word = line.substring(0, index);
                    word.trim();
                    String def = line.substring(index);
                    def = "<html>" + def + "</html>";
                    data2.put(word, def);
                    keys.add(word);
                    wordsNum++;
                }
            }

            System.out.println(wordsNum + " words");


        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return data2;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }

    public ArrayList<String> getDefs() {
        return defs;
    }

    public void setDefs(ArrayList<String> defs) {
        this.defs = defs;
    }

    public ArrayList<String> readData2() {

        FileInputStream file = null;
        ZipInputStream zipStream = null;
        ZipEntry entry = null;
        BufferedReader reader = null;

        try {
            file = new FileInputStream("./data/V_E.zip");
            zipStream = new ZipInputStream(file);
            entry = zipStream.getNextEntry();

            reader = new BufferedReader(new InputStreamReader(zipStream));

            String line;
            int wordsNum = 0;
            while ((line = reader.readLine()) != null) {
                //System.out.printf("%s\n----------------------\n", line);
                int first = line.indexOf("<i>");
                int last = line.indexOf("</i>");

                if (first != -1 && last > first) {
                    String word = line.substring(first+3, last);
                    word.trim();
                    data.add(word);
                    wordsNum++;
                }
            }

            System.out.println(wordsNum + " words");


        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

        return data;
    }
}
