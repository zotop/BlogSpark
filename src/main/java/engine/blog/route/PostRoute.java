package engine.blog.route;


import com.mongodb.DB;
import engine.blog.db.BlogPostsManager;
import engine.blog.entities.BlogPost;
import engine.blog.util.JsonTransformer;

import static spark.Spark.get;
import static spark.Spark.post;

public class PostRoute {

    BlogPostsManager blogPostsManager;

    public PostRoute(DB db) {
        blogPostsManager = new BlogPostsManager(db);
        addRoutes();
    }

    private void addRoutes() {
        insertNewPost();
        listAllPosts();
    }

    private void listAllPosts() {
        get(Path.POST + "list", (request, response) -> {
            response.type("application/json");
            return blogPostsManager.listAllBlogPosts();
        }, new JsonTransformer());
    }

    private void insertNewPost() {
        post(Path.POST + "new", (request, response) -> {
            BlogPost blogPost = new BlogPost("title", "body");
            blogPostsManager.insertNewBlogPost(blogPost);
            return "success";
        });
    }
}
