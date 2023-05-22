package BugTracker.bugket.domain;

import BugTracker.bugket.data.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    private final VoteRepository repository;

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public boolean checkVoters(String username, int reportId) {
        List<String> voters = repository.checkVoters(reportId);
        return voters.contains(username);
    }
    public Result<Void> addVote(String username, int reportId) {
        Result<Void> result = new Result<>();
        if (checkVoters(username, reportId)) {
            result.addMessage(ActionStatus.DUPLICATE, "You have already voted on this report");
            return result;
        }
        boolean success = repository.addVote(username, reportId);
        if (!success) {
            result.addMessage(ActionStatus.INVALID, "Failed to add vote");
            return result;
        }
        return result;
    }

    public Result<Void> deleteVote(String username, int reportId) {
        Result<Void> result = new Result<>();
        if (!checkVoters(username, reportId)) {
            result.addMessage(ActionStatus.NOT_FOUND, "You have not yet voted on this report");
            return result;
        }
        boolean success = repository.deleteVote(username, reportId);
        if (!success) {
            result.addMessage(ActionStatus.INVALID, "Failed to delete vote");
            return result;
        }
        return result;
    }
}
