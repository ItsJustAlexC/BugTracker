package BugTracker.bugket.data;

import BugTracker.bugket.models.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int i) throws SQLException {
        return new Message(
                rs.getInt("message_id"),
                rs.getString("message"),
                rs.getDate("post_date").toLocalDate(),
                rs.getString("username")
        );
    }
}
