package bd.org.quantum.education.academic.academic_class;

import bd.org.quantum.education.academic.reference.Clazz;
import bd.org.quantum.education.academic.reference.Institution;
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
@Table(name = "aca_academic_class")
public class AcademicClass extends AuditData {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institution_id")
    private Institution institution;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private Clazz clazz;

    private String classGroup;
    private String section;
    private Integer session;
    private Integer status = 0;
    private Integer releaseStatus;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "academic_class", referencedColumnName = "id")
    private List<SubjectDetails> subjectDetailsList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "academic_class", referencedColumnName = "id")
    private List<AcademicClassStudent> studentList;

    @Transient
    private String action;

    public String getAcademicClassInfo() {
        if (classGroup.isEmpty() && section == null) {
            return clazz.getName() + " (" + session + ")";
        }
        if (classGroup.isEmpty()) {
            return clazz.getName() + ", " + section + " (" + session + ")";
        }
        if (section == null) {
            return clazz.getName() + ", " + classGroup + " (" + session + ")";
        }
        return clazz.getName() + ", " + classGroup + ", " + section + " (" + session + ")";
    }
}
