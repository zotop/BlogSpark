package engine.blog.testutils;

import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class TestListener extends RunListener {

    /**
     * Called when all the tests are finished.
     * Source: http://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html
     * @param result
     * @throws Exception
     */
    @Override
    public void testRunFinished(Result result) throws Exception {
        EmbeddedMongo.INSTANCE.stop();
    }
}
