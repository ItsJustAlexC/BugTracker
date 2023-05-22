package BugTracker.bugket.domain;


import BugTracker.bugket.models.Message;
import BugTracker.bugket.data.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public List<Message> getMessagesById(int reportId) {
        return repository.findAllForReport(reportId);
    }

    public Result<Message> add(Message message) {
        Result<Message> result = validate(message);

        if (result.isSuccess()) {
            repository.create(message);
            result.setPayload(message);
        }
        return result;
    }

    public Result<Void> delete(int messageId) {
        Result<Void> result = new Result<>();

        boolean success = repository.deleteMessage(messageId);
        if(!success) {
            result.addMessage(ActionStatus.INVALID, "Failed to delete message");
            return result;
        }
        return result;
    }


    private Result<Message> validate(Message message) {
        Result<Message> result = new Result<>();
        if (message == null) {
            result.addMessage(ActionStatus.INVALID, "Message cannot be null");
            return result;
        }
        if(message.getMessage() == null) {
            result.addMessage(ActionStatus.INVALID, "Message text cannot be null");
            return result;
        }
        if(message.getMessage().matches(".*\\S{40,}.*")) {
            result.addMessage(ActionStatus.INVALID, "Please do not spam characters");
        }

        return result;

    }
}
