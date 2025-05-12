package bd.org.quantum.education.academic.examination;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamMarksRepository extends JpaRepository<ExamMarks, Long>, JpaSpecificationExecutor<ExamMarks> {
    List<ExamMarks> findByAcademicClassIdAndSubjectIdAndExamId(Long classId, Long subjectId, Long examId);
}
