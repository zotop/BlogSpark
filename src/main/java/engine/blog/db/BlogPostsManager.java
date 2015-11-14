package engine.blog.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import engine.blog.entities.BlogPost;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.List;
import java.util.Optional;


public class BlogPostsManager {

    public static final String COLLECTION_NAME = "blogposts";
    private JacksonDBCollection<BlogPost, String> blogPostsCollection;

    public BlogPostsManager() {
        DB db = MongoDBClient.INSTANCE.getDatabase("test");
        DBCollection collection = db.getCollection(COLLECTION_NAME);
        this.blogPostsCollection = JacksonDBCollection.wrap(collection, BlogPost.class,
                String.class);
    }

    public BlogPostsManager(DB db) {
        DBCollection collection = db.getCollection(COLLECTION_NAME);
        this.blogPostsCollection = JacksonDBCollection.wrap(collection, BlogPost.class,
                String.class);
    }

    public WriteResult insertNewBlogPost(BlogPost blogPost) {
        return blogPostsCollection.insert(blogPost);
    }

    public Optional<BlogPost> getBlogPostById(String blogPostId) {
        return Optional.ofNullable(blogPostsCollection.findOneById(blogPostId));
    }

    public List<BlogPost> listAllBlogPosts() {
        return blogPostsCollection.find().toArray();
    }


}
