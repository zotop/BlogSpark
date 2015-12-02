package engine.blog.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BlogPost {

    @JsonIgnore
    public static final String CREATION_DATE = "creationDate";

    @JsonIgnore
    public static final String BODY = "body";

    @JsonIgnore
    public static final String TAGS = "tags";

    private String title;
    private String body;
    private Date creationDate;
    private List<String> tags = new ArrayList<>();

    @Id
    @ObjectId
    private  String id;

    @JsonCreator
    private BlogPost(@Id String id) {
        this.id = id;
    }

    @Id
    @ObjectId
    public String getId() {
        return id;
    }

    public BlogPost(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
