package backend.speech.recognition;

import backend.util.R;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dinht_000 on 10/28/2015.
 */
public class IBMSpeechRecognition {

    public final static IBMSpeechRecognition instance = new IBMSpeechRecognition();
    public static IBMSpeechRecognition getInstance() { return instance; }
    private IBMSpeechRecognition() {}

    public ArrayList<String> sendPost(String lang, String audioPath) {

        SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword(R.IBM_STT_USER, R.IBM_STT_PASSWD);

        File audio = new File(audioPath);
        Map params = new HashMap();
        params.put("audio", audio);
        params.put("content_type", "audio/wav; rate=48000");
        params.put("word_confidence", false);
        params.put("continuous", false);
        params.put("timestamps", false);
        params.put("inactivity_timeout", 30);
        params.put("max_alternatives", 5);

        System.out.println("\nIBM speech recoginition v1: ");
        System.out.println("Audio Language: " + lang);
        System.out.println("Audio path: " + audioPath);
        System.out.println("Sending request...");

        SpeechResults transcript = service.recognize(params);

        System.out.println("Done sending request");
        String outputData = transcript.toString();

        System.out.println("Response data:\n" + outputData.replace("\n", ""));

        return getResultFromJson(outputData);
    }

    public ArrayList<String> getResultFromJson(String raw) {
        ArrayList<String> arrResult = new ArrayList<>();

        try {

            JSONObject rawObj = (JSONObject) JSONSerializer.toJSON(raw);
            JSONArray resultObj = (JSONArray) rawObj.get("results");
            JSONArray alterArray = (JSONArray) ((JSONObject)resultObj.get(0)).get("alternatives");

            JSONObject guessedObj;
            String guessedText;

            for (int i = 0; i < alterArray.size(); i++) {
                guessedObj = (JSONObject) alterArray.get(i);
                guessedText = (String) guessedObj.get("transcript");
                guessedText.replace("\\u0027", "'"); // \u0027 = ' (unicode decode)
                arrResult.add(guessedText.trim());
            }

            return arrResult;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String args[]) {
//        IBMSpeechRecognition.getInstance().sendPost("en-us", "./tmp/RecordAudio.wav");
//    }
}
