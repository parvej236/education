package bd.org.quantum.education.residence.hall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long>, JpaSpecificationExecutor<Hall> {
    Hall getById(Long id);
}
