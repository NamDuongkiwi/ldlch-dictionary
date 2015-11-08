package demo;

import database.DicDatabase;

/**
 * Created by cuong on 10/20/2015.
 */
public class DemoDisplayDefinition {

    public static void main(String args[]) {
        DicDatabase database = new DicDatabase();
        System.out.println(database.getDictionary(2).getData().get("love"));
    }

}
