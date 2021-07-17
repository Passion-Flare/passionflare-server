package flare.passion.model;

import javax.persistence.*;

@Entity
public class Area {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String ISOCode;

    public Area() {
    }
    public Area(String ISOCode){
        this.ISOCode=ISOCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String ISOCode) {
        this.ISOCode = ISOCode;
    }
}
