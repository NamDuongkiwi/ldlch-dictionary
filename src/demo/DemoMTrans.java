package demo;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * Created by cuong on 10/29/2015.
 */
public class DemoMTrans {

    public static final String CLIENT_ID
            = "UET_Translator";
    public static final String CLIENT_SECRET
            = "9NThF1QmwHjKniNo1mSkjOwZO6vN+KcbASXkWHpq+9Q=";

    public static void main(String args[]) {
        Translate.setClientId(CLIENT_ID);
        Translate.setClientSecret(CLIENT_SECRET);

        try {
            System.out.println(Translate.execute("I miss you so much, I wanna kiss you",
                    Language.AUTO_DETECT, Language.VIETNAMESE));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
