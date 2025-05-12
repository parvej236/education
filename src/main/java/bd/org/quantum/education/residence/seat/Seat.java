package bd.org.quantum.education.residence.seat;

import bd.org.quantum.education.common.AuditData;
import bd.org.quantum.education.residence.room.Room;
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
@Table(name = "res_seat")
public class Seat extends AuditData {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    private String seatCode;
    private String status;
    private String seatDescription;
}
