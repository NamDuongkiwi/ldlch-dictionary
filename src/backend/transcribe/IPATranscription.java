package backend.transcribe;

import backend.http.header.postrequest.HttpPostRequest;
import backend.util.R;

/**
 * Created by cuong on 10/27/2015.
 */
public class IPATranscription {

    private static final IPATranscription instance = new IPATranscription();

    private IPATranscription() {}
    public static synchronized IPATranscription getInstance() { return instance; }

    public synchronized String getResult(String originText) {

        System.out.println("\nTranscribing to IPA Phonetic: " + originText);

        String rawData = HttpPostRequest.getInstance().sendPost(
                R.IPA_URL,
                String.format(R.IPA_PARAMS, originText.replace(" ", "+")));

        System.out.println("Result after parsing response: " + (rawData = parseData(rawData)));
        return rawData;
    }

    private static String parseData(String rawData) {
        int start = rawData.indexOf("<div id=\"transcr_output\">");
        int end = rawData.indexOf("<div id=\"multi_ipa_notes\">");

        String res = "";

        if (start != -1 && end != -1) {
            rawData = rawData.substring(start, end);

            end = start = 0;
            while (true) {
                start = rawData.indexOf("<span class=\"transcribed_word\">", end+1);
                end = rawData.indexOf("</span>", start+1);
                if (start == -1 || end == -1) { break; }
                res += rawData.substring(start + 31, end) + " ";
            }

            return res;
        }

        return null;
    }
}
