package engine.blog.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import engine.blog.entities.BlogPost;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public class BlogPostsManager {

    private static final String COLLECTION_NAME = "blogposts";
    private JacksonDBCollection<BlogPost, String> blogPostsCollection;

    public BlogPostsManager(DB db) {
        initBlogPostsCollection(db);
    }

    private void initBlogPostsCollection(DB db) {
        DBCollection collection = db.getCollection(COLLECTION_NAME);
        this.blogPostsCollection = JacksonDBCollection.wrap(collection, BlogPost.class,
                String.class);
    }

    public WriteResult insertNewBlogPost(BlogPost blogPost) {
        blogPost.setCreationDate(new Date());
        return blogPostsCollection.insert(blogPost);
    }

    public Optional<BlogPost> getBlogPostById(String blogPostId) {
        return Optional.ofNullable(blogPostsCollection.findOneById(blogPostId));
    }

    public List<BlogPost> listAllBlogPosts() {
        DBObject creationDateDescending = new BasicDBObject("creationDate", -1);
        DBObject omitPostBody = new BasicDBObject("body", 0);
        return blogPostsCollection.find(new BasicDBObject(), omitPostBody).sort(creationDateDescending).toArray();
    }

    public List<BlogPost> listBlogPostsContainingTag(String tag) {
        DBObject creationDateDescending = new BasicDBObject("creationDate", -1);
        DBObject omitPostBody = new BasicDBObject("body", 0);
        DBObject tagObject = new BasicDBObject();
        tagObject.put("tags", tag);
        return blogPostsCollection.find(tagObject, omitPostBody).sort(creationDateDescending).toArray();
    }

    public int deletePost(String id) {
        return blogPostsCollection.removeById(id).getN();
    }

}
