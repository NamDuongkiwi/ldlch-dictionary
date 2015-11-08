package backend.translator;

import backend.util.R;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * Created by cuong on 10/30/2015.
 */
public class MTranslator {

    private final static MTranslator instance = new MTranslator();
    private MTranslator() {}
    public static MTranslator getInstance() { return instance; }

    public String execute(String srcText, String srcLang, String desLang) {
        Translate.setClientId(R.MICROSOFT_TRANS_TTS_CLIENT_ID);
        Translate.setClientSecret(R.MICROSOFT_TRANS_TTS_CLIENT_SECRET);

        try {
            return Translate.execute(srcText, Language.fromString(srcLang),
                    Language.fromString(desLang));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
