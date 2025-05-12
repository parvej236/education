package bd.org.quantum.education.academic.academic_class;

import bd.org.quantum.education.admission.student.Student;
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
@Table(name = "aca_class_students")
public class AcademicClassStudent extends AuditData {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student")
    private Student student;

    private String status;
    private String remarks;
}
