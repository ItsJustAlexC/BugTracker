package BugTracker.bugket.data;

import BugTracker.bugket.models.Report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportRepositoryDouble implements ReportRepository{
    public List<Report> all = new ArrayList<>();

    public ReportRepositoryDouble() {
        all.add(new Report(1,"Computer Turns off", "The computer shuts down randomly", "Hold down the power button for long enough",
                LocalDate.of(2022, 10, 20), 2,false, "test"));

        all.add(new Report(2,"Screen Frozen", "The screen freezes at random times", "Plug in a second display and wait",
                LocalDate.of(2023, 1, 3), 3, false, "admin"));
    }

    @Override
    public List<Report> findAll() {
        return all;
    }

    @Override
    public List<Report> findIncomplete() {
        List<Report> reports = new ArrayList<>();
        for(Report report : all) {
            if(report.isCompletionStatus() == false) {
                reports.add(report);
            }
        }
        return reports;
    }

    @Override
    public List<Report> findByUsername(String username) {
        List<Report> reports = new ArrayList<>();
        for (Report report : all) {
            if(report.getAuthorUsername().equals(username)) {
                reports.add(report);
            }
        }
        return reports;
    }

    @Override
    public List<Report> findByVote(String username) {
        List<Report> reports = new ArrayList<>();
       if (username.equals("test")) {
           reports.addAll(all);
       }
       return reports;
    }

    @Override
    public Report create(Report report) {
        return report;
    }

    @Override
    public boolean updateStatus(int id, boolean status) {
        return status;
    }




}
