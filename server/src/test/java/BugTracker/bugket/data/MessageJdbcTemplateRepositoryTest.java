package BugTracker.bugket.data;

import BugTracker.bugket.models.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageJdbcTemplateRepositoryTest {

    @Autowired
    MessageJdbcTemplateRepository repository;

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
    void shouldFindAllForReport() {
        List<Message> messages = repository.findAllForReport(3);
        assertEquals(1, messages.size());
    }

    @Test
    void shouldAddMessage() {
        Message message = new Message(
                "This bug always happens to me too. Not sure why", LocalDate.of(2022,10, 10), "test", 2
        );
        Message actual = repository.create(message);
        assertNotNull(actual);
        assertEquals("test", message.getAuthorUsername());
    }


    @Test
    void shouldDelete() {
        assertTrue(repository.deleteMessage(1));
    }

    @Test
    void shouldNotDeleteMissingMessage() {
        assertFalse(repository.deleteMessage(99));
    }

}