package engine.blog.properties;

import engine.blog.entities.Credentials;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum AdminUserProperties {

    INSTANCE;

    private final Properties adminProperties = new Properties();

    private AdminUserProperties() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try(InputStream resourceStream = loader.getResourceAsStream("adminUser.properties")) {
            adminProperties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Credentials getAdminCredentials() {
        String login = adminProperties.getProperty("adminLogin");
        String password = adminProperties.getProperty("adminPassword");
        return  new Credentials(login, password);
    }


}
