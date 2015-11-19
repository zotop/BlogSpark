package engine.blog.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HttpCall {
    public static HttpResponse perform(String method, String path) throws IOException {
        URL url = new URL("http://localhost:4567" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.connect();
        Map<String, List<String>> headers = connection.getHeaderFields();
        String body = IOUtils.toString(connection.getInputStream());
        return new HttpResponse(connection.getResponseCode(), headers, body);
    }
}
