package engine.blog.json;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by joaoguerreiro on 14/11/15.
 */
public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
