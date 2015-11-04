package engine.blog.db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import engine.blog.entities.BlogPost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongojack.WriteResult;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class BlogPostsManagerTest {

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    private MongodExecutable mongodExecutable;
    private MongodProcess mongodProcess;
    private MongoClient mongoClient;
    private DB mongoDatabase;
    private BlogPostsManager blogPostsManager;

    @Before
    public void setup() throws Exception {
        mongodExecutable = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.DEVELOPMENT)
                .net(new Net(12345, Network.localhostIsIPv6()))
                .build());
        mongodProcess = mongodExecutable.start();
        mongoClient = new MongoClient("localhost", 12345);
        mongoDatabase = mongoClient.getDB("test");
        blogPostsManager = new BlogPostsManager(mongoDatabase);
    }

    @Test
    public void get_blog_post_with_null_id_should_not_break() {
        Optional<BlogPost> blogPost = blogPostsManager.getBlogPostById(null);
        assertFalse(blogPost.isPresent());
    }

    @Test
    public void test_inserting_a_new_blog_post() {
        BlogPost blogPost = new BlogPost("test", "test_body_text");
        WriteResult<BlogPost, String> writeResult = blogPostsManager.insertNewBlogPost(blogPost);
        Optional<BlogPost> blogPostFound = blogPostsManager.getBlogPostById(writeResult.getSavedId());
        assertTrue(blogPostFound.isPresent());
        assertEquals("test_body_text", blogPostFound.get().getBodyText());
    }

    @Test
    public void test_listing_all_blog_posts() {
        List<BlogPost> allBlogPosts = blogPostsManager.listAllBlogPosts();
        assertEquals(0, allBlogPosts.size());
        BlogPost blogPost1 = new BlogPost("test1", "test_body_text1");
        BlogPost blogPost2 = new BlogPost("test2", "test_body_text2");
        blogPostsManager.insertNewBlogPost(blogPost1);
        blogPostsManager.insertNewBlogPost(blogPost2);
        allBlogPosts = blogPostsManager.listAllBlogPosts();
        assertEquals(2, allBlogPosts.size());

    }



    @After
    public void tearDown() {
        try {
            mongodProcess.stop();
            mongodExecutable.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}