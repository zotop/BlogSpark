package engine.blog.run;


import engine.blog.resource.BlogPostResource;
import spark.servlet.SparkApplication;

import static spark.Spark.staticFileLocation;

public class Main implements SparkApplication {

    @Override
    public void init() {
        staticFileLocation("public");
        new BlogPostResource();
    }
}
