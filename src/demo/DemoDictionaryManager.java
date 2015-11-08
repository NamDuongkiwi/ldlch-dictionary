package demo;

import database.DicAbstract;
import database.DicDatabase;

/**
 * Created by cuong on 10/21/2015.
 */
public class DemoDictionaryManager {

    public static void main(String args[]) {

        DicDatabase database = new DicDatabase();
        DicAbstract dic = database.getDictionary(2);

        System.out.println(dic.getKeys());

        dic.removeWord("legend");
//        new WriteDataTask().writeData(dic, "./data/E_V.zip");

        System.out.println(dic.getKeys());
//        System.out.println(dic.getData().get("heaven"));
//
//        manager.modifyWord("heaven", null, "Thiên đường");
//        System.out.println(dic.getKeys());
//        System.out.println(dic.getData().get("heaven"));
//
//        manager.removeWord("heaven");
//        System.out.println(dic.getKeys());

    }

}
