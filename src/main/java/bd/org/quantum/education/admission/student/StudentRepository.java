package bd.org.quantum.education.admission.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Student getById(Long id);

    boolean existsByStudentId(String studentId);

    @Query(value = "SELECT s.* " +
            "FROM education.adm_student s " +
            "JOIN education.adm_applicant a " +
            "ON s.applicant = a.id " +
            "WHERE (s.student_id LIKE CONCAT('%', :nameOrQuantaaId) " +
            "OR LOWER(a.name_en) LIKE CONCAT('%', :nameOrQuantaaId, '%'))" +
            "AND s.institution_id IN :accessInstitutions", nativeQuery = true)
    List<Student> getStudentInfoByNameOrQuantaaId(@Param(value = "nameOrQuantaaId") String nameOrQuantaaId,
                                                  @Param(value = "accessInstitutions") List<Long> accessInstitutions);

    boolean existsByStudentIdAndIdNot(String studentId, Long id);
}
