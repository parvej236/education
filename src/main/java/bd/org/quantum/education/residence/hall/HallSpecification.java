package bd.org.quantum.education.residence.hall;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class HallSpecification {
    public static Specification<Hall> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeName =
                    builder.like(
                            builder.lower(root.get(Hall_.name)),
                            contains(omniText.toLowerCase()));

            Predicate likeCode =
                    builder.like(
                            builder.lower(root.get(Hall_.code)),
                            contains(omniText.toLowerCase()));

            return builder.or(likeName, likeCode);
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
