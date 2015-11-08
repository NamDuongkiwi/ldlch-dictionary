package backend.tts;

import backend.util.R;
import com.memetix.mst.language.SpokenDialect;
import com.memetix.mst.speak.Speak;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cuong on 10/30/2015.
 */
public class MTTS {

    private final static MTTS instance = new MTTS();
    private MTTS() {}
    public static MTTS getIntance() { return instance; }

    public void execute(String text) {
        try {
            // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
            Speak.setClientId(R.MICROSOFT_TRANS_TTS_CLIENT_ID);
            Speak.setClientSecret(R.MICROSOFT_TRANS_TTS_CLIENT_SECRET);
            // Calls the speak service with text to be spoken and specifies the dialect in which to speak it
            String sWavUrl = Speak.execute(text, SpokenDialect.ENGLISH_UNITED_STATES);

            //Print the URL returned from the Speak service
            //System.out.println(sWavUrl);

            // Now, makes an HTTP Connection to get the InputStream
            final URL waveUrl = new URL(sWavUrl);
            HttpURLConnection con = (HttpURLConnection) waveUrl.openConnection();

            // set details for Connection
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Pass the input stream to the playClip method
            playClip(con.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static void playClip(InputStream is) throws Exception {
        class AudioListener implements LineListener {
            private boolean done = false;

            public synchronized void update(LineEvent event) {
                LineEvent.Type eventType = event.getType();
                if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                    done = true;
                    notifyAll();
                }
            }
            public synchronized void waitUntilDone() throws InterruptedException {
                while (!done) { wait(); }
            }
        }

        AudioListener listener = new AudioListener();

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                new BufferedInputStream(is));

        try {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(listener);
            clip.open(audioInputStream);
            try {
                clip.start();
                listener.waitUntilDone();
            } finally {
                clip.close();
            }
        } finally {
            audioInputStream.close();
        }
    }
}
