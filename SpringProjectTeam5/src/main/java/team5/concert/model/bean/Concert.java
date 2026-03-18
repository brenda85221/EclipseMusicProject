package team5.concert.model.bean;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "concert")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concertId")
    private int concertId;

    @Column(name = "concertName")
    private String concertName;

    @Column(name = "concertDesc")
    private String concertDesc;

    @Column(name = "concertAct")
    private String concertAct;
    
    @Column(name = "concertDate")
    private LocalDateTime concertDate;

    @Column(name = "concertPhoto")
    private String concertPhoto;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "typeId")
    private ConcertType concertType;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "placeId")
    private ConcertPlace concertPlace;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "statusId")
    private ConcertStatus concertStatus;

    @Column(name = "registrationDeadline")
    private LocalDateTime registrationDeadline; // 報名截止日期

    @Column(name = "maxRegistrants")
    private int maxRegistrants; // 最大報名人數
    
    @Column(name = "viewStatus")
    private String viewStatus; // 新增欄位：可見性狀態
    
    @Column(name = "concertPrice")
    private Integer concertPrice;
 
    
    // Getters and Setters
    public int getConcertId() {
        return concertId;
    }

    public void setConcertId(int concertId) {
        this.concertId = concertId;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public String getConcertDesc() {
        return concertDesc;
    }

    public void setConcertDesc(String concertDesc) {
        this.concertDesc = concertDesc;
    }

    public String getConcertAct() {
		return concertAct;
	}

	public void setConcertAct(String concertAct) {
		this.concertAct = concertAct;
	}



    public LocalDateTime getRegistrationDeadline() {
		return registrationDeadline;
	}

	public LocalDateTime getConcertDate() {
		return concertDate;
	}

	public void setConcertDate(LocalDateTime concertDate) {
		this.concertDate = concertDate;
	}

	public void setRegistrationDeadline(LocalDateTime registrationDeadline) {
		this.registrationDeadline = registrationDeadline;
	}

	public String getConcertPhoto() {
        return concertPhoto;
    }

    public void setConcertPhoto(String concertPhoto) {
        this.concertPhoto = concertPhoto;
    }

    public ConcertType getConcertType() {
        return concertType;
    }

    public void setConcertType(ConcertType concertType) {
        this.concertType = concertType;
    }

    public ConcertPlace getConcertPlace() {
        return concertPlace;
    }

    public void setConcertPlace(ConcertPlace concertPlace) {
        this.concertPlace = concertPlace;
    }

    public ConcertStatus getConcertStatus() {
        return concertStatus;
    }

    public void setConcertStatus(ConcertStatus concertStatus) {
        this.concertStatus = concertStatus;
    }


    public int getMaxRegistrants() {
        return maxRegistrants;
    }

    public void setMaxRegistrants(int maxRegistrants) {
        this.maxRegistrants = maxRegistrants;
    }

    public String getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(String viewStatus) {
        this.viewStatus = viewStatus;
    }

	public Integer getConcertPrice() {
		return concertPrice;
	}

	public void setConcertPrice(Integer concertPrice) {
		this.concertPrice = concertPrice;
	}

    
}
