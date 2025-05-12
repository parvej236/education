package bd.org.quantum.education.academic.reference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends JpaRepository<Clazz, Long>, JpaSpecificationExecutor<Clazz> {
    Clazz getById(Long id);
}
