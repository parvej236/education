package bd.org.quantum.education.academic.reference;

import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aca_subject")
public class Subject extends AuditData {
    private String name;
    private String code;
    private int status;
    private String remarks;

    @Transient
    private String action;
}