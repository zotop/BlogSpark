package engine.blog.run;


import com.mongodb.MongoClient;
import engine.blog.resource.BlogPostResource;
import spark.servlet.SparkApplication;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class Main implements SparkApplication {

    @Override
    public void init() {
        staticFileLocation("public");
        new BlogPostResource();
    }
}
