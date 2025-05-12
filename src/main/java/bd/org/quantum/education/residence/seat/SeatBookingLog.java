package bd.org.quantum.education.residence.seat;

import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "res_seat_booking_log")
public class SeatBookingLog extends AuditData {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    private String studentName;
    private String studentId;
    private String studentClass;
    private String institution;

    private String bookingDate;
    private String releaseDate;
    private String status;

    @Transient
    private Long studentRowId;
}
