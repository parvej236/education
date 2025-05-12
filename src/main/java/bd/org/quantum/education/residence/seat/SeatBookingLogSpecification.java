package bd.org.quantum.education.residence.seat;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class SeatBookingLogSpecification {
    public static Specification<SeatBookingLog> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate studentId = builder.like(
                    builder.lower(root.get(SeatBookingLog_.studentId)),
                    contains(omniText.toLowerCase()));

            Predicate studentName = builder.like(
                    builder.lower(root.get(SeatBookingLog_.studentName)),
                    contains(omniText.toLowerCase()));

            return builder.or(studentId, studentName);
        };
    }

    public static Specification<SeatBookingLog> statusIsBook() {
        return (root, query, builder) -> builder.equal(root.get(SeatBookingLog_.status), "booked");
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
