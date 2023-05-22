package BugTracker.bugket.models;

import java.time.LocalDate;

public class Report {
    private int reportId;
    private String title;
    private String issueDescription;
    private String replicationInstructions;
    private LocalDate postDate;
    private int voteCount;
    private boolean completionStatus;
    private String authorUsername;

    public Report() {

    }

    public Report(String title, String issueDescription,
                  String replicationInstructions, LocalDate postDate,
                  Boolean completionStatus, String authorUsername
                  ){
        this.title = title;
        this.issueDescription = issueDescription;
        this.replicationInstructions = replicationInstructions;
        this.postDate = postDate;
        this.completionStatus = completionStatus;
        this.authorUsername = authorUsername;
    }


    public Report(int reportId, String title, String issueDescription,
                  String replicationInstructions, LocalDate postDate,
                  int voteCount, boolean completionStatus, String authorUsername) {
        this.reportId = reportId;
        this.title = title;
        this.issueDescription = issueDescription;
        this.replicationInstructions = replicationInstructions;
        this.postDate = postDate;
        this.voteCount = voteCount;
        this.completionStatus = completionStatus;
        this.authorUsername = authorUsername;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getReplicationInstructions() {
        return replicationInstructions;
    }

    public void setReplicationInstructions(String replicationInstructions) {
        this.replicationInstructions = replicationInstructions;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(boolean completionStatus) {
        this.completionStatus = completionStatus;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

}
