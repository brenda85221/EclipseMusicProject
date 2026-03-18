package team5.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team5.forum.model.Article;
import team5.forum.model.DTO.AddReportNSendNotifyDTO;
import team5.forum.model.Report;
import team5.forum.service.ArticleService;
import team5.forum.service.NotificationService;
import team5.forum.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/forum/report")
//@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getAll")
    public List<Report> processQueryAllReports(){
        return reportService.getAllReports();
    }

    @GetMapping("/getByStatus/{status}")
    public List<Report> processQueryReportsByStatus(@PathVariable("status") Integer status){
        return reportService.getReportsByStatus(status);
    }

    @GetMapping("/get/{id}")
    public Report processQueryReportById(@PathVariable("id") Integer id){
        return reportService.getReportById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReport(@RequestBody AddReportNSendNotifyDTO addReportDTO){
        Article article = articleService.getArticleById(addReportDTO.getArticleId());
        System.out.println("檢舉原因 = " + addReportDTO.getReportReason());
        Report report = new Report(addReportDTO.getAcct(), article, addReportDTO.getReportReason());
        reportService.addReport(report);
        // 發送通知給文章作者
        notificationService.notifyUser(addReportDTO.getArticleAcct(), addReportDTO.getMessage(), addReportDTO.getArticleId());
        System.out.println("訊息發送成功給：" + addReportDTO.getArticleAcct());
        return ResponseEntity.ok("文章檢舉紀錄新增成功");
    }

    @PutMapping("/update")
    public String processUpdateReport(Report reportBean){
        Report report = reportService.updateReport(reportBean);
        if(report == null){
            return "檢舉事件更新失敗";
        }
        return "檢舉事件更新成功，檢舉編號為 " + report.getReportId();
    }

    @PutMapping("/update/{reportId}/{status}")
    public String processUpdateReportStatus(@PathVariable("reportId") Integer reportId, @PathVariable("status") Integer status){
        Report report = reportService.getReportById(reportId);
        if(report != null){
            report.setReportSuccess(status);
            reportService.updateReport(report);
            return "檢舉編號：" + report.getReportId() + " 更新狀態成功";
        }
        return "查無檢舉編號，更新狀態失敗";
    }

    @GetMapping("/get/isExist")
    public Boolean checkIfReportExist(@RequestParam String acct, @RequestParam Integer articleId){
        return reportService.getReportByAcctNArticleId(acct, articleId);
    }



}
