package bd.org.quantum.education.academic.reference;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class ExamSpecification {
    public static Specification<Exam> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeName =
                    builder.like(
                            builder.lower(root.get(Exam_.name)),
                            contains(omniText.toLowerCase()));

            return builder.or(likeName);
        };
    }
    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
