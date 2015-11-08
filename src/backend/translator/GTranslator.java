/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.translator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import backend.util.R;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
        
/**
 *
 * @author datrx105
 */
public class GTranslator {
    // properties
    private static GTranslator instance;
    
    
    // constructor
    private GTranslator() { // singleton pattern
    }
    
    public static synchronized GTranslator getInstance() {
        if (instance == null) {
            instance = new GTranslator();
        }
        return instance;
    }
    
    // private methods
    private File downloadFile(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/5.0");
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        BufferedInputStream bis = new BufferedInputStream(entity.getContent());
        File f = new File(R.TRANS_RESULT_PATH);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
        int inByte;
        while ((inByte = bis.read()) != -1)
            bos.write(inByte);
   
        bis.close();
        bos.close();
        return f;
    }
    
    // public methods
    
    /**
     *
     * @param sourceText
     * @param sourceLanguage
     * @param targetLanguage
     * @return
     */
    public String translate_a(String sourceText, String sourceLanguage, String targetLanguage) {
        String translatedText = new String();
        
        try {
            // Download translation
            String url = (String.format(R.GOOGLE_TRANS_URL,
                    sourceLanguage, targetLanguage,
                    URLEncoder.encode(sourceText, "UTF-8")));
                      
            File f = downloadFile(url);

            
            // Get translated text
            if (f.exists()) {
                String text = FileUtils.readFileToString(f, "UTF-8");
                int startQuote = text.indexOf("\"");
                int endQuote = text.indexOf("\"", startQuote + 1);
                
                if (startQuote != -1 && endQuote != -1) {
                    translatedText = text.substring(startQuote + 1, endQuote);
                }
                
            }       
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        return translatedText;
    }
    
    
}
