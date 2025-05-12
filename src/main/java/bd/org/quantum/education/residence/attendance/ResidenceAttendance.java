package bd.org.quantum.education.residence.attendance;

import bd.org.quantum.education.academic.academic_class.AcademicClass;
import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "res_attendance")
public class ResidenceAttendance extends AuditData {
    private String type;
    private String date;
    private String institution;
    private String classInfo;
    private Integer status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academic_class")
    private AcademicClass academicClass;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "attendance_sheet", referencedColumnName = "id")
    private List<ResidenceAttendanceStatus> statusList;

    @Transient
    private Integer action;
}
