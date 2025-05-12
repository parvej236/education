package bd.org.quantum.education.academic.student;

import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aca_student_shift")
public class StudentShift extends AuditData {
    private String studentId;
    private Long studentRowId;
    private String studentName;
    private String shiftFromInstitution;
    private String shiftToInstitution;
    private String shiftFromClassInfo;
    private String shiftToClassInfo;
    private String shiftBy;
    private String cause;

    @Transient
    private Long preAcademicClass;

    @Transient
    private Long newAcademicClass;
}
