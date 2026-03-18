package team5.concert.model.bean;

import jakarta.persistence.*;
import team5.profile.model.bean.ProfilesBean;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name = "registration")
public class Registration {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "registrationId", nullable = false)
	    private Integer registrationId;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "concertId", nullable = false)
	    private Concert concert;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "profileId", nullable = false)
	    private ProfilesBean profile;

	    @Column(name = "registrationTime", nullable = false, columnDefinition = "DATETIME DEFAULT GETDATE()")
	    private LocalDateTime registrationTime;

	    @Column(name = "ticketCount", nullable = false, columnDefinition = "INT DEFAULT 1")
	    private Integer ticketCount;

	    @Column(name = "ticketPrice")
	    private Integer ticketPrice;


	    @JsonProperty("totalAmount")
	    @Column(name = "totalAmount")
	    private Integer totalAmount;

	    @Column(name = "isDeleted")
	    private Boolean isDeleted;

	    @Column(name = "registStatus")
	    private String registStatus;

	    @Column(name = "paymentMethod", length = 50)
	    private String paymentMethod;

	    

		public Integer getRegistrationId() {
			return registrationId;
		}

		public void setRegistrationId(Integer registrationId) {
			this.registrationId = registrationId;
		}

		public Concert getConcert() {
			return concert;
		}

		public void setConcert(Concert concert) {
			this.concert = concert;
		}


		public ProfilesBean getProfile() {
			return profile;
		}

		public void setProfile(ProfilesBean profile) {
			this.profile = profile;
		}

		public LocalDateTime getRegistrationTime() {
			return registrationTime;
		}

		public void setRegistrationTime(LocalDateTime registrationTime) {
			this.registrationTime = registrationTime;
		}

		public Integer getTicketCount() {
			return ticketCount;
		}

		public void setTicketCount(Integer ticketCount) {
			this.ticketCount = ticketCount;
		}

		   public Integer getTicketPrice() {
			return ticketPrice;
		}

		public void setTicketPrice(Integer ticketPrice) {
			this.ticketPrice = ticketPrice;
		}

		public void setTotalAmount(Integer totalAmount) {
			this.totalAmount = totalAmount;
		}

		public Integer getTotalAmount() {
		        return totalAmount;
		    }


		public Boolean getIsDeleted() {
			return isDeleted;
		}

		public void setIsDeleted(Boolean isDeleted) {
			this.isDeleted = isDeleted;
		}

		public String getRegistStatus() {
			return registStatus;
		}

		public void setRegistStatus(String registStatus) {
			this.registStatus = registStatus;
		}

		public String getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
		}


	    
}
