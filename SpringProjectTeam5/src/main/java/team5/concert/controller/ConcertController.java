package team5.concert.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import team5.concert.model.ConcertService;
import team5.concert.model.bean.Concert;

//@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/concerts")
public class ConcertController {
	
	@Autowired
	private ConcertService concertService;
	
	@GetMapping("/search/keyword")
	public ResponseEntity<List<Concert>> searchByKeyword(@RequestParam String keyword) {
	    List<Concert> results = concertService.searchByKeyword(keyword);

	    if (results.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.ok(results);
	}
	
    // 查詢所有
    @GetMapping("/")  // http://localhost:8080/concerts/
    public ResponseEntity<List<Concert>> getAllConcerts() {
        List<Concert> concerts = concertService.getAll();
        return new ResponseEntity<>(concerts, HttpStatus.OK);
    }

    //  ID 查詢
    @GetMapping("/{concertId}")  // http://localhost:8080/concerts/1
    public ResponseEntity<Concert> getConcertById(@PathVariable Integer concertId) {
        Optional<Concert> concert = concertService.getById(concertId);
        return concert.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // 地區查詢
    @GetMapping("/search/region") // http://localhost:8080/concerts/search/region?locRegion=北區
    public ResponseEntity<List<Concert>> getConcertsByRegion(@RequestParam String locRegion) {
        List<Concert> concerts = concertService.findConcertsByRegion(locRegion);
        return new ResponseEntity<>(concerts, HttpStatus.OK);
    }

    // 狀態查詢
    @GetMapping("/search/status") // http://localhost:8080/concerts/search/status?statusName=未開始
    public ResponseEntity<List<Concert>> getConcertsByStatus(@RequestParam String statusName) {
        List<Concert> concerts = concertService.findConcertsByStatus(statusName);
        return new ResponseEntity<>(concerts, HttpStatus.OK);
    }

    // 日期範圍查詢
    @GetMapping("/search/date")
    public ResponseEntity<List<Concert>> getConcertsByDateRange(
        @RequestParam String startDate, @RequestParam String endDate) {

        // 為日期補充時間部分，默認設置為 "00:00:00"
        String startDateTimeStr = startDate + " 00:00:00";
        String endDateTimeStr = endDate + " 23:59:59"; // 默認設置結束時間為當天的最後一刻

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeStr, formatter);

        List<Concert> concerts = concertService.findConcertsByDateRange(startDateTime, endDateTime);
        return new ResponseEntity<>(concerts, HttpStatus.OK);
    }


    
    // 活動類型查詢
    @GetMapping("/search/type")
    public ResponseEntity<List<Concert>> getConcertsByType(@RequestParam String typeName) {
        List<Concert> concerts = concertService.findConcertsByType(typeName);
        if (concerts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(concerts);
    }

    // 地區 類型 狀態查詢
    @GetMapping("/search")
    public ResponseEntity<List<Concert>> searchConcerts(
            @RequestParam(required = false) String locRegion,
            @RequestParam(required = false) String typeName,
            @RequestParam(required = false) String statusName) {

        List<Concert> concerts = concertService.searchConcerts(locRegion, typeName, statusName);

        if (concerts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(concerts);
    }
    
    // 新增活動
    @PostMapping("/")
    public ResponseEntity<Concert> createConcert(@RequestBody Concert concert) {
   
        Concert createdConcert = concertService.create(concert);
        return new ResponseEntity<>(createdConcert, HttpStatus.CREATED);
    }

    // 更新活動
    @PutMapping("/{concertId}")
    public ResponseEntity<Concert> updateConcert(@PathVariable Integer concertId, @RequestBody Concert updatedConcert) {
        Concert concert = concertService.update(concertId, updatedConcert);
        return (concert != null)
                ? new ResponseEntity<>(concert, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    // 刪除活動
//    @DeleteMapping("/{concertId}")
//    public ResponseEntity<Map<String, String>> deleteConcert(@PathVariable Integer concertId) {
//        boolean isDeleted = concertService.deleteConcert(concertId);
//
//        if (isDeleted) {
//            // 刪除成功，返回成功訊息
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "刪除成功");
//            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
//        } else {
//            // 刪除失敗，返回錯誤訊息
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "刪除失敗");
//            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//        }     
//        
//    }
    
    // 上架活動
    @PutMapping("/{concertId}/publish")
    public ResponseEntity<Map<String, String>> publishConcert(@PathVariable Integer concertId) {
        boolean isPublished = concertService.publishConcert(concertId);

        Map<String, String> response = new HashMap<>();
        if (isPublished) {
            response.put("message", "活動已上架");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "上架失敗，可能活動不存在");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 下架活動
    @PutMapping("/{concertId}/unpublish")
    public ResponseEntity<Map<String, String>> unpublishConcert(@PathVariable Integer concertId) {
        boolean isUnpublished = concertService.unpublishConcert(concertId);

        Map<String, String> response = new HashMap<>();
        if (isUnpublished) {
            response.put("message", "活動已下架");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "下架失敗，可能活動不存在");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 假刪除活動
    @PutMapping("/{concertId}/delete")
    public ResponseEntity<Map<String, String>> softDeleteConcert(@PathVariable Integer concertId) {
        boolean isDeleted = concertService.softDeleteConcert(concertId);

        Map<String, String> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "活動已設為刪除狀態");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "刪除失敗，可能活動不存在");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // 恢復刪除的活動
    @PutMapping("/{concertId}/restore")
    public ResponseEntity<Map<String, String>> restoreConcert(@PathVariable Integer concertId) {
        boolean isRestored = concertService.restoreConcert(concertId);

        Map<String, String> response = new HashMap<>();
        if (isRestored) {
            response.put("message", "活動已恢復");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "恢復失敗，可能活動不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


 // 查詢所有上架活動
    @GetMapping("/online")
    public ResponseEntity<List<Concert>> getVisibleConcerts() {
        List<Concert> concerts = concertService.getVisibleConcerts();
        
        if (concerts.isEmpty()) {
            // 如果沒有上架活動，回傳 204 No Content
            return ResponseEntity.noContent().build();
        }
        
        // 回傳 JSON 格式的活動清單
        return ResponseEntity.ok(concerts);
    }
}






	
	
   

	 
    
    
    
    
    
    
    





