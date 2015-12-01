package engine.blog.testutils;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public enum EmbeddedMongo {

    INSTANCE;

    private final MongodStarter starter = MongodStarter.getDefaultInstance();
    private MongodExecutable mongodExecutable;
    private MongodProcess mongodProcess;
    private MongoClient mongoClient;

    EmbeddedMongo() {
        try {
            mongodExecutable = starter.prepare(new MongodConfigBuilder()
                    .version(Version.Main.DEVELOPMENT)
                    .net(new Net(12345, Network.localhostIsIPv6()))
                    .build());
            mongodProcess = mongodExecutable.start();
            mongoClient = new MongoClient("localhost", 12345);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public DB getDatabase(String databaseName) {
        return mongoClient.getDB(databaseName);
    }

    public void stop() {
        try {
            mongodProcess.stop();
            mongodExecutable.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
