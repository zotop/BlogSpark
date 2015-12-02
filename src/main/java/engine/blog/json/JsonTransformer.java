package engine.blog.json;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Object to json converter
 */
public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
