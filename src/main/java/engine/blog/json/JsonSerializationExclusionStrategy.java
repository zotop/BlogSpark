package engine.blog.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;

/**
 * Created by joaoguerreiro on 08/12/15.
 */
public class JsonSerializationExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        Expose expose = fieldAttributes.getAnnotation(Expose.class);
        if(expose != null) {
            return !expose.serialize();
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
