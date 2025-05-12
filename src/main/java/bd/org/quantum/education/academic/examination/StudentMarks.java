package bd.org.quantum.education.academic.examination;

import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "aca_student_marks")
public class StudentMarks extends AuditData {
    private Long studentId;
    private String studentName;
    private String quantaaId;
    private Double marks;
}
