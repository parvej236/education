package bd.org.quantum.education.academic.reference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectTypeRepository extends JpaRepository<SubjectType, Long>, JpaSpecificationExecutor<SubjectType> {
    SubjectType getById(Long id);

    @Query(value = "SELECT * FROM education.aca_subject_type WHERE status = 1", nativeQuery = true)
    List<SubjectType> getAllSubjectType();
}
