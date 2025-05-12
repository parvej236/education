package bd.org.quantum.education.admission.document;

import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor  
@AllArgsConstructor
@Entity
@Table(name = "adm_document")
public class Document extends AuditData {
    private String documentName;
    private String documentCode;
    private String remarks;
}
