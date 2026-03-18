package team5.concert.model;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import team5.concert.model.bean.Concert;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Integer> {
	
	// 查詢可見狀態
	List<Concert> findByViewStatus(String viewStatus);

	// 關鍵字查詢
	  @Query("SELECT c FROM Concert c WHERE " +
	           "LOWER(c.concertName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(c.concertDesc) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(c.concertAct) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	    List<Concert> searchByKeyword(@Param("keyword") String keyword);

    // 地區查詢活動
    List<Concert> findByConcertPlaceLocRegion(String locRegion);

    // 活動狀態查詢
    List<Concert> findByConcertStatusStatusName(String statusName);

    // 日期範圍查詢
    List<Concert> findByConcertDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    
    // 活動類型名稱查詢
    List<Concert> findByConcertTypeTypeName(String typeName);
    
    // 地區、類型和狀態查詢
    List<Concert> findByConcertPlaceLocRegionAndConcertTypeTypeNameAndConcertStatusStatusName(
            String locRegion, String typeName, String statusName);

    // 地區和類型查詢
    List<Concert> findByConcertPlaceLocRegionAndConcertTypeTypeName(
            String locRegion, String typeName);

    // 地區和狀態查詢
    List<Concert> findByConcertPlaceLocRegionAndConcertStatusStatusName(
            String locRegion, String statusName);

    // 類型和狀態查詢
    List<Concert> findByConcertTypeTypeNameAndConcertStatusStatusName(
            String typeName, String statusName);
    
    
}
