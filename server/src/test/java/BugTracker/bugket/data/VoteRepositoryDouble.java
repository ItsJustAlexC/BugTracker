package BugTracker.bugket.data;

import BugTracker.bugket.models.Report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VoteRepositoryDouble implements VoteRepository {

    Report report = new Report();
    public VoteRepositoryDouble() {
        report = new Report(1,"Computer Turns off", "The computer shuts down randomly", "Hold down the power button for long enough",
                LocalDate.of(2022, 10, 20), 2,false, "test");
    }

    @Override
    public List<String> checkVoters(int reportId) {
        ArrayList<String> voters = new ArrayList<>();
        voters.add("admin");
        return voters;
    }

    @Override
    public boolean addVote(String username, int reportId) {
        if (report.getReportId() == reportId && report.getAuthorUsername().equals(username)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteVote(String username, int reportId) {
        if(report.getReportId() == reportId && !report.getAuthorUsername().equals(username)) {
            return true;
        }
        return false;
    }
}
