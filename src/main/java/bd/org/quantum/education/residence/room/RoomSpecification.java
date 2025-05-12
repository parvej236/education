package bd.org.quantum.education.residence.room;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class RoomSpecification {
    public static Specification<Room> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeCode =
                    builder.like(
                            builder.lower(root.get(Room_.code)),
                            contains(omniText.toLowerCase()));

            return builder.or(likeCode);
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
