package demo;

import database.DicAbstract;
import database.DicDatabase;
import frames.ModifyDictionaryFrame;

/**
 * Created by cuong on 10/21/2015.
 */
public class DemoModifyDicFrame {

    public static void main(String args[]) {

        DicDatabase database = new DicDatabase();
        DicAbstract dic = database.getDictionary(2);

        System.out.println(dic.getKeys());

        ModifyDictionaryFrame frame;// = new ModifyDictionaryFrame(dic, 1); // add new word

        frame = new ModifyDictionaryFrame(dic, 2);
//        System.out.println(dic.getData().get("legend"));

//        frame = new ModifyDictionaryFrame(dic, ModifyDictionaryFrame.REMOVE_WORD, "legend");
    }

}
