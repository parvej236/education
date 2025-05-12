package bd.org.quantum.education.academic.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentShiftRepository extends JpaRepository<StudentShift, Long>, JpaSpecificationExecutor<StudentShift> {
}
