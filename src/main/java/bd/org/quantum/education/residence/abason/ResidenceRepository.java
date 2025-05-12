package bd.org.quantum.education.residence.abason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Long>, JpaSpecificationExecutor<Residence> {
    Residence getById(Long id);
}
