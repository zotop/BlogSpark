package engine.blog.run;


import spark.servlet.SparkApplication;

import static spark.Spark.get;

public class Main implements SparkApplication {

    @Override
    public void init() {
        get("/hello", (req, res) -> "Hello World");
    }
}
