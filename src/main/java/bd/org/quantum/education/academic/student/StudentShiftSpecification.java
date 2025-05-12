package bd.org.quantum.education.academic.student;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class StudentShiftSpecification {
    public static Specification<StudentShift> omniText(String omniText) {
        if (omniText == null || omniText.isEmpty()) {
            return null;
        }

        return (root, query, builder) -> {
            Predicate likeStudentName =
                    builder.like(
                            builder.lower(root.get(StudentShift_.studentName)),
                            contains(omniText.toLowerCase()));

            Predicate likeStudentId =
                    builder.like(
                            builder.lower(root.get(StudentShift_.studentId)),
                            contains(omniText.toLowerCase()));

            return builder.or(likeStudentName, likeStudentId);
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
