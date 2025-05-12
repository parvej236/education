package bd.org.quantum.education.academic.reference;

import bd.org.quantum.education.common.AuditData;
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
@Table(name = "aca_access_institution")
public class AccessInstitution extends AuditData {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution")
    private Institution institution;
    private Boolean isAccessed;
}
