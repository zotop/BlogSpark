package engine.blog.route;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mongodb.DB;
import engine.blog.util.EmbeddedMongo;
import engine.blog.util.HttpCall;
import engine.blog.util.HttpResponse;
import org.junit.*;
import spark.Spark;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostRouteTest {

    private EmbeddedMongo embeddedMongo;

    @BeforeClass
    public static void beforeClass() {
        Spark.init();
        Spark.awaitInitialization();
    }

    @Before
    public void setup() throws Exception {
        embeddedMongo = new EmbeddedMongo();
        embeddedMongo.start();
        DB testDatabase = embeddedMongo.getDatabase("test");
        new PostRoute(testDatabase);
    }

    @Test
    public void list_all_posts() {
        HttpResponse response = HttpCall.perform("GET", Path.POST + "list");
        JsonElement jsonResponse = response.json();
        List<String> contentTypeList = response.headers.get("Content-Type");
        assertEquals(200, response.status);
        assertTrue(contentTypeList.contains("application/json"));
        assertTrue(jsonResponse instanceof JsonArray);
        JsonArray jsonArray = jsonResponse.getAsJsonArray();
        assertTrue(jsonArray.size() == 0);

    }

    @After
    public void tearDown() {
        embeddedMongo.stop();
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }


}

