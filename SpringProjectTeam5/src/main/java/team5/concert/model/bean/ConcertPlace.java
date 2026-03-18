package team5.concert.model.bean;


import jakarta.persistence.*;

@Entity
@Table(name = "concertPlace")
public class ConcertPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Integer placeId;      
    private String placeName;  
    private String locName;       
    private String locRegion;    

    // Getter 和 Setter 方法
    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getLocRegion() {
        return locRegion;
    }

    public void setLocRegion(String locRegion) {
        this.locRegion = locRegion;
    }
}

