package bd.org.quantum.education.admission.examcenter;

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
@Table(name = "adm_exam_center")
public class ExamCenter extends AuditData {

    private String name;
    private String code;
    private String district;
    private Boolean active;

    private String responsible;
    private String phone;
    private String email;
    private String address;
    private String remarks;
}
