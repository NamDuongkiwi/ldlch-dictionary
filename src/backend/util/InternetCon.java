package backend.util;

import java.io.IOException;

/**
 * Created by cuong on 10/26/2015.
 */
public class InternetCon {

    private static InternetCon ourInstance = new InternetCon();

    public static InternetCon getInstance() {
        return ourInstance;
    }

    private InternetCon() {

    }

    public boolean isConnected() {
        try {
            // try to ping to google to know
            // if you are connected with internet (google almost works anytime)
            Process p = Runtime.getRuntime().exec("ping www.google.com");
            return (p.waitFor() == 0);

        } catch (IOException e) {
            e.printStackTrace();
            return false;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
