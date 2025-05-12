package bd.org.quantum.education.admission.examcenter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamCenterRepository extends JpaRepository<ExamCenter, Long>, JpaSpecificationExecutor<ExamCenter> {
    ExamCenter getById(Long id);
    boolean existsByCode(String examCenterCode);
    boolean existsByCodeAndIdNot(String examCenterCode, Long id);
}
