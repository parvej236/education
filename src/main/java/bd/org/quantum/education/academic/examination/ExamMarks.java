package bd.org.quantum.education.academic.examination;

import bd.org.quantum.education.academic.academic_class.AcademicClass;
import bd.org.quantum.education.academic.reference.Exam;
import bd.org.quantum.education.academic.reference.Subject;
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
@Table(name = "aca_exam_marks")
public class ExamMarks extends AuditData {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academic_class")
    private AcademicClass academicClass;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject")
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam")
    private Exam exam;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "mark_sheet", referencedColumnName = "id")
    private List<StudentMarks> studentMarks;

    private Integer status;

    @Transient
    private Integer action;
}
