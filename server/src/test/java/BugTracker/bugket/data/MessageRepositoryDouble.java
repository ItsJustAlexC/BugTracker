package BugTracker.bugket.data;

import BugTracker.bugket.models.Message;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryDouble implements MessageRepository {
    public List<Message> all = new ArrayList<>();

    public MessageRepositoryDouble()  {
        all.add(new Message(1, "It's not a bug, it's a feature", LocalDate.of(2022, 12, 10), "test"));
        all.add(new Message(2,"Have you tried turning it off and back on again?", LocalDate.of(2022, 11, 24), "admin"));
    }

    @Override
    public List<Message> findAllForReport(int reportId) {
        return all;
    }

    @Override
    public Message create(Message message) {
        return message;
    }

    @Override
    public boolean deleteMessage(int messageId) {
        for (int i = 0; i < all.size(); i++) {
            if (messageId == all.get(i).getMessageId()) {
                return true;
            }
        }
        return false;
    }
}
