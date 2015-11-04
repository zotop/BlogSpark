package engine.blog.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.mongojack.Id;
import org.mongojack.ObjectId;


public class BlogPost {

    private String title;
    private String bodyText;

    @Id
    @ObjectId
    private  String id;

    @JsonCreator
    public BlogPost(@Id String id) {
        this.id = id;
    }

    @Id
    @ObjectId
    public String getId() {
        return id;
    }

    public BlogPost(String title, String bodyText) {
        this.title = title;
        this.bodyText = bodyText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }


}
