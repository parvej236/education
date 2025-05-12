package bd.org.quantum.education.admission.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardianAppointmentRepository extends JpaRepository<GuardianAppointment, Long>, JpaSpecificationExecutor<GuardianAppointment> {
}
