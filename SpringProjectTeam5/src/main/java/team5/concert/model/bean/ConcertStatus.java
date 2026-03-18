package team5.concert.model.bean;
import jakarta.persistence.*;

@Entity
@Table(name = "concertStatus")
public class ConcertStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusId")
    private int statusId;

    @Column(name = "statusName")
    private String statusName;

    // Getters and Setters
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
