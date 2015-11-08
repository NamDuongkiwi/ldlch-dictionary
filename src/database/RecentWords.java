package database;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by cuong on 10/24/2015.
 */
public class RecentWords extends DicAbstract {

    public RecentWords() {
        keys = new ArrayList<>();
    }

    @Override
    public void readData() {
        File file = null;
        BufferedReader br = null;
        FileInputStream fileStream = null;

        file = new File(path);
        try {
            fileStream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fileStream));

            String word;
            int numWord = 0;
            while ((word = br.readLine()) != null) {
                keys.add(word);
                numWord++;
            }
            br.close();

            System.out.println(path + ": " + numWord + " words");

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void update() {
        setModified(false);

        FileOutputStream fileStream = null;
        BufferedWriter writer = null;

        try {
            fileStream = new FileOutputStream(path);
            writer = new BufferedWriter(new OutputStreamWriter(fileStream));

            for (String key : keys) {
                writer.write(key);
                writer.newLine();
            }

            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addWord(String word, String definition) {}

    public void addWord(String word) {
        if (!keys.contains(word)) { // word doesn't exist in recent words list
            keys.add(0, word); // add to recent word

        } else { // move the latest word to the first
            int index = keys.indexOf(word);
            keys.remove(index);
            keys.add(0, word);
        }

        this.update();
    }

    @Override
    public void modifyWord(String word, String newWord, String newDefinition) {}

    @Override
    public void removeWord(String word) {
        int n = keys.lastIndexOf(word);
        if (n>=0 && n<=keys.size()-1){
            keys.remove(n);
        }

        this.update();
    }

    @Override
    public void removeAll() {
        keys = new ArrayList<>();
    }
}
