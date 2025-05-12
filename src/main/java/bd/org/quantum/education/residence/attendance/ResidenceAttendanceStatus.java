package bd.org.quantum.education.residence.attendance;

import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "res_attendance_status")
public class ResidenceAttendanceStatus extends AuditData {
    private Long studentId;
    private String studentName;
    private String quantaaId;
    private String attendanceStatus;
    @Size(max = 500)
    private String absentCause;
}
