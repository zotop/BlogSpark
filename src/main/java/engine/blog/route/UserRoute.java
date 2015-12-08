package engine.blog.route;

import com.google.gson.Gson;
import com.mongodb.DB;
import engine.blog.db.UserManager;
import engine.blog.entities.Credentials;
import engine.blog.entities.User;
import engine.blog.json.JsonResponse;
import engine.blog.json.JsonTransformer;
import engine.blog.util.RequestUtils;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Optional;

import static spark.Spark.post;

/**
 * API for user related operations
 */
public class UserRoute {

    UserManager userManager;

    public UserRoute(DB db) {
        userManager = new UserManager(db);
        addRoutes();
    }

    private void addRoutes() {
        login();
    }

    private void login() {
        post(Path.USER + "/login", (request, response) -> {
            Credentials credentials = new Gson().fromJson(request.body(), Credentials.class);
            if (RequestUtils.parametersAreValid(credentials.getUsername(), credentials.getPassword())) {
                Optional<User> optionalUser = userManager.getAuthenticatedUser(credentials);
                if (optionalUser.isPresent()) {
                    response.status(HttpStatus.OK_200);
                    return optionalUser.get();
                } else {
                    response.status(HttpStatus.UNAUTHORIZED_401);
                }
            } else {
                response.status(HttpStatus.BAD_REQUEST_400);
            }
            return new JsonResponse("User could not be authenticated.");
        }, new JsonTransformer());
    }
}
