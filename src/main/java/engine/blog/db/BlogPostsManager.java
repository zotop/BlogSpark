package engine.blog.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import engine.blog.entities.BlogPost;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import java.util.List;
import java.util.Optional;


public class BlogPostsManager {

    private static final String COLLECTION_NAME = "blogposts";
    private JacksonDBCollection<BlogPost, String> blogPostsCollection;

    public BlogPostsManager() {
        DB db = MongoDBClient.INSTANCE.getDatabase();
        initBlogPostsCollection(db);
    }

    public BlogPostsManager(DB db) {
        initBlogPostsCollection(db);
    }

    private void initBlogPostsCollection(DB db) {
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
