package aleatorio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectRandom {
	public String httpPost(String type) throws Exception{
		DetectedObject detectedObject = null;
		if((Math.random()*10)>5){
			detectedObject = new DetectedObject("Sur", type, new Date());
		}else{
			detectedObject = new DetectedObject("Norte", type, new Date());

		}
		String urlStr = "http://localhost:8080/FinalProject/detectedObject/DetectedObject";

			URL url = new URL(urlStr);
        	  HttpURLConnection conn =
        	      (HttpURLConnection) url.openConnection();
        	  conn.setRequestMethod("POST");
        	  conn.setDoOutput(true);
        	  conn.setDoInput(true);
        	  conn.setUseCaches(false);
        	  conn.setAllowUserInteraction(false);
        	  conn.setRequestProperty("Content-Type",
        	      "application/json");

        	  // Create the form content
        	  OutputStream out = conn.getOutputStream();
        	  Writer writer = new OutputStreamWriter(out, "UTF-8");
        	  writer.write(this.toJson(detectedObject));
        	  writer.close();
        	  out.close();

        	  if (conn.getResponseCode() != 200) {
        	    throw new IOException(conn.getResponseMessage());
        	  }

        	  // Buffer the result into a string
        	  BufferedReader rd = new BufferedReader(
        	      new InputStreamReader(conn.getInputStream()));
        	  StringBuilder sb = new StringBuilder();
        	  String line;
        	  while ((line = rd.readLine()) != null) {
        	    sb.append(line);
        	  }
        	  rd.close();

        	  conn.disconnect();
        	  return sb.toString();      
        }	
	
    public String toJson(Object data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
 
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
