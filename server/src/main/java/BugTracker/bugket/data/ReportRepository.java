package BugTracker.bugket.data;

import BugTracker.bugket.models.Report;

import java.util.List;

public interface ReportRepository {
    List<Report> findAll();

    List<Report> findIncomplete();

    List<Report> findByUsername(String username);

    Report create(Report report);

    List<Report> findByVote(String username);

    boolean updateStatus(int id, boolean status);
}
