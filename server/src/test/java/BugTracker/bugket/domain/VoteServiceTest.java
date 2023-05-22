package BugTracker.bugket.domain;

import BugTracker.bugket.data.VoteRepository;
import BugTracker.bugket.data.VoteRepositoryDouble;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VoteServiceTest {

    VoteRepository repository = new VoteRepositoryDouble();
    VoteService service = new VoteService(repository);

    @Test
    void shouldFindVoter() {
        List<String> expected = repository.checkVoters(1);
        boolean actual = service.checkVoters("admin", 1);
        assertEquals(expected.contains("admin"), actual);
    }

    @Test
    void shouldNotFindMissingVoter() {
        boolean actual = service.checkVoters("test", 1);
        assertFalse(actual);
    }

    @Test
    void shouldAddVote() {
        boolean expected = repository.addVote("test", 1);
        Result actual = service.addVote("test", 1);
        assertEquals(expected, actual.isSuccess());
    }

    @Test
    void shouldNotDuplicateVote() {
        Result actual = service.addVote("admin", 2);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
    }
    @Test
    void shouldDeleteVote() {
        boolean expected = repository.deleteVote("admin",1 );
        Result actual = service.deleteVote("admin", 1);
        assertEquals(expected, actual.isSuccess());
        assertTrue(actual.isSuccess());
    }
    @Test
    void shouldNotDeleteMissingVote() {
        Result actual = service.deleteVote("test", 1);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
    }
}