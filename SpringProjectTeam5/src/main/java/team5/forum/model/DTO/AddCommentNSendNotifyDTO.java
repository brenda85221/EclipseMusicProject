package team5.forum.model.DTO;


public class AddCommentNSendNotifyDTO {

    private String acct; // 按讚的人
    private Integer articleId;
    private String articleAcct; // 被按讚的人
    private String message;
    private String commentContent;

    public AddCommentNSendNotifyDTO() {
    }

    public AddCommentNSendNotifyDTO(String acct, Integer articleId, String articleAcct, String message, String commentContent) {
        this.acct = acct;
        this.articleId = articleId;
        this.articleAcct = articleAcct;
        this.message = message;
        this.commentContent = commentContent;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getArticleAcct() {
        return articleAcct;
    }

    public void setArticleAcct(String articleAcct) {
        this.articleAcct = articleAcct;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
