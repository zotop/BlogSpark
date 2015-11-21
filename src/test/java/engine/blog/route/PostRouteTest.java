package engine.blog.route;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.DB;
import engine.blog.util.EmbeddedMongo;
import engine.blog.util.HttpCall;
import engine.blog.util.HttpResponse;
import org.eclipse.jetty.http.HttpStatus;
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
            assertEquals(HttpStatus.OK_200, response.status);
            JsonElement jsonResponse = response.json();
            List<String> contentTypeList = response.headers.get("Content-Type");
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
    public void insert_empty_post_should_throw_error() {
        try {
            HttpResponse response = HttpCall.perform("POST", Path.POST + "new");
            assertEquals(HttpStatus.BAD_REQUEST_400, response.status);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void insert_new_post() {
        try {
            HttpResponse response = HttpCall.perform("POST", Path.POST + "new?title=First%20Post&body=Sample%20Body");
            assertEquals(HttpStatus.CREATED_201, response.status);
            JsonElement jsonResponse = response.json();
            List<String > contentTypeList = response.headers.get("Content-Type");
            assertTrue(contentTypeList.contains("application/json"));
            assertTrue(jsonResponse instanceof JsonObject);
            JsonObject jsonObject = jsonResponse.getAsJsonObject();
            assertTrue(jsonObject.has("id"));
            assertEquals("First Post", jsonObject.get("title").getAsString());
            assertEquals("Sample Body", jsonObject.get("body").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void get_non_existing_blog_post_by_id() {
        try {
            HttpResponse response = HttpCall.perform("GET", Path.POST + "view?id=564cfb3cd1c44b940ebfbd3e");
            assertEquals(HttpStatus.OK_200, response.status);
            List<String> contentTypeList = response.headers.get("Content-Type");
            assertTrue(contentTypeList.contains("application/json"));
            assertEquals("{}", response.body);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void get_blog_post_by_id() {
        try {
            HttpResponse newBlogPostResponse = HttpCall.perform("POST", Path.POST + "new?title=First%20Post&body=Sample%20Body");
            JsonElement jsonElement = newBlogPostResponse.json();
            String newPostId = jsonElement.getAsJsonObject().get("id").getAsString();
            HttpResponse findPostResponse = HttpCall.perform("GET", Path.POST + "view?id=" + newPostId);
            assertEquals(HttpStatus.OK_200, findPostResponse.status);
            List<String> contentTypeList = findPostResponse.headers.get("Content-Type");
            assertTrue(contentTypeList.contains("application/json"));
            jsonElement = findPostResponse.json();
            String foundId = jsonElement.getAsJsonObject().get("id").getAsString();
            assertEquals(newPostId, foundId);
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

