package bd.org.quantum.education.admission.appointment;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class GuardianAppointmentSpecification {

    public static Specification<GuardianAppointment> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeStudentName = builder.like(
                    builder.lower(root.get(GuardianAppointment_.student).get("applicant").get("nameEn")),
                    contains(omniText.toLowerCase()));

            Predicate likeStudentId = builder.like(
                    builder.lower(root.get(GuardianAppointment_.student).get("studentId")),
                    contains(omniText.toLowerCase()));

            return builder.or(likeStudentName, likeStudentId);
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
