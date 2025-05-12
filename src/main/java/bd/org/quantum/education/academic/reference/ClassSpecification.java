package bd.org.quantum.education.academic.reference;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class ClassSpecification {
    public static Specification<Clazz> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeName =
                    builder.like(
                            builder.lower(root.get(Clazz_.name)),
                            contains(omniText.toLowerCase()));

            Predicate likeLevel =
                    builder.like(
                            builder.lower(root.get(Clazz_.level)),
                            contains(omniText.toLowerCase()));

            return builder.or(likeName, likeLevel);
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
