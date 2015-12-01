package engine.blog.testutils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class HttpResponse {

    public final int status;
    public final Map<String, List<String>> headers;
    public final String body;


    public HttpResponse(int status, Map<String, List<String>> headers, String body) {
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public JsonElement json() {
        return new JsonParser().parse(body);
    }

}
