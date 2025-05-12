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
@Table(name = "aca_institution_class")
public class InstitutionClass extends AuditData {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private Clazz clazz;
}
