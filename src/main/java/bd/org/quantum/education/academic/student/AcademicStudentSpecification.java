package bd.org.quantum.education.academic.student;

import bd.org.quantum.education.admission.student.Student;
import bd.org.quantum.education.admission.student.Student_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

public class AcademicStudentSpecification {
    public static Specification<Student> omniText(String omniText) {
        if (StringUtils.isEmpty(omniText)) {
            return null;
        }
        return (root, query, builder) -> {
            Predicate likeName = builder.like(builder.lower(root.get(Student_.applicant).get("nameEn")), contains(omniText.toLowerCase()));
            Predicate equalQuantaa = builder.like(root.get(Student_.studentId), contains(omniText));
            return builder.or(likeName, equalQuantaa);
        };
    }

    public static Specification<Student> equalInstitution(Long institution) {
        if (institution == null || institution == 0) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(Student_.institutionId), institution);
    }

    public static Specification<Student> hasAccess(StudentSearchCriteria criteria) {
        if (criteria.getOrgan().equals("academic")) {
            return (root, query, builder) -> root.get(Student_.institutionId).in(criteria.getAccessInstitutions());
        }

        if (criteria.getOrgan().equals("residence")) {
            return (root, query, builder) -> root.get(Student_.residenceId).in(criteria.getAccessResidences());
        }
        return null;
    }

    public static Specification<Student> isInClass(Boolean isInClass) {
        if (isInClass == null) {
            return null;
        }
        return (root, query, builder) ->
                isInClass ? builder.isNotNull(root.get(Student_.currentAcademicClassId))
                        : builder.isNull(root.get(Student_.currentAcademicClassId));
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
