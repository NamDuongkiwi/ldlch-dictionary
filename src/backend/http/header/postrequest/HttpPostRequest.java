package backend.http.header.postrequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cuong on 10/27/2015.
 */
public class HttpPostRequest {
    private static HttpPostRequest ourInstance = new HttpPostRequest();
    public static HttpPostRequest getInstance() {
        return ourInstance;
    }

    private HttpPostRequest() {}

    public String sendPost(String urlStr, String urlParams) {

        try {

            URL url = new URL(urlStr);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();

            //add request header
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("User-Agent", "Mozilla/5.0");
            urlCon.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            //send post request
            urlCon.setDoOutput(true);
            DataOutputStream writer = new DataOutputStream(urlCon.getOutputStream());
            writer.writeBytes(urlParams);
            writer.flush();
            writer.close();

            //get response information
            int responseCode = urlCon.getResponseCode();
            System.out.println("Send POST request to url: " + urlStr);
            System.out.println("With parameters: " + urlParams);
            System.out.println("Responsed with code: " + responseCode);

            //read data from response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            urlCon.getInputStream(), "UTF-8"));

            StringBuilder res = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                res.append(line);
            }
            reader.close();

            return res.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
