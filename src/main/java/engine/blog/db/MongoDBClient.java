package engine.blog.db;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import java.io.IOException;

public enum MongoDBClient {

    INSTANCE;

    private MongodStarter starter;
    private MongodExecutable mongodExecutable;
    private MongodProcess mongodProcess;

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

    public DB getDatabase(String databaseName) {
        return mongoClient.getDB(databaseName);
    }

    public void stop() {
        mongodProcess.stop();
        mongodExecutable.stop();
    }
}
