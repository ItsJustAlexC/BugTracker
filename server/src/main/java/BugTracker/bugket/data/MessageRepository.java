package BugTracker.bugket.data;

import BugTracker.bugket.models.Message;

import java.util.List;

public interface MessageRepository {

    List<Message> findAllForReport(int reportId);

    Message create(Message message);

    boolean deleteMessage(int messageId);
}
