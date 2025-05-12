package bd.org.quantum.education.admission.guardian;

import bd.org.quantum.education.admission.student.Student;
import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "adm_guardian")
public class Guardian extends AuditData {
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student")
    private Student student;

    private String nameEn;
    private String nameBn;
    private String indentityType;
    private String indentityNumber;
    private String primaryPhone;
    private String secondaryPhone;
    private String email;
    private String relation;
    private Integer nationality;
    private String imagePath;

    @Transient
    private String action;
}
