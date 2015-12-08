package engine.blog.json;

import com.google.gson.GsonBuilder;
import spark.ResponseTransformer;

/**
 * Object to json converter
 */
public class JsonTransformer implements ResponseTransformer {

    private GsonBuilder gson = new GsonBuilder();

    @Override
    public String render(Object model) {
        gson.addSerializationExclusionStrategy(new JsonSerializationExclusionStrategy());
        return gson.create().toJson(model);
    }
}
