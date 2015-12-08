package engine.blog.properties;


import engine.blog.entities.Credentials;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AdminUserPropertiesTest {

    @Test
    public void test_retrieving_admin_credentials() {
        Credentials credentials = AdminUserProperties.INSTANCE.getAdminCredentials();
        assertNotNull(credentials.getUsername());
        assertNotNull(credentials.getPassword());
    }

}
