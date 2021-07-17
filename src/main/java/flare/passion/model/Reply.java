package flare.passion.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String content;

    @ManyToOne
    private Ask ask;

    @ManyToOne
    private Reply reply;

    @ManyToOne
    private User user;

    private Date publishDate;

    public Reply() {
    }

    public Reply(String content, User user, Reply reply, Ask ask) {
        this.content = content;
        this.user = user;
        this.reply = reply;
        this.ask = ask;
    }

    public Reply(String content, User user, Ask ask) {
        this.ask = ask;
        this.content = content;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Ask getAsk() {
        return ask;
    }

    public void setAsk(Ask ask) {
        this.ask = ask;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
