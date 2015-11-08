package demo;
import com.memetix.mst.language.SpokenDialect;
import com.memetix.mst.speak.Speak;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

/**
 * Created by cuong on 10/29/2015.
 */
public class DemoMTTS {
    public static final String CLIENT_ID
            = "UET_Translator";
    public static final String CLIENT_SECRET
            = "9NThF1QmwHjKniNo1mSkjOwZO6vN+KcbASXkWHpq+9Q=";

    public static void main( String[] args ) throws Exception
    {
        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Speak.setClientId(CLIENT_ID);
        Speak.setClientSecret(CLIENT_SECRET);
        // Calls the speak service with text to be spoken and specifies the dialect in which to speak it
        String sWavUrl = Speak.execute("I love the University of Engineering and Technology",
                SpokenDialect.ENGLISH_UNITED_STATES);

        //Print the URL returned from the Speak service
        System.out.println(sWavUrl);

        // Now, makes an HTTP Connection to get the InputStream
        final URL waveUrl = new URL(sWavUrl);
        HttpURLConnection con = (HttpURLConnection) waveUrl.openConnection();

        // set details for Connection
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        // Pass the input stream to the playClip method
        playClip(con.getInputStream());
    }

    private static void playClip(InputStream is) throws Exception {
        class AudioListener implements LineListener {
            private boolean done = false;

            public synchronized void update(LineEvent event) {
                Type eventType = event.getType();
                if (eventType == Type.STOP || eventType == Type.CLOSE) {
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
