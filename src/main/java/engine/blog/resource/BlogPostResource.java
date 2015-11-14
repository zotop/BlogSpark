package engine.blog.resource;


import com.mongodb.DB;
import engine.blog.db.BlogPostsManager;
import engine.blog.entities.BlogPost;
import engine.blog.util.JsonTransformer;
import org.mongojack.internal.stream.JacksonDBDecoder;

import static spark.Spark.get;
import static spark.Spark.post;

public class BlogPostResource {

    BlogPostsManager blogPostsManager;

    public BlogPostResource() {
        blogPostsManager = new BlogPostsManager();
        insertNewPost();
        listAllPosts();
    }

    private void listAllPosts() {
        get("/post/list", (request, response) -> {
            return blogPostsManager.listAllBlogPosts();
        }, new JsonTransformer());
    }

    private void insertNewPost() {
        post("/post/new", (request, response) -> {
            BlogPost blogPost = new BlogPost("title", "body");
            blogPostsManager.insertNewBlogPost(blogPost);
            return "success";
        });
    }
}
