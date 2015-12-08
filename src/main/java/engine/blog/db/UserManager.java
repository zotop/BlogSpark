package engine.blog.db;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import engine.blog.entities.Credentials;
import engine.blog.entities.User;
import engine.blog.properties.AdminUserProperties;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.Optional;

public class UserManager {

    private static final String COLLECTION_NAME = "users";
    private JacksonDBCollection<User, String> usersCollection;

    public UserManager(DB db) {
        initUsersCollection(db);
    }

    private void initUsersCollection(DB db) {
        DBCollection collection = db.getCollection(COLLECTION_NAME);
        this.usersCollection = JacksonDBCollection.wrap(collection, User.class, String.class);
        createUsernameIndex();
    }

    private void createUsernameIndex() {
        DBObject key = new BasicDBObject("credentials.username", 1);
        DBObject option = new BasicDBObject("unique", true);
        usersCollection.createIndex(key, option);
    }

    public Optional<User> getAuthenticatedUser(Credentials credentials) {
        /*
        BasicDBObject credentialsObject = new BasicDBObject("credentials.username", credentials.getUsername());
        credentialsObject.append("credentials.password", credentials.getPassword());
        return usersCollection.findOne(credentialsObject);
        */
        Credentials adminCredentials = AdminUserProperties.INSTANCE.getAdminCredentials();
        if(adminCredentials.getUsername().equals(credentials.getUsername())) {
            if(passwordMatches(credentials.getPassword(), adminCredentials.getPassword())) {
                User user = new User();
                user.setCredentials(adminCredentials);
                return Optional.of(user);
            }
        }
        return Optional.ofNullable(null);
    }

    private boolean passwordMatches(String plaintextPassword, String encryptedPassword) {
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        return encryptor.checkPassword(plaintextPassword, encryptedPassword);
    }

    public boolean createUser(User user) {
        WriteResult writeResult = null;
        try {
            writeResult = usersCollection.insert(user);
            return writeResult.getSavedObject() != null;
        } catch (Exception e) {
            if (writeResult != null) {
                String savedId = (String) writeResult.getSavedId();
                if (savedId != null) {
                    usersCollection.removeById(savedId);
                }
            }
            return false;
        }
    }
}
