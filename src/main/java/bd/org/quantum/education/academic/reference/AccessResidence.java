package bd.org.quantum.education.academic.reference;

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
@Table(name = "aca_access_residence")
public class AccessResidence extends AuditData {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "residence")
    private Residence residence;
    private Boolean isAccessed;
}
