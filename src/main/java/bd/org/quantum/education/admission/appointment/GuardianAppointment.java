package bd.org.quantum.education.admission.appointment;

import bd.org.quantum.common.utils.DateUtils;
import bd.org.quantum.education.admission.guardian.Guardian;
import bd.org.quantum.education.admission.student.Student;
import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "adm_guardian_appointment")
public class GuardianAppointment extends AuditData {
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student")
    private Student student;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "adm_appointment_guardian",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "guardian_id"))
    private List<Guardian> guardians;

    @DateTimeFormat(pattern = DateUtils.DD_MM_YYYY)
    private LocalDate appointmentOn;
    private String responsiblePerson;
    private String reason;
    private Integer numberOfGuaridian;
    private String remarks;

    @Transient
    private String action;
}
