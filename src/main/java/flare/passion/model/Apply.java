package flare.passion.model;

import javax.persistence.*;

@Entity
public class Apply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String reason;
    private String contact;

    /*
     * 1为成功，0为失败，2为未处理
     * 默认为2
     * */
    private int status;
    private String organization;

    @ManyToOne
    private User user;

    public Apply(String reason, String contact, String organization, User user) {
        this.reason = reason;
        this.contact = contact;
        this.organization = organization;
        this.user = user;
        this.status = 2;
    }

    public Apply() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
