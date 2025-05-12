package bd.org.quantum.education.residence.seat;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class SeatSpecification {
    public static Specification<Seat> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate seat = builder.like(
                    builder.lower(root.get(Seat_.seatCode)),
                    contains(omniText.toLowerCase()));

            return builder.or(seat);
        };
    }

    public static Specification<Seat> statusIsFree() {
        return (root, query, builder) -> builder.equal(root.get(Seat_.status), "free");
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
