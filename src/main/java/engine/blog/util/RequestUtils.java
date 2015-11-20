package engine.blog.util;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class RequestUtils {

    public static boolean parametersAreValid(String... parameters) {
        for(String parameter : parameters) {
            if(StringUtils.isEmpty(parameter) || parameter.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
