package engine.blog.entities;


import org.mongojack.Id;
import org.mongojack.ObjectId;

public class User {

    @Id
    @ObjectId
    private  String id;

    private String firstName;
    private String surname;
    private Credentials credentials;

    @Id
    @ObjectId
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
