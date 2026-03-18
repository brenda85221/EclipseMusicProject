package team5.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team5.forum.model.Report;

import java.util.List;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository reportRepo;

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public List<Report> getReportsByStatus(Integer status) {
        return reportRepo.findByReportSuccess(status);
    }

    public Report getReportById(int id) {
        return reportRepo.findById(id).orElse(null);
    }

    public Report addReport(Report report) {
        return reportRepo.save(report);
    }

    public Report getReportById(Integer id) {
        return reportRepo.findById(id).orElse(null);
    }

    public Boolean getReportByAcctNArticleId(String acct, Integer articleId) {
        List<Report> isExist = reportRepo.findByAcctAndArticle_ArticleId(acct, articleId);
        if (isExist == null || isExist.isEmpty()) {
            return false;
        }
        return true;
    }

    public Report updateReport(Report report) {
        return reportRepo.save(report);
    }


}
