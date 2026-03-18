package team5.concert.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import team5.concert.model.bean.Concert;
import team5.concert.model.bean.Registration;
import team5.profile.model.bean.ProfilesBean;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    // 查詢某個活動的所有報名
    List<Registration> findByConcert_ConcertId(Integer concertId);
    
    // 查詢特定會員的報名記錄
    List<Registration> findByProfile_profileId(Integer profileId);

    // 查詢未刪除的報名記錄
    List<Registration> findByIsDeletedFalse();

    // 查詢指定日期範圍內的報名記錄
    List<Registration> findByRegistrationTimeBetween(LocalDateTime start, LocalDateTime end);
    

	boolean existsByConcertAndProfile(Concert concert, ProfilesBean profile);

//    Integer countTicketByConcert(Concert concert);
    
    // 查詢該活動的報名人數（排除已刪除的報名）
   	@Query("SELECT SUM(r.ticketCount) FROM Registration r WHERE r.concert = :concert AND r.isDeleted = false")
   	Integer countTicketByConcert(@Param("concert") Concert concert);
   	
    // 根據 registrationId 查詢對應的報名訂單
    Optional<Registration> findByRegistrationId(Integer registrationId);
  
    
    
}

