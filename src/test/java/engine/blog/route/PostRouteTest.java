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
import org.mongojack.WriteResult;
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
            HttpResponse response = HttpCall.get(Path.POST + "/list");
            JsonElement jsonResponse = response.json();
            int numberOfPosts = jsonResponse.getAsJsonArray().size();
            BlogPost blogPost = new BlogPost("test1", "test_body_text1");
            blogPostsManager.insertNewBlogPost(blogPost);
            blogPostsManager.insertNewBlogPost(blogPost);

            response = HttpCall.get(Path.POST + "/list");
            assertEquals(HttpStatus.OK_200, response.status);
            jsonResponse = response.json();
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
            HttpResponse response = HttpCall.post(Path.POST + "/new", null);
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

            HttpResponse response = HttpCall.post(Path.POST + "/new", jsonObject.toString());
            assertEquals(HttpStatus.CREATED_201, response.status);
            JsonElement jsonResponse = response.json();
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
            HttpResponse response = HttpCall.get(Path.POST + "/view?id=564cfb3cd1c44b940ebfbd3e");
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
            BlogPost blogPost1 = new BlogPost("test1", "test_body_text1");
            blogPost1.setTags(Arrays.asList(new String[]{"rest"}));
            BlogPost savedPost = (BlogPost) blogPostsManager.insertNewBlogPost(blogPost1).getSavedObject();
            HttpResponse findPostResponse = HttpCall.get(Path.POST + "/view?id=" + savedPost.getId());
            assertEquals(HttpStatus.OK_200, findPostResponse.status);
            JsonElement responseJson = findPostResponse.json();
            String foundId = responseJson.getAsJsonObject().get("id").getAsString();
            assertEquals(savedPost.getId(), foundId);
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
            HttpResponse response = HttpCall.get(Path.POST + "/list?tag=non_existent_tag");
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

            HttpResponse response = HttpCall.get(Path.POST + "/list?tag=rest");
            assertEquals(HttpStatus.OK_200, response.status);
            JsonArray jsonResponse = response.json().getAsJsonArray();
            assertEquals(2, jsonResponse.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void deletion_of_post_should_return_status_ok() {
        try {
            BlogPost blogPost1 = new BlogPost("test1", "test_body_text1");
            blogPost1.setTags(Arrays.asList(new String[]{"rest"}));
            WriteResult writeResult = blogPostsManager.insertNewBlogPost(blogPost1);
            String savedId = (String) writeResult.getSavedId();
            HttpResponse response = HttpCall.delete(Path.POST + "?id=" + savedId);
            assertEquals(HttpStatus.OK_200, response.status);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void deleting_a_post_twice_should_return_status_not_found() {
        try {

            BlogPost blogPost1 = new BlogPost("test1", "test_body_text1");
            blogPost1.setTags(Arrays.asList(new String[]{"rest"}));
            WriteResult writeResult = blogPostsManager.insertNewBlogPost(blogPost1);
            String savedId = (String) writeResult.getSavedId();
            blogPostsManager.deletePost(savedId);
            HttpResponse response = HttpCall.delete(Path.POST + "?id=" + savedId);
            assertEquals(HttpStatus.NOT_FOUND_404, response.status);
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

