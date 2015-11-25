package engine.blog.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HttpCall {
    public static HttpResponse get(String path) throws IOException {
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.connect();
        Map<String, List<String>> headers = connection.getHeaderFields();
        String body = "";
        try {
            body = IOUtils.toString(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpResponse(connection.getResponseCode(), headers, body);
    }

    public static HttpResponse post(String path, String json) throws IOException {
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        if (json != null) {
            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes());
            os.flush();
        }
        connection.connect();
        Map<String, List<String>> headers = connection.getHeaderFields();
        String body = "";
        try {
            body = IOUtils.toString(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpResponse(connection.getResponseCode(), headers, body);
    }
}
