package bd.org.quantum.education.residence.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidenceAttendanceRepository extends JpaRepository<ResidenceAttendance, Long>, JpaSpecificationExecutor<ResidenceAttendance> {
    boolean existsByAcademicClassIdAndTypeAndDate(Long clsId, String type, String date);
}
