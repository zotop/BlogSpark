package engine.blog.entities;


public class Credentials {

    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //needed by Mongojack
    private Credentials() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
