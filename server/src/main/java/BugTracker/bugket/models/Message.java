package BugTracker.bugket.models;

import java.time.LocalDate;

public class Message {
    private int messageId;
    private String message;
    private LocalDate postDate;
    private String authorUsername;
    private int reportId;

    public Message() {

    }

    public Message(String message, LocalDate postDate, String authorUsername, int reportId) {
        this.message = message;
        this.postDate = postDate;
        this.authorUsername = authorUsername;
        this.reportId = reportId;
    }

    public Message(int messageId, String message, LocalDate postDate, String authorUsername) {
        this.messageId = messageId;
        this.message = message;
        this.postDate = postDate;
        this.authorUsername = authorUsername;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
}

