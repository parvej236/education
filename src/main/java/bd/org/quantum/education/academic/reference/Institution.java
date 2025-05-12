package bd.org.quantum.education.academic.reference;

import bd.org.quantum.education.common.AuditData;
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
@Table(name = "aca_institution")
public class Institution extends AuditData {
    private String name;
    private String code;
    private String phone;
    private String email;
    private String responsiblePerson;
    private String address;
    private String remarks;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "institution_id", referencedColumnName = "id")
    private List<InstitutionClass> classList;

    @Transient
    private String action;
}
