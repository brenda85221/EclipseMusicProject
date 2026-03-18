package team5.concert.model.bean;

import jakarta.persistence.*;

@Entity
@Table(name = "concertType")
public class ConcertType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeId")
    private int typeId;

    @Column(name = "typeName")
    private String typeName;

    // Getters and Setters
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
