package BugTracker.bugket.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VoteJdbcTemplateRepositoryTest {
    @Autowired
    VoteJdbcTemplateRepository repository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    static boolean hasRun = false;

    @BeforeEach
    void setup(){
        if(!hasRun){
            hasRun = true;
            jdbcTemplate.update("call set_known_good_state();");
        }
    }

    @Test
    void shouldGetVoters() {
        List<String> voters = repository.checkVoters(2);
        assertEquals(2, voters.size());
    }

    @Test
    void shouldNotGetVotersForMissing() {
        List<String> voters = repository.checkVoters(1000);
        assertEquals(0, voters.size());
    }

    @Test
    void shouldAddVote() {
        assertTrue(repository.addVote("test", 3));
        assertTrue(repository.checkVoters(3).contains("test"));
    }

    @Test
    void shouldDeleteVote() {
        assertTrue(repository.deleteVote("admin", 1));
        assertFalse(repository.checkVoters(1).contains("admin"));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(repository.deleteVote("not a user", 1));
        assertFalse(repository.deleteVote("admin", 10000));
    }

}