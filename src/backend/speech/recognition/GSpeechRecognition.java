package backend.speech.recognition;


import backend.util.R;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuong
 */
public class GSpeechRecognition {
    


    
    private static GSpeechRecognition instance = new GSpeechRecognition();
    
    private GSpeechRecognition() {}
    
    public static GSpeechRecognition getInstance() { return instance; }
    
    public ArrayList<String> sendPost(String lang, String audioPath) {

        System.out.println("\nGoogle speech recoginition v2: ");
        System.out.println("Audio Language: " + lang);
        System.out.println("Audio path: " + audioPath);
        System.out.println("Sending request...");

        byte[] audioData = getFileBytes(audioPath);
        
        try {
            URL url = new URL(String.format(R.GOOGLE_STT_URL, lang, R.GOOGLE_STT_API_KEY));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "audio/l16; rate=16000");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setConnectTimeout(30000);
            
            con.setDoOutput(true);
            con.setInstanceFollowRedirects(false);
            con.setUseCaches(false);
            
            DataOutputStream writer = new DataOutputStream(con.getOutputStream());
            writer.write(audioData);
            writer.flush();
            writer.close();
            
            con.disconnect();
            
            System.out.println("Done sending request");
            
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            // response contains two lines, and we only get line 2
            String line = reader.readLine();
            String line2 = reader.readLine();

            System.out.println("Response data:\n" + line2);

            return (line2 == null ) ? null : getResultFromJson(line2);
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GSpeechRecognition.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GSpeechRecognition.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private byte[] getFileBytes(String filePath) {
        FileInputStream fileStream = null;
        File file = new File(filePath);
        byte fileData[] = new byte[(int)file.length()];
        
        // get byte array of audio file
        try {
            fileStream = new FileInputStream(file);
            fileStream.read(fileData);
            
            return fileData;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GSpeechRecognition.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            
        } catch (IOException ex) {
            Logger.getLogger(GSpeechRecognition.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            
        }
    }

    public ArrayList<String> getResultFromJson(String raw) {
        ArrayList<String> arrResult = new ArrayList<>();

        try {

            JSONObject rawObj = (JSONObject) JSONSerializer.toJSON(raw);
            JSONArray resultObj = (JSONArray) rawObj.get("result");
            JSONArray alterArray = (JSONArray) ((JSONObject)resultObj.get(0)).get("alternative");

            JSONObject guessedObj;
            String guessedText;

            for (int i = 0; i < alterArray.size(); i++) {
                guessedObj = (JSONObject) alterArray.get(i);
                guessedText = (String) guessedObj.get("transcript");

                arrResult.add(guessedText.trim());
            }

            return arrResult;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String args[]) {
//        GSpeechRecognition.getInstance()
//                .sendPost("en-us", "./tmp/RecordAudio.wav");
//    }
}
