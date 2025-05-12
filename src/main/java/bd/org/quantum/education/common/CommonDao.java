package bd.org.quantum.education.common;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDao {
    private final JdbcTemplate jdbcTemplate;

    public CommonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setAdmittedApplicant(Long id) {
        String sql = "UPDATE education.adm_applicant SET admitted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
