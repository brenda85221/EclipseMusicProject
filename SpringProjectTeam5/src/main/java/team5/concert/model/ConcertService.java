package team5.concert.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import team5.concert.model.bean.Concert;
import team5.concert.model.bean.ConcertPlace;
import team5.concert.model.bean.ConcertStatus;
import team5.concert.model.bean.ConcertType;



@Service
@Transactional
public class ConcertService {
	
	@Autowired
	private ConcertRepository concertRepository;

    @Autowired
    private ConcertTypeRepository concertTypeRepository;
    
    @Autowired
    private ConcertPlaceRepository concertPlaceRepository;
    
    @Autowired
    private ConcertStatusRepository concertStatusRepository;
    
	 // 關鍵字查詢
	   public List<Concert> searchByKeyword(String keyword) {
	        return concertRepository.searchByKeyword(keyword);
	    }
	 
	// 查詢所有
    public List<Concert> getAll() {
        return concertRepository.findAll();
    }

    // 根據 ID 查詢
    public Optional<Concert> getById(Integer id) {
        return concertRepository.findById(id);
    }
    
//    // 刪除
//    public boolean deleteConcert(Integer id) {
//        if (concertRepository.existsById(id)) {
//            concertRepository.deleteById(id);
//            return true;
//        }
//        return false;  // 若 ID 不存在，返回 false
//    }

    // 新增
    public Concert create(Concert concert) {
        // 假設你的 concertType、concertPlace 和 concertStatus 是外來鍵引用的對象
        ConcertType concertType = concertTypeRepository.findById(concert.getConcertType().getTypeId()).orElse(null);
        ConcertPlace concertPlace = concertPlaceRepository.findById(concert.getConcertPlace().getPlaceId()).orElse(null);
        ConcertStatus concertStatus = concertStatusRepository.findById(concert.getConcertStatus().getStatusId()).orElse(null);

        if (concertType != null && concertPlace != null && concertStatus != null) {
            concert.setConcertType(concertType);
            concert.setConcertPlace(concertPlace);
            concert.setConcertStatus(concertStatus);
            return concertRepository.save(concert);
        } else {
            // 處理外來鍵無效的情況
            throw new IllegalArgumentException("Invalid foreign key value.");
        }
    }

    // 更新
    public Concert update(Integer id, Concert updatedConcert) {
        if (concertRepository.existsById(id)) {
            updatedConcert.setConcertId(id);  // 確保 ID 保持不變

            // 處理關聯
            ConcertType concertType = concertTypeRepository.findById(updatedConcert.getConcertType().getTypeId())
                    .orElseThrow(() -> new RuntimeException("ConcertType not found"));
            updatedConcert.setConcertType(concertType);

            ConcertPlace concertPlace = concertPlaceRepository.findById(updatedConcert.getConcertPlace().getPlaceId())
                    .orElseThrow(() -> new RuntimeException("ConcertPlace not found"));
            updatedConcert.setConcertPlace(concertPlace);

            ConcertStatus concertStatus = concertStatusRepository.findById(updatedConcert.getConcertStatus().getStatusId())
                    .orElseThrow(() -> new RuntimeException("ConcertStatus not found"));
            updatedConcert.setConcertStatus(concertStatus);

            return concertRepository.save(updatedConcert);
        }return null;  // 若 ID 不存在，返回 null
    }


    // 地區查詢
    public List<Concert> findConcertsByRegion(String locRegion) {
        return concertRepository.findByConcertPlaceLocRegion(locRegion);
    }

    // 狀態查詢
    public List<Concert> findConcertsByStatus(String statusName) {
        return concertRepository.findByConcertStatusStatusName(statusName);
    }

    // 日期範圍查詢
    public List<Concert> findConcertsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return concertRepository.findByConcertDateBetween(startDate, endDate);
    }

    
    // 查詢活動類型
    public List<Concert> findConcertsByType(String typeName) {
        return concertRepository.findByConcertTypeTypeName(typeName);
    }
    
    // 地區 類型 狀態查詢
    public List<Concert> searchConcerts(String locRegion, String typeName, String statusName) {
        if (locRegion != null && typeName != null && statusName != null) {
            return concertRepository.findByConcertPlaceLocRegionAndConcertTypeTypeNameAndConcertStatusStatusName(locRegion, typeName, statusName);
        } else if (locRegion != null && typeName != null) {
            return concertRepository.findByConcertPlaceLocRegionAndConcertTypeTypeName(locRegion, typeName);
        } else if (locRegion != null && statusName != null) {
            return concertRepository.findByConcertPlaceLocRegionAndConcertStatusStatusName(locRegion, statusName);
        } else if (typeName != null && statusName != null) {
            return concertRepository.findByConcertTypeTypeNameAndConcertStatusStatusName(typeName, statusName);
        } else if (locRegion != null) {
            return concertRepository.findByConcertPlaceLocRegion(locRegion);
        } else if (typeName != null) {
            return concertRepository.findByConcertTypeTypeName(typeName);
        } else if (statusName != null) {
            return concertRepository.findByConcertStatusStatusName(statusName);
        } else {
            return concertRepository.findAll();  // 如果都不傳入，則返回所有活動
        }
   
}
    
    // 查詢所有上架活動，顯示在用戶的活動列表中
    public List<Concert> getVisibleConcerts() {
        return concertRepository.findByViewStatus("上架");
    }
	 
    // 上架活動
    public boolean publishConcert(Integer concertId) {
        return updateViewStatus(concertId, "上架");
    }

    // 下架活動
    public boolean unpublishConcert(Integer concertId) {
        return updateViewStatus(concertId, "下架");
    }

    // 假刪除活動
    public boolean softDeleteConcert(Integer concertId) {
        return updateViewStatus(concertId, "刪除");
    }

    // 恢復活動（設置為上架）
    public boolean restoreConcert(Integer concertId) {
        return updateViewStatus(concertId, "上架");
    }

    // 更新活動的 viewStatus
    private boolean updateViewStatus(Integer id, String newStatus) {
        Optional<Concert> concertOpt = concertRepository.findById(id);
        if (concertOpt.isPresent()) {
            Concert concert = concertOpt.get();
            concert.setViewStatus(newStatus);
            concertRepository.save(concert);
            return true;
        }
        return false; // 若 ID 不存在，返回 false
    }
}
