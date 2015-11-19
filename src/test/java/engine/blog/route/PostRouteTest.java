package engine.blog.route;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import engine.blog.util.EmbeddedMongo;
import engine.blog.util.HttpCall;
import engine.blog.util.HttpResponse;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class PostRouteTest {

    private static EmbeddedMongo embeddedMongo;

    @BeforeClass
    public static void beforeClass() throws IOException {
        Spark.init();
        Spark.awaitInitialization();
        embeddedMongo = new EmbeddedMongo();
        embeddedMongo.start();
    }

    @Before
    public void setup() throws Exception {
        DB testDatabase = embeddedMongo.getDatabase("test");
        new PostRoute(testDatabase);
    }

    @Test
    public void list_all_posts() {
        try {
            HttpResponse response = HttpCall.perform("GET", Path.POST + "list");
            JsonElement jsonResponse = response.json();
            List<String> contentTypeList = response.headers.get("Content-Type");
            assertEquals(200, response.status);
            assertTrue(contentTypeList.contains("application/json"));
            assertTrue(jsonResponse instanceof JsonArray);
            JsonArray jsonArray = jsonResponse.getAsJsonArray();
            assertTrue(jsonArray.size() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void insert_new_post() {
        try {
            HttpResponse response = HttpCall.perform("POST", Path.POST + "new?title=FirstPost&body=SampleBody");
            JsonElement jsonResponse = response.json();
            List<String > contentTypeList = response.headers.get("Content-Type");
            assertEquals(201, response.status);
            assertTrue(contentTypeList.contains("application/json"));
            assertTrue(jsonResponse instanceof JsonObject);
            JsonObject jsonObject = jsonResponse.getAsJsonObject();
            assertTrue(jsonObject.has("id"));
            assertEquals("FirstPost", jsonObject.get("title").getAsString());
            assertEquals("SampleBody", jsonObject.get("body").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @AfterClass
    public static void afterClass() {
        embeddedMongo.stop();
        Spark.stop();
    }


}

