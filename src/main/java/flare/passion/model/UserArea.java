package flare.passion.model;

import javax.persistence.*;

@Entity
public class UserArea {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Area area;

    public UserArea() {
    }
    public UserArea(User user,Area area){
        this.area=area;
        this.user=user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
