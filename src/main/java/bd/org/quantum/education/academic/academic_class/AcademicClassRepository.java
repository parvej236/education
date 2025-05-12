package bd.org.quantum.education.academic.academic_class;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicClassRepository extends JpaRepository<AcademicClass, Long>, JpaSpecificationExecutor<AcademicClass> {
    AcademicClass getById(Long id);
    List<AcademicClass> findAllByInstitutionIdAndStatusOrderBySessionDesc(Long id, Integer status);
}
