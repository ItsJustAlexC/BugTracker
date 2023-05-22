package BugTracker.bugket.data;

import BugTracker.bugket.models.TempAppUserDetails;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class TempAppUserDetailsMapper implements RowMapper<TempAppUserDetails> {

    @Override
    public TempAppUserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TempAppUserDetails(
                rs.getString("username"),
                rs.getString("name")
        );
    }
}
