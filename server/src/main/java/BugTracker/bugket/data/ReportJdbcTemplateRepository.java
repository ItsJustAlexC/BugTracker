package BugTracker.bugket.data;

import BugTracker.bugket.models.Report;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ReportJdbcTemplateRepository implements ReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReportJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Report> findAll() {
        final String sql = """
                SELECT report_id, title, issue_description, replication_instructions, date_of_reporting, completion_status, ru.username,
                    (SELECT count(user_id) from votes
                    where report_id = r.report_id
                    group by report_id)  vote_count
                from reports r
                inner join registered_user ru on r.user_id = ru.user_id;
                """;
        return jdbcTemplate.query(sql, new ReportMapper());
    }

    @Override
    public List<Report> findIncomplete() {
        final String sql = """
                SELECT report_id, title, issue_description, replication_instructions, date_of_reporting, completion_status, ru.username,
                    (SELECT count(user_id) from votes
                    where report_id = r.report_id
                    group by report_id)  vote_count
                from reports r
                inner join registered_user ru on r.user_id = ru.user_id
                where completion_status = 0;
                """;
        return jdbcTemplate.query(sql, new ReportMapper());
    }

    @Override
    public List<Report> findByUsername(String username){
        final String sql = """
                SELECT report_id, title, issue_description, replication_instructions, date_of_reporting, completion_status, ru.username,
                    (SELECT count(user_id) from votes
                    where report_id = r.report_id
                    group by report_id)  vote_count
                from reports r
                join registered_user ru on r.user_id = ru.user_id
                where ru.username = ?;
                """;
        return jdbcTemplate.query(sql, new ReportMapper(), username);
    }

    @Override
    public List<Report> findByVote(String username){
        final String sql = """
                SELECT r.report_id, title, issue_description, replication_instructions, date_of_reporting, completion_status, ru.username,
                    (SELECT count(user_id) from votes
                    where report_id = r.report_id
                    group by report_id)  vote_count
                from reports r
                inner join registered_user ru on r.user_id = ru.user_id
                inner join votes v on r.report_id =  v.report_id
                inner join registered_user u on v.user_id = u.user_id
                where u.username = ?;
                """;
        return jdbcTemplate.query(sql, new ReportMapper(), username);
    }

    @Override
    public Report create(Report report) {
        final String sql = """
                INSERT into reports (title, issue_description, replication_instructions, date_of_reporting, completion_status, user_id)
                values (?, ?, ?, ?, ?, (SELECT user_id from registered_user where username = ?));
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, report.getTitle());
            ps.setString(2, report.getIssueDescription());
            ps.setString(3, report.getReplicationInstructions());
            ps.setDate(4,java.sql.Date.valueOf(report.getPostDate()));
            ps.setBoolean(5, report.isCompletionStatus());
            ps.setString(6, report.getAuthorUsername());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0){
            return null;
        }

        report.setReportId(keyHolder.getKey().intValue());
        return report;
    }

    @Override
    public boolean updateStatus(int id, boolean status) {
        final String sql = """
                UPDATE reports set
                completion_status = ?
                where report_id = ?;
                """;

        return jdbcTemplate.update(sql,
                status, id) > 0;
    }
}
