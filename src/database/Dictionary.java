package database;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by linh on 10/20/2015.
 */
public class Dictionary extends DicAbstract {

    public Dictionary() {
        data = new HashMap<>();
        keys = new ArrayList<>();
    }

    @Override
    public void readData() {

        FileInputStream file = null;
        ZipInputStream zipStream = null;
        ZipEntry entry = null;
        BufferedReader reader = null;

        try {
            file = new FileInputStream(path);
            zipStream = new ZipInputStream(file);
            entry = zipStream.getNextEntry();

            reader = new BufferedReader(new InputStreamReader(zipStream));

            String line, word, def;
            int wordsNum = 0;
            while ((line = reader.readLine()) != null) {
                //System.out.printf("%s\n----------------------\n", line);
                int index = line.indexOf("<html>");
                int index2 = line.indexOf("<ul>");

                if (index2 != -1 && index > index2) {
                    index = index2;
                }

                if (index != -1) {
                    word = line.substring(0, index);

                    word = word.trim();
                    keys.add(word);

                    //word = word.toLowerCase();
                    def = line.substring(index);
                    //def = "<html>" + def + "</html>";

                    data.put(word, def);

                    wordsNum++;
                }
            }
            reader.close();

            System.out.println(path + ": " + wordsNum + " words");


        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

        Collections.sort(keys);

    }

    @Override
    public void update() {
        setModified(false);

        FileOutputStream file = null;
        ZipOutputStream zipStream = null;
        BufferedWriter writer = null;

        try {
            file = new FileOutputStream(path);
            zipStream = new ZipOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(zipStream));
            zipStream.putNextEntry(new ZipEntry(path.replace("./data/", "").replace("zip", "txt")));

            for (String key : keys) {
                writer.write(key);
                String def = data.get(key);
                if (def != null) {
                    writer.write(data.get(key));
                }

                writer.newLine();
            }

            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int binarySearch(String w, ArrayList<String> k){
        if (k.get(0).compareTo(w)>=0) return 0;
        int d = 0, c = k.size();
        while (d<c-1){
            int g = (d+c)/2;
            if (k.get(g).compareTo(w)<0) d=g;else c=g;
        }
        return c;
    }

    public void addWord(String word,String definition){
        word = word.toLowerCase();
        data.put(word, definition);
        int n = binarySearch(word,keys);
        keys.add(n, word);
    }

//    public void addWord(String word, String definition) {
//        data.put(word, definition);
//        int n = keys.size();
//        //System.out.println(n);
//        while (n>=1 && keys.get(n-1).compareToIgnoreCase(word) > 0){
//            n--;
//        }
//        //System.out.println(n);
//        keys.add(n, word);
//    }

    /**
     * newWord == null -> không thay từ
     * newDefinition == null -> không thay nghĩa
     */
    public void modifyWord(String word, String newWord, String newDefinition) {
        if (newWord == null){
            data.replace(word, newDefinition);
        } else
        if (newDefinition == null){
            String defin = new String();
            defin = data.get(word);
            removeWord(word);
            addWord(newWord, defin);
        } else{
            removeWord(word);
            addWord(newWord,newDefinition);
        }
    }

    public void removeWord(String word) {
        int n = keys.lastIndexOf(word);
        if (n>=0 && n<=keys.size()-1){
            keys.remove(n);
            data.remove(word);
        }
    }

    @Override
    public void removeAll() {

    }
}
