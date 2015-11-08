package backend.ocr;

import backend.util.R;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OCR enginee by neworc.com
 * @author cuong
 */
public class NewOCR {
    // essential singleton pattern properties
    private final static NewOCR instance = new NewOCR();
    private NewOCR(){}
    public static NewOCR getInstance() { return instance; }

    private String fileID;
    private String page;
    private final String lang = "eng";
    private final String psm = "3";
    
    public String execute(String imgPath) throws NullPointerException {
        System.out.println("\nOCR: ");
        System.out.println("Image path: " + imgPath);
        
        uploadImages(imgPath);
        System.out.println("Done upload image");
        String res =  getRecognizedText();
        if (res == null) {
            throw new NullPointerException("Can not parsing image");
        } else {
            System.out.println("Result: " + res);
        }
        
        return res;
    }
    
    private boolean uploadImages(String imgPath) {
        File file = new File(imgPath); 
        
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(R.OCR_URL_UPLOAD + "?key=" + R.OCR_API_KEY);
            
            HttpEntity entity = MultipartEntityBuilder
            .create()       
            .addPart("file", new FileBody(file))
            .addPart("type", new StringBody(URLConnection.guessContentTypeFromName(file.getName())))
            .build();

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity result = response.getEntity();
            
            BufferedReader reader = new BufferedReader(
                        new InputStreamReader(result.getContent()));
            
            StringBuffer buff = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buff.append(line + "\n");
//                System.out.println(line);
            }
            reader.close();
            
            getImageDetailsFromJson(buff.toString());
            return true;
            
        } catch (IOException ex) {
            Logger.getLogger(NewOCR.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
       
    private String getRecognizedText() {        
        
        try {
            String params = String.format("?key=%s&file_id=%s&page=%s&lang=%s&psm=%s",
                    R.OCR_API_KEY, fileID, page, lang, psm);
            
            URL url = new URL(R.OCR_URL_PARSE + params);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            
//            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'GET' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
		    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
            }
            in.close();
            
//            System.out.println("Response data : " + response.toString());
            return getTextFromJSON(response.toString());
            
        } catch (IOException ex) {
            Logger.getLogger(NewOCR.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }        
    }
    
    /**
     * get file_id and page
     * @param raw
     * @return 
     */
    private void getImageDetailsFromJson(String raw) {
        JSONObject obj = (JSONObject) JSONSerializer.toJSON(raw);
        if (obj.getString("status").equals("error")) { //error
            return ;
        }
        
        JSONObject dataObj = obj.getJSONObject("data");
        fileID = dataObj.getString("file_id");
        page = dataObj.getString("pages");
//        int start = raw.indexOf("\"file_id\":\"");
//        int end = raw.indexOf("\"", start+11);
//        fileID = raw.substring(start+11, end);
//        start = raw.indexOf("\"pages\":");
//        page = raw.substring(start).replaceAll("\\D+", "");
    }
    
    private String getTextFromJSON(String raw) {
        JSONObject obj = (JSONObject) JSONSerializer.toJSON(raw);
        if (obj.getString("status").equals("error")) { //error
            return null;
        }  
        
        JSONObject dataObj = obj.getJSONObject("data");
        String text = dataObj.getString("text").replace("\\n", "\n");
        
//        int start = raw.indexOf("\"text\":\"");
//        int end = raw.indexOf("\"", start+8);
//        String text = raw.substring(start+8, end);
        
//        System.out.printf("%s\n%s", raw, text);
        return text;
    }
}
