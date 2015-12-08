package engine.blog.db;


import com.mongodb.DB;
import engine.blog.entities.Credentials;
import engine.blog.entities.User;
import engine.blog.testutils.EmbeddedMongo;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class UserManagerTest {

    private UserManager userManager;

    @Before
    public void setUpDatabase() {
        DB testDatabase = EmbeddedMongo.INSTANCE.getDatabase("test");
        userManager = new UserManager(testDatabase);
    }

    @Ignore
    @Test
    public void valid_user_is_authenticated() {
        //Credentials credentials = new Credentials("username", "password");
       // assertTrue(userManager.getAuthenticatedUser(credentials));
    }

    @Test
    public void usernames_should_be_unique() {
        Credentials credentials = new Credentials("username", "password");
        User userA = new User();
        User userB = new User();
        userA.setCredentials(credentials);
        userB.setCredentials(credentials);
        userManager.createUser(userA);
        assertFalse(userManager.createUser(userB));
    }

    @After
    public void clearDatabase() {
        EmbeddedMongo.INSTANCE.getDatabase("test").dropDatabase();
    }

}
