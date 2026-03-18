package team5.forum.model.DTO;


public class PopularityScoreDTO {
    private Integer articleId;
    private String articleTitle;
    private long visitCount;
    private long likeCount;
    private long commentCount;
    private long popularityScore;

    public PopularityScoreDTO(Integer articleId, String articleTitle, long visitCount, long likeCount, long commentCount, long popularityScore) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.visitCount = visitCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.popularityScore = popularityScore;
    }

    public PopularityScoreDTO() {
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(long visitCount) {
        this.visitCount = visitCount;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public long getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(long popularityScore) {
        this.popularityScore = popularityScore;
    }
}
