package BugTracker.bugket.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VoteJdbcTemplateRepository implements VoteRepository {
    private final JdbcTemplate jdbcTemplate;


    public VoteJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> checkVoters(int reportId) {
        final String sql = """
                SELECT u.username from votes v
                inner join registered_user u on v.user_id = u.user_id
                where v.report_id = ?;
                """;

        return jdbcTemplate.query(sql, (rs, row) -> rs.getString("username"), reportId);
    }

    @Override
    public boolean addVote(String username, int reportId) {
        final String sql = """
                INSERT into votes(user_id, report_id) values
                ((SELECT user_id from registered_user where username = ?), ?);
                """;

        return jdbcTemplate.update(sql, username, reportId) > 0;
    }

    @Override
    public boolean deleteVote(String username, int reportId) {
        final String sql = """
                DELETE from votes
                where report_id = ?
                and user_id = (SELECT user_id from registered_user where username = ?);
                """;

        return jdbcTemplate.update(sql, reportId, username) > 0;
    }
}
