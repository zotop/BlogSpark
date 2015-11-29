package engine.blog.route;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.DB;
import engine.blog.db.BlogPostsManager;
import engine.blog.entities.BlogPost;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PostRouteTest {

    private BlogPostsManager blogPostsManager;
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
        blogPostsManager = new BlogPostsManager(testDatabase);
    }

    @Test
    public void list_all_posts() {
        try {
            HttpResponse response = HttpCall.get(Path.POST + "list");
            JsonElement jsonResponse = response.json();
            int numberOfPosts = jsonResponse.getAsJsonArray().size();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("title", "First Post");
            jsonObject.addProperty("body", "Sample Body");
            HttpCall.post(Path.POST + "new", jsonObject.toString());
            HttpCall.post(Path.POST + "new", jsonObject.toString());
            response = HttpCall.get(Path.POST + "list");
            assertEquals(HttpStatus.OK_200, response.status);
            jsonResponse = response.json();
            List<String> contentTypeList = response.headers.get("Content-Type");
            assertTrue(contentTypeList.contains("application/json"));
            JsonArray jsonArray = jsonResponse.getAsJsonArray();
            assertEquals(numberOfPosts + 2, jsonArray.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void insert_empty_post_should_throw_error() {
        try {
            HttpResponse response = HttpCall.post(Path.POST + "new", null);
            assertEquals(HttpStatus.BAD_REQUEST_400, response.status);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void insert_new_post() {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("title", "First Post");
            jsonObject.addProperty("body", "Sample Body");
            JsonArray tagsArray = new JsonArray();
            tagsArray.add(new JsonPrimitive("rest"));
            tagsArray.add(new JsonPrimitive("api"));
            jsonObject.add("tags", tagsArray);
            HttpResponse response = HttpCall.post(Path.POST + "new", jsonObject.toString());
            assertEquals(HttpStatus.CREATED_201, response.status);
            JsonElement jsonResponse = response.json();
            List<String> contentTypeList = response.headers.get("Content-Type");
            assertTrue(contentTypeList.contains("application/json"));
            assertTrue(jsonResponse instanceof JsonObject);
            jsonObject = jsonResponse.getAsJsonObject();
            assertTrue(jsonObject.has("id"));
            assertTrue(jsonObject.has("creationDate"));
            assertEquals("First Post", jsonObject.get("title").getAsString());
            assertEquals("Sample Body", jsonObject.get("body").getAsString());
            JsonArray tags = (JsonArray) jsonObject.get("tags");
            assertEquals(2, tags.size());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    @Test
    public void get_non_existing_blog_post_by_id() {
        try {
            HttpResponse response = HttpCall.get(Path.POST + "view?id=564cfb3cd1c44b940ebfbd3e");
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
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("title", "First Post");
            jsonObject.addProperty("body", "Sample Body");
            HttpResponse newBlogPostResponse = HttpCall.post(Path.POST + "new", jsonObject.toString());
            JsonElement jsonElement = newBlogPostResponse.json();
            String newPostId = jsonElement.getAsJsonObject().get("id").getAsString();
            HttpResponse findPostResponse = HttpCall.get(Path.POST + "view?id=" + newPostId);
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

    @Test
    public void listing_posts_with_non_existent_tag_should_return_empty_results() {
        try {
            BlogPost blogPost1 = new BlogPost("test1", "test_body_text1");
            blogPost1.setTags(Arrays.asList(new String[]{"rest"}));
            blogPostsManager.insertNewBlogPost(blogPost1);
            HttpResponse response = HttpCall.get(Path.POST + "list?tag=non_existent_tag");
            assertEquals(HttpStatus.OK_200, response.status);
            JsonArray jsonResponse = response.json().getAsJsonArray();
            assertEquals(0, jsonResponse.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void posts_containing_a_certain_tag_should_be_returned() {
        try {
            BlogPost blogPost1 = new BlogPost("test1", "test_body_text1");
            blogPost1.setTags(Arrays.asList(new String[]{"rest"}));
            BlogPost blogPost2 = new BlogPost("test2", "test_body_text2");
            blogPost2.setTags(Arrays.asList(new String[]{"api"}));
            BlogPost blogPost3 = new BlogPost("test3", "test_body_text3");
            blogPost3.setTags(Arrays.asList(new String[]{"micro", "rest"}));
            blogPostsManager.insertNewBlogPost(blogPost1);
            blogPostsManager.insertNewBlogPost(blogPost2);
            blogPostsManager.insertNewBlogPost(blogPost3);
            HttpResponse response = HttpCall.get(Path.POST + "list?tag=rest");
            assertEquals(HttpStatus.OK_200, response.status);
            JsonArray jsonResponse = response.json().getAsJsonArray();
            assertEquals(2, jsonResponse.size());
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

