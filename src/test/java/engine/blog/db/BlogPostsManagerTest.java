package engine.blog.db;

import com.mongodb.DB;
import engine.blog.entities.BlogPost;
import engine.blog.util.EmbeddedMongo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongojack.WriteResult;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class BlogPostsManagerTest {

    private EmbeddedMongo embeddedMongo;
    private BlogPostsManager blogPostsManager;

    @Before
    public void setup() throws Exception {
        embeddedMongo = new EmbeddedMongo();
        embeddedMongo.start();
        DB testDatabase = embeddedMongo.getDatabase("test");
        blogPostsManager = new BlogPostsManager(testDatabase);
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
        assertEquals("test_body_text", blogPostFound.get().getBody());
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
        embeddedMongo.stop();

    }
}