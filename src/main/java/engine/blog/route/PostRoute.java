package engine.blog.route;


import com.mongodb.DB;
import engine.blog.db.BlogPostsManager;
import engine.blog.entities.BlogPost;
import engine.blog.util.JsonTransformer;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.WriteResult;

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
            response.status(HttpStatus.OK_200);
            return blogPostsManager.listAllBlogPosts();

        }, new JsonTransformer());
    }

    private void insertNewPost() {
        post(Path.POST + "new", (request, response) -> {
            String title = request.queryParams("title");
            String body = request.queryParams("body");
            BlogPost post = new BlogPost(title, body);
            WriteResult writeResult = blogPostsManager.insertNewBlogPost(post);
            BlogPost savedPost = (BlogPost) writeResult.getSavedObject();
            response.type("application/json");
            response.status(HttpStatus.CREATED_201);
            return savedPost;
        }, new JsonTransformer());
    }
}
