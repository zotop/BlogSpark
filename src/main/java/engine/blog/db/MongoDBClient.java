package engine.blog.db;


import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.io.IOException;

public enum MongoDBClient {

    INSTANCE;

    private MongoClient mongoClient;

    private MongoDBClient() {
        try {
            if (mongoClient == null) {
                mongoClient = getClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MongoClient getClient() throws IOException {
        return new MongoClient( "localhost" , 27017 );
    }

    public DB getDatabase() {
        return getDatabase("dev");
    }

    private DB getDatabase(String databaseName) {
        return mongoClient.getDB(databaseName);
    }

}
