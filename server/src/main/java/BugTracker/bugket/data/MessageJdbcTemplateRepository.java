package BugTracker.bugket.data;

import BugTracker.bugket.models.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class MessageJdbcTemplateRepository implements MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public MessageJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Message> findAllForReport(int reportId) {
        final String sql = """
                select message_id, message, post_date, ru.username
                from messages m
                inner join registered_user ru on m.user_id = ru.user_id
                inner join reports r on m.report_id = r.report_id
                where r.report_id = ?;
                """;
        return jdbcTemplate.query(sql, new MessageMapper(), reportId);
    }

    @Override
    public Message create(Message message) {
        final String sql = """
                INSERT into messages (message, post_date, user_id, report_id)
                values (?,?,(SELECT user_id from registered_user where username = ?),?);
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, message.getMessage());
            ps.setDate(2, java.sql.Date.valueOf(message.getPostDate()));
            ps.setString(3, message.getAuthorUsername());
            ps.setInt(4, message.getReportId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        message.setMessageId(keyHolder.getKey().intValue());
        return message;
    }

    @Override
    public boolean deleteMessage(int messageId) {
        final String sql = """
                DELETE from messages
                where message_id = ?
                """;
        return jdbcTemplate.update(sql, messageId) > 0;
    }
}
