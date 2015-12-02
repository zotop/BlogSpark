package engine.blog.testutils;

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

    private static final String BASE_URL = "http://localhost:4567";

    public static HttpResponse perform(HttpMethod method, String path, String json) throws IOException {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.value());
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
        }
        return new HttpResponse(connection.getResponseCode(), headers, body);
    }
}
