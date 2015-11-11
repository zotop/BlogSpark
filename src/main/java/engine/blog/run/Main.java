package engine.blog.run;


import spark.servlet.SparkApplication;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class Main implements SparkApplication {

    @Override
    public void init() {
        staticFileLocation("public");
        get("/hello", (req, res) -> "Hello World");
        //get("/", (req, res) -> "hi");
    }
}
