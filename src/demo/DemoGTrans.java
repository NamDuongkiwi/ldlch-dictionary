package demo;

import backend.translator.GTranslator;

/**
 * Created by cuong on 11/1/2015.
 */
public class DemoGTrans {
    public static void main(String args[]) {
        String res = GTranslator.getInstance().translate_a("I love you", "en", "vi");
        System.out.println(res);
    }
}
