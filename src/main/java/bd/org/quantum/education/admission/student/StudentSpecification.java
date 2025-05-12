package bd.org.quantum.education.admission.student;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class StudentSpecification {

    public static Specification<Student> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeNameEn =
                    builder.like(
                            builder.lower(root.get(Student_.applicant).get("nameEn")),
                            contains(omniText.toLowerCase()));

            Predicate likeNameBn =
                    builder.like(
                            builder.lower(root.get(Student_.applicant).get("nameBn")),
                            contains(omniText));

            Predicate equalStudentId =
                    builder.like(
                            builder.lower(root.get(Student_.studentId)), contains(omniText));

            return builder.or(likeNameBn, likeNameEn, equalStudentId);
        };
    }

    public static Specification<Student> institutionEquals(Long institution) {
        if (institution == null || institution == 0) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.institutionId).get("id"), institution);
    }

    public static Specification<Student> classEquals(String clazz) {
        if (clazz == null || clazz.equals("0")) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.CURRENT_ACADEMIC_CLASS_INFO), contains(clazz));
    }

    public static Specification<Student> genderEquals(String gender) {
        if (gender == null || gender.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.applicant).get("gender"), gender);
    }

    public static Specification<Student> religionEquals(String religion) {
        if (religion == null || religion.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.applicant).get("religion"), religion);
    }

    public static Specification<Student> communityEquals(String community) {
        if (community == null || community.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.applicant).get("community"), community);
    }

    public static Specification<Student> examCenterEquals(String examCenter) {
        if (examCenter == null || examCenter.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.applicant).get("examCenter"), examCenter);
    }

    public static Specification<Student> districtEquals(String district) {
        if (district == null || district.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.applicant).get("presentDistrict"), district);
    }

    public static Specification<Student> upazilaEquals(String upazila) {
        if (upazila == null || upazila.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.applicant).get("presentUpazila"), upazila);
    }

    public static Specification<Student> sessionEquals(String session) {
        if (session == null || session.isEmpty()) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.applicant).get("session"), session);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
