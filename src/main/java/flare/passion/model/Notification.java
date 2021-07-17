package flare.passion.model;


import javax.persistence.*;

@Entity
public class Notification {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String content;
    /**
     * 1:订阅地区疫情通知 2:收到回复通知 3:申请处理通知
     */
    private int type;

    @ManyToOne
    User user;

    public Notification() {
    }
    public Notification(String content,int type,User user){
        this.content=content;
        this.type=type;
        this.user=user;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
