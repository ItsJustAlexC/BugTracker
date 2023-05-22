package BugTracker.bugket.domain;

import BugTracker.bugket.data.MessageRepository;
import BugTracker.bugket.models.Message;
import BugTracker.bugket.data.MessageRepositoryDouble;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceTest {

    MessageRepository repository = new MessageRepositoryDouble();
    MessageService service = new MessageService(repository);

    @Test
    void shouldFindAll() {
        List<Message> expected = repository.findAllForReport(1);
        List<Message> actual = service.getMessagesById(1);

        assertEquals(expected.size(), actual.size());
    }

    @Test
    void shouldCreate() {
        Message message = new Message("It's not a bug if your computer is broken", LocalDate.of(2023, 1, 11), "admin", 1);
        Result<Message> result = service.add(message);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotCreateInvalidMessage() {
        Message message = new Message(null, LocalDate.of(2023, 1,11), "admin", 1);
        Result<Message> result = service.add(message);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotCreateSpamMessage() {
        Message message = new Message(
                "asdifoskenrtudhewcjeislarudownsilfesjlrod a bad idea",
                LocalDate.of(2023, 1,11), "admin", 1
        );
        Result<Message> result = service.add(message);
        assertFalse(result.isSuccess());

        message.setMessage(
                "a asdifo@kenr2udhewBjeisSarud)wns.lfes\"lrod a bad idea"
        );
        Result<Message> result2 = service.add(message);
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldDelete() {
        boolean expected = repository.deleteMessage(1);
        Result actual = service.delete(1);
        assertEquals(expected, actual.isSuccess());
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotDeleteMissingMessage() {
        Result actual = service.delete(99);
        assertFalse(actual.isSuccess());
    }

}