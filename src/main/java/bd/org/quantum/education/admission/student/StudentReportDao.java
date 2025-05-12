package bd.org.quantum.education.admission.student;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentReportDao {
    private final JdbcTemplate jdbcTemplate;

    public StudentReportDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StudentDto> getStudentList(StudentReportCriteria criteria) {
        StringBuilder qb = new StringBuilder(100);
        List<Object> params = new ArrayList<>();

        qb.append("SELECT ap.name_en, ap.gender, ap.community, ap.religion, ap.birth_date, ap.image_path, ");
        qb.append("st.student_id, cls.name AS cls, ins.name AS institution, st.residence_name AS residence, st.status ");
        qb.append("FROM education.adm_applicant AS ap ");
        qb.append("JOIN education.adm_student AS st ON ap.id = st.applicant ");
        qb.append("JOIN education.aca_academic_class AS ac ON st.current_academic_class_id = ac.id ");
        qb.append("JOIN education.aca_institution AS ins ON ac.institution_id = ins.id ");
        qb.append("JOIN education.aca_class AS cls ON ac.class_id = cls.id WHERE 1 = 1 ");
        if (criteria != null) {
            if (criteria.getSession() != null) {
                qb.append("AND ac.session = ? ");
                params.add(criteria.getSession());
            }
            if (criteria.getGender() != null) {
                qb.append("AND ap.gender = ? ");
                params.add(criteria.getGender());
            }
            if (criteria.getInstitution() != null) {
                qb.append("AND ac.institution_id = ? ");
                params.add(criteria.getInstitution());
            }
            if (criteria.getCls() != null) {
                qb.append("AND ac.class_id = ? ");
                params.add(criteria.getCls());
            }
            if (criteria.getResidence() != null) {
                qb.append("AND st.residence_id = ? ");
                params.add(criteria.getResidence());
            }
            if(!criteria.getReligion().equals("0")) {
                qb.append("AND ap.religion = ? ");
                params.add(criteria.getReligion());
            }
            if (!criteria.getCommunity().equals("0")) {
                qb.append("AND ap.community = ? ");
                params.add(criteria.getCommunity());
            }
            if (!criteria.getStatus().equals("0")) {
                qb.append("AND st.status = ? ");
                params.add(criteria.getStatus());
            }
            if (!criteria.getDistrict().equals("0")) {
                qb.append("AND ap.present_district = ? ");
                params.add(criteria.getDistrict());
            }
            if (!criteria.getUpazila().equals("0")) {
                qb.append("AND ap.present_upazila = ? ");
                params.add(criteria.getUpazila());
            }
            if (!criteria.getUnion().equals("0")) {
                qb.append("AND ap.present_union = ? ");
                params.add(criteria.getUnion());
            }
        }

        List<StudentDto> list = jdbcTemplate.query(qb.toString(), params.toArray(), (rs, i) -> {
            StudentDto student = new StudentDto();
            student.setNameEn(rs.getString("name_en"));
            student.setGender(rs.getString("gender"));
            student.setCommunity(rs.getString("community"));
            student.setReligion(rs.getString("religion"));
            student.setDob(rs.getString("birth_date"));
            student.setImagePath(rs.getString("image_path"));
            student.setStudentId(rs.getString("student_id"));
            student.setCls(rs.getString("cls"));
            student.setInstitution(rs.getString("institution"));
            student.setResidence(rs.getString("residence"));
            student.setStatus(rs.getString("status"));
            return student;
        });

        return list;
    }
}
