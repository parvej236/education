package bd.org.quantum.education.academic.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicAttendanceRepository extends JpaRepository<AcademicAttendance, Long>, JpaSpecificationExecutor<AcademicAttendance> {
    boolean existsByAcademicClassIdAndTypeAndDate(Long clsId, String type, String date);
}
