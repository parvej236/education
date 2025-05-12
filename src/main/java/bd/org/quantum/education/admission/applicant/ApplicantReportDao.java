package bd.org.quantum.education.admission.applicant;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ApplicantReportDao {
    private final JdbcTemplate jdbcTemplate;

    public ApplicantReportDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Applicant> getApplicantList(ApplicantReportCriteria criteria) {
        StringBuilder qb = new StringBuilder(100);
        List<Object> params = new ArrayList<>();

        qb.append("SELECT name_en, gender, religion, community, form_no, applied_class, session, ");
        qb.append("exam_center, present_address, present_union, present_upazila, present_district, ");
        qb.append("selected, admitted, image_path FROM education.adm_applicant WHERE 1 = 1 ");
        if (criteria != null) {
            if (criteria.getGender() != null) {
                    qb.append("AND gender = ? ");
                    params.add(criteria.getGender());
            }
            if (criteria.getSession() != null) {
                qb.append("AND session = ? ");
                params.add(criteria.getSession());
            }
            if (!criteria.getCls().equals("0")) {
                qb.append("AND applied_class = ? ");
                params.add(criteria.getCls());
            }
            if (!criteria.getReligion().equals("0")) {
                qb.append("AND religion = ? ");
                params.add(criteria.getReligion());
            }
            if (!criteria.getCommunity().equals("0")) {
                qb.append("AND community = ? ");
                params.add(criteria.getCommunity());
            }
            if(!criteria.getExamCenter().equals("0")){
                qb.append("AND exam_center = ? ");
                params.add(criteria.getExamCenter());
            }
            if(!criteria.getDistrict().equals("0")){
                qb.append("AND present_district = ? ");
                params.add(criteria.getDistrict());
            }
            if(!criteria.getUpazila().equals("0")){
                qb.append("AND present_upazila = ? ");
                params.add(criteria.getUpazila());
            }
            if(!criteria.getUnion().equals("0")){
                qb.append("AND present_union = ? ");
                params.add(criteria.getUnion());
            }
            if(criteria.getIsSelected() != null){
                qb.append("AND selected = ? ");
                params.add(criteria.getIsSelected());
            }
            if(criteria.getIsAdmitted() != null){
                qb.append("AND admitted = ? ");
                params.add(criteria.getIsAdmitted());
            }
        }

        List<Applicant> list = jdbcTemplate.query(qb.toString(), params.toArray(), (rs, i) -> {
            Applicant applicant = new Applicant();
            applicant.setFormNo(rs.getString("form_no"));
            applicant.setAppliedClass(rs.getString("applied_class"));
            applicant.setSession(rs.getString("session"));
            applicant.setExamCenter(rs.getString("exam_center"));
            applicant.setNameEn(rs.getString("name_en"));
            applicant.setReligion(rs.getString("religion"));
            applicant.setCommunity(rs.getString("community"));
            applicant.setGender(rs.getString("gender"));
            applicant.setPresentDistrict(rs.getString("present_district"));
            applicant.setPresentUpazila(rs.getString("present_upazila"));
            applicant.setPresentUnion(rs.getString("present_union"));
            applicant.setPresentAddress(rs.getString("present_address"));
            applicant.setSelected(rs.getBoolean("selected"));
            applicant.setAdmitted(rs.getBoolean("admitted"));
            applicant.setImagePath(rs.getString("image_path"));
            return applicant;
        });

        return list;
    }
}
