package engine.blog.route;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import engine.blog.db.BlogPostsManager;
import engine.blog.util.HttpResponse;
import engine.blog.util.HttpUtil;
import org.junit.*;
import spark.Spark;

import static org.junit.Assert.assertTrue;

public class PostRouteTest {

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    private MongodExecutable mongodExecutable;
    private MongodProcess mongodProcess;
    private MongoClient mongoClient;
    private DB mongoDatabase;
    private BlogPostsManager blogPostsManager;

    @BeforeClass
    public static void beforeClass() {
        Spark.init();
        Spark.awaitInitialization();
    }

    @Before
    public void setup() throws Exception {
        mongodExecutable = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.DEVELOPMENT)
                .net(new Net(12345, Network.localhostIsIPv6()))
                .build());
        mongodProcess = mongodExecutable.start();
        mongoClient = new MongoClient("localhost", 12345);
        mongoDatabase = mongoClient.getDB("test");
        new PostRoute(mongoDatabase);
    }

    @Test
    public void list_all_posts() {
        HttpResponse response = HttpUtil.request("GET", "/post/list");
        JsonElement jsonResponse = response.json();
        assertTrue(jsonResponse instanceof JsonArray);
        JsonArray jsonArray = jsonResponse.getAsJsonArray();
        assertTrue(jsonArray.size() == 0);
    }

    @After
    public void tearDown() {
        try {
            mongodProcess.stop();
            mongodExecutable.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }


}

