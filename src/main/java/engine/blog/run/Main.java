package engine.blog.run;


import com.mongodb.DB;
import engine.blog.db.MongoDBClient;
import engine.blog.route.PostRoute;
import spark.servlet.SparkApplication;

import static spark.Spark.staticFileLocation;

public class Main implements SparkApplication {

    @Override
    public void init() {
        staticFileLocation("public");
        DB db = MongoDBClient.INSTANCE.getDatabase();
        new PostRoute(db);
    }
}
