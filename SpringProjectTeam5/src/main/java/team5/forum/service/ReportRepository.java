package team5.forum.service;

import org.springframework.data.jpa.repository.JpaRepository;
import team5.forum.model.Report;

import java.util.List;


public interface ReportRepository extends JpaRepository<Report,Integer> {
    List<Report> findByReportSuccess(Integer reportSuccess);
    List<Report> findByAcctAndArticle_ArticleId(String acct, Integer articleId);
}
