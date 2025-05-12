package bd.org.quantum.education.admission.guardian;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class GuardianSpecification {
    public static Specification<Guardian> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeName =
                    builder.like(
                            builder.lower(root.get(Guardian_.student).get("applicant").get("nameEn")),
                            contains(omniText.toLowerCase()));

            Predicate likeStudentID =
                    builder.like(
                            builder.lower(root.get(Guardian_.student).get("studentId")),
                            contains(omniText.toLowerCase()));

            Predicate likeGuardianName =
                    builder.like(
                            builder.lower(root.get(Guardian_.nameEn)),
                            contains(omniText.toLowerCase()));

            Predicate likeGueardianPhone =
                    builder.like(
                            builder.lower(root.get(Guardian_.primaryPhone)),
                            contains(omniText.toLowerCase()));

            return builder.or(likeName, likeStudentID, likeGuardianName, likeGueardianPhone);
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
