package bd.org.quantum.education.admission.guardian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long>, JpaSpecificationExecutor<Guardian> {
    @Transactional
    @Modifying
    @Query("UPDATE Guardian SET imagePath = :path WHERE id = :id")
    void updateImagePath(@Param(value = "path") String path, @Param(value = "id") long id);

    List<Guardian> findAllByStudentId(Long id);
}