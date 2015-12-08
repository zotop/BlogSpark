package engine.blog.route;

import com.google.gson.Gson;
import com.mongodb.DB;
import engine.blog.db.UserManager;
import engine.blog.entities.Credentials;
import engine.blog.entities.User;
import engine.blog.testutils.EmbeddedMongo;
import engine.blog.testutils.HttpCall;
import engine.blog.testutils.HttpMethod;
import engine.blog.testutils.HttpResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.*;
import spark.Spark;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class UserRouteTest {

    private UserManager userManager;

    @BeforeClass
    public static void setupOnce() throws IOException {
        Spark.init();
        Spark.awaitInitialization();
    }

    @Before
    public void setup() throws Exception {
        DB testDatabase = EmbeddedMongo.INSTANCE.getDatabase("test");
        new UserRoute(testDatabase);
        userManager = new UserManager(testDatabase);
    }

    @Test
    public void test_admin_login() {
        try {
            Credentials credentials = new Credentials("login", "pass");
            String credentialsAsJson = new Gson().toJson(credentials);
            HttpResponse response = HttpCall.perform(HttpMethod.POST, Path.USER + "/login", credentialsAsJson);
            User authenticatedUser = new Gson().fromJson(response.body, User.class);
            assertEquals("login", authenticatedUser.getCredentials().getUsername());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void failure_to_authenticate_should_return_401_status() {
        try {
            Credentials credentials = new Credentials("*", "*");
            String credentialsAsJson = new Gson().toJson(credentials);
            HttpResponse response = HttpCall.perform(HttpMethod.POST, Path.USER + "/login", credentialsAsJson);
            assertEquals(HttpStatus.UNAUTHORIZED_401, response.status);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }



    @After
    public void clearDatabase() {
        EmbeddedMongo.INSTANCE.getDatabase("test").dropDatabase();
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }

}
