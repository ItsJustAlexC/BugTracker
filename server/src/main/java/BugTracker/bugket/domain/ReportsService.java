package BugTracker.bugket.domain;

import BugTracker.bugket.models.Report;
import BugTracker.bugket.data.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsService {

    private final ReportRepository repository;


    public ReportsService(ReportRepository repository) {
        this.repository = repository;
    }

    public List<Report> getAll()  {
        return repository.findAll();
    }

    public List<Report> getAllIncomplete(){
        return repository.findIncomplete();
    }

    public List<Report> getByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<Report> getByVote(String username) {
        return repository.findByVote(username);
    }

    public Result<Report> add(Report report){
        Result<Report> result = validate(report);

        if (result.isSuccess()) {
            report = repository.create(report);
            result.setPayload(report);
        }
        return result;
    }

    public Result<Void> updateStatus(int id, boolean status) {
        Result<Void> result = new Result<>();
        if (id <= 0) {
            result.addMessage(ActionStatus.INVALID, "Report ID is invalid");
            return result;
        }
        boolean success = repository.updateStatus(id, status);
        if (!success) {
            result.addMessage(ActionStatus.INVALID, "Failed to update report status");
            return result;
        }
        return result;
    }

    private Result<Report> validateNulls(Report report) {
        Result<Report> result = new Result<>();

        if (report == null) {
            result.addMessage(ActionStatus.INVALID, "Report cannot be null.");
            return result;
        }
        if (report.getTitle() == null || report.getTitle().isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Title is required.");
        }
        if (report.getIssueDescription() == null || report.getIssueDescription().isBlank() ) {
            result.addMessage(ActionStatus.INVALID, "Issue Description is required.");
        }
        if (report.getReplicationInstructions() == null || report.getReplicationInstructions().isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Replication Instructions are required.");
        }
        if (report.getAuthorUsername() == null || report.getAuthorUsername().isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Author username is required .");
        }

        return result;
    }

    private Result<Report> validate(Report report) {
        Result<Report> result = validateNulls(report);
        if(!result.isSuccess()) {
            return result;
        }

        if (report.getTitle().length() > 100) {
            result.addMessage(ActionStatus.INVALID, "Title must be less than 100 characters");
        }
        if(report.getTitle().matches(".*\\S{40,}.*")) {
            result.addMessage(ActionStatus.INVALID, "Please do not spam characters");
        }
        if (report.getIssueDescription().length() > 1024) {
            result.addMessage(ActionStatus.INVALID, "The issue description is too long. " +
                    "Please shorten it and try again");
        }
        if(report.getIssueDescription().matches(".*\\S{40,}.*")) {
            result.addMessage(ActionStatus.INVALID, "Please do not spam characters");
        }
        if (report.getReplicationInstructions().length() > 1024) {
            result.addMessage(ActionStatus.INVALID, "The replication instructions are too long. " +
                    "Please shorten them and try again");
        }
        if(report.getReplicationInstructions().matches(".*\\S{40,}.*")) {
            result.addMessage(ActionStatus.INVALID, "Please do not spam characters");
        }
        return result;
    }

}
