package bd.org.quantum.education.residence.room;

import bd.org.quantum.education.common.AuditData;
import bd.org.quantum.education.residence.hall.Hall;
import bd.org.quantum.education.residence.seat.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "res_room")
public class Room extends AuditData {
    private String code;
    private String capacity;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hall")
    private Hall hall;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    @Transient
    private String action;
}
