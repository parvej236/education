package bd.org.quantum.education.academic.reference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {
    Subject getById(Long id);

    @Query(value = "SELECT * FROM education.aca_subject WHERE status = 1", nativeQuery = true)
    List<Subject> getAllSubject();
}
