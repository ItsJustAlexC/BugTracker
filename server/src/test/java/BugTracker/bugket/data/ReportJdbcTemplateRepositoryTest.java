package BugTracker.bugket.data;

import BugTracker.bugket.models.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportJdbcTemplateRepositoryTest {

    @Autowired
    ReportJdbcTemplateRepository repository;

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
    void shouldFindAll() {
        List<Report> reports = repository.findAll();
        assertTrue(3 <= reports.size());
    }

    @Test
    void shouldFindIncomplete() {
        List<Report> reports = repository.findIncomplete();
        assertTrue(2 <= reports.size());
    }

    @Test
    void shouldFindAllByUsername(){
        List<Report> reports = repository.findByUsername("admin");
        assertTrue(2 <= reports.size());

        reports = repository.findByUsername("test");
        assertTrue(1 <= reports.size());
    }

    @Test
    void shouldNotFindByMissingUsername() {
        List<Report> reports = repository.findByUsername("missing");
        assertEquals(0, reports.size());
    }

    @Test
    void shouldFindByVote() {
        List<Report> reports = repository.findByVote("admin");
        assertTrue(2 <= reports.size());

        List<Report> reports2 = repository.findByUsername("test");
        assertTrue(1 <= reports2.size());
    }

    @Test
    void shouldNotFindMissingUsernameVote() {
        List<Report> reports2 = repository.findByUsername("missing");
        assertEquals(0, reports2.size());
    }

    @Test
    void shouldPopulateAllFields() {
        List<Report> reports = repository.findAll();
        for (Report report : reports) {
            assertNotNull(report.getReportId());
            assertNotNull(report.getTitle());
            assertNotNull(report.getIssueDescription());
            assertNotNull(report.getReplicationInstructions());
            assertNotNull(report.getPostDate());
            assertNotNull(report.getVoteCount());
            assertNotNull(report.getAuthorUsername());
        }
    }

    @Test
    void shouldCreate() {

        Report report = new Report(
                "test", "test", "test", LocalDate.of(2023,01,01), false, "test"
        );

        Report actual = repository.create(report);
        assertNotNull(actual);
        assertEquals("test", actual.getTitle());
        assertEquals(4, actual.getReportId());
        assertEquals("test", actual.getAuthorUsername());
    }

    @Test
    void shouldEditStatus() {
        boolean result = repository.updateStatus(2, true);
        assertTrue(result);

        result = repository.updateStatus(2, false);
        assertTrue(result);
    }

    @Test
    void shouldNotEditMissingReport() {
        boolean result = repository.updateStatus(100000, true);
        assertFalse(result);
    }
}