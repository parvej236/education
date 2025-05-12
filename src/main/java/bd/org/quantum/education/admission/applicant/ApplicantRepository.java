package bd.org.quantum.education.admission.applicant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long>, JpaSpecificationExecutor<Applicant> {
    Applicant getById(Long id);

    Boolean existsByFormNo(String formNo);

    @Transactional
    @Modifying
    @Query("UPDATE Applicant SET imagePath = :path WHERE id = :id")
    void updateImagePath(@Param(value = "path") String path, @Param(value = "id") long id);

    boolean existsByFormNoAndIdNot(String formNo, Long id);
}