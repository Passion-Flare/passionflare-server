package flare.passion.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Ask {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String title;
    private String content;

    @ManyToOne
    private User user;

    @ManyToOne
    private Passage passage;

    private Date publishDate;

    @OneToOne
    private Reply latestReply;

    public Ask() {
    }

    public Ask(String title, String content, User user, Passage passage) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.passage = passage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Passage getPassage() {
        return passage;
    }

    public void setPassage(Passage passage) {
        this.passage = passage;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Reply getLatestReply() {
        return latestReply;
    }

    public void setLatestReply(Reply latestReply) {
        this.latestReply = latestReply;
    }
}
