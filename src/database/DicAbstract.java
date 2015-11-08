package database;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cuong on 10/24/2015.
 */
public abstract class DicAbstract {
    protected String name;
    protected String path; // path to file which stores
    protected boolean isModified = false;

    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }

    public boolean isModified() {

        return isModified;
    }

    protected HashMap<String, String> data;
    protected ArrayList<String> keys;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }

    public abstract void readData();
    public abstract void update(); // update data on disk
    public abstract void addWord(String word,String definition);
    public abstract void modifyWord(String word, String newWord, String newDefinition);
    public abstract void removeWord(String word);
    public abstract void removeAll();

}
