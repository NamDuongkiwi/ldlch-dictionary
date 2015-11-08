package backend.tts;
import com.sun.speech.freetts.*;

/**
 *
 * @author cuong
 */
public class WinTTS {

    private final static WinTTS instance = new WinTTS();
    private final static String VOICE_NAME = "kevin";
    private WinTTS() {}

    public final static WinTTS getInstance() {
        return instance;
    }

    public static void speak(String text) {

        VoiceManager manager = VoiceManager.getInstance();
        Voice voice = manager.getVoice(VOICE_NAME);

        voice.allocate();
        try {
            voice.speak(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
