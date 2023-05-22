package BugTracker.bugket.data;

import java.util.List;

public interface VoteRepository {
    List<String> checkVoters(int reportId);

    boolean addVote(String username, int reportId);

    boolean deleteVote(String username, int reportId);
}
