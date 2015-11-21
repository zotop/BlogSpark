package engine.blog.route;


import com.google.gson.JsonObject;
import com.mongodb.DB;
import engine.blog.db.BlogPostsManager;
import engine.blog.entities.BlogPost;
import engine.blog.util.JsonTransformer;
import engine.blog.util.RequestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.WriteResult;

import java.util.Optional;

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
        getPostById();
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
            if (!RequestUtils.parametersAreValid(title, body)) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return "Missing Parameters";
            }
            BlogPost post = new BlogPost(title, body);
            WriteResult writeResult = blogPostsManager.insertNewBlogPost(post);
            BlogPost savedPost = (BlogPost) writeResult.getSavedObject();
            response.type("application/json");
            response.status(HttpStatus.CREATED_201);
            return savedPost;
        }, new JsonTransformer());
    }

    private void getPostById() {
        get(Path.POST + "view", (request, response) -> {
            String id = request.queryParams("id");
            if (!RequestUtils.parametersAreValid(id)) {
                response.status(HttpStatus.BAD_REQUEST_400);
                return "Missing Parameters";
            }
            response.type("application/json");
            response.status(HttpStatus.OK_200);
            Optional<BlogPost> blogPost = blogPostsManager.getBlogPostById(id);
            if (blogPost.isPresent()) {
                return blogPost.get();
            } else {
                return new JsonObject();
            }
        }, new JsonTransformer());
    }
}
