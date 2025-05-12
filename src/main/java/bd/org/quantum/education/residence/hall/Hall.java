package bd.org.quantum.education.residence.hall;

import bd.org.quantum.education.common.AuditData;
import bd.org.quantum.education.residence.abason.Residence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "res_hall")
public class Hall extends AuditData {
    private String name;
    private String code;
    private String remarks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "residence")
    private Residence residence;

    @Transient
    private String action;
}
