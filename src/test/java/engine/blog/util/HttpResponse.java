package engine.blog.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 *
 */
public class HttpResponse {

        public final String body;
        public final int status;

        public HttpResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public JsonElement json() {
            return new JsonParser().parse(body);
        }

}
