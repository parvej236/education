package bd.org.quantum.education.academic.academic_class;

import bd.org.quantum.education.academic.reference.Subject;
import bd.org.quantum.education.academic.reference.SubjectType;
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
@Table(name = "aca_subject_details")
public class SubjectDetails extends AuditData {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject")
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_type")
    private SubjectType subjectType;

    private String remarks;
}
