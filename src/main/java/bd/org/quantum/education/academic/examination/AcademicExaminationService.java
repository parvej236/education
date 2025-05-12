package bd.org.quantum.education.academic.examination;

import bd.org.quantum.authorizer.helper.UserContext;
import bd.org.quantum.authorizer.model.UserDetails;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.academic.reference.Institution;
import bd.org.quantum.education.academic.reference.UserAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AcademicExaminationService {

    private final ExamMarksRepository repository;
    private final AcademicReferenceService academicReferenceService;

    public AcademicExaminationService(ExamMarksRepository repository,
                                      AcademicReferenceService academicReferenceService) {
        this.repository = repository;
        this.academicReferenceService = academicReferenceService;
    }

    public ExamMarks createExamMarks(ExamMarks examMarks) {
        return repository.save(examMarks);
    }

    public ExamMarks getExamMarksById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<ExamMarks> examMarksSearch(ExamMarksSearch search) {
        UserDetails user = UserContext.getPrincipal().getUserDetails();
        List<Long> accessInstitutions = new ArrayList<>();

        if (user.getSuperUser()) {
            for(Institution institution : academicReferenceService.getAllInstitution()) {
                accessInstitutions.add(institution.getId());
            }
        } else {
            UserAccess userAccess = academicReferenceService.getUserAccessByUserId(user.getId());
            accessInstitutions = userAccess.getAllAccessInstitutionId();
        }

        Specification<ExamMarks> specification = Specification
                .where(StringUtils.isEmpty(search.getOmniText()) ? null : ExamMarksSpecification.omniText(search.getOmniText()))
                .and((search.getInstitution() == null || search.getInstitution() == 0) ? null : ExamMarksSpecification.institutionEquals(search.getInstitution()))
                .and((search.getAcademicClass() == null ||search.getAcademicClass() == 0) ? null : ExamMarksSpecification.academicClassEquals(search.getAcademicClass()))
                .and((search.getSubject() == null || search.getSubject() == 0) ? null : ExamMarksSpecification.subjectEquals(search.getSubject()))
                .and((search.getExam() == null) ? null : ExamMarksSpecification.examEquals(search.getExam()))
                .and(ExamMarksSpecification.inAccessInstitutions(accessInstitutions));

        Page<ExamMarks> examMarksPage;

        Pageable pageable = PageRequest.of(
                search.getPage(),
                search.getPageSize(),
                Sort.by(Sort.Direction.fromString(search.getSortDirection()), search.getSortBy())
        );

        if (search.isUnpaged()) {
            List<ExamMarks> list = repository.findAll(specification, pageable.getSort());
            examMarksPage = new PageImpl<>(list);
        } else {
            examMarksPage = repository.findAll(specification, pageable);
        }
        return examMarksPage;
    }

    public List<ExamMarks> isExistsExamMarks(Long classId, Long subjectId, Long examId) {
        return repository.findByAcademicClassIdAndSubjectIdAndExamId(classId, subjectId, examId);
    }

    public void validation(ExamMarks examMarks, BindingResult result) {
        int flag = 0;
        if (examMarks.getAcademicClass() == null || examMarks.getAcademicClass().getId() == 0) {
            result.rejectValue("academicClass.id", "error.required");
            flag = 1;
        }
        if (examMarks.getSubject() == null || examMarks.getSubject().getId() == 0) {
            result.rejectValue("subject.id", "error.required");
            flag = 1;
        }
        if (examMarks.getExam().getId() == 0) {
            result.rejectValue("exam.id", "error.required");
            flag = 1;
        }
        if (flag == 0) {
            if (!isExistsExamMarks(examMarks.getAcademicClass().getId(), examMarks.getSubject().getId(), examMarks.getExam().getId()).isEmpty()) {
                result.rejectValue("academicClass", "error.exists");
            }
        }
    }

    public void studentMarksValidation(ExamMarks examMarks, BindingResult result) {
        for (StudentMarks studentMarks : examMarks.getStudentMarks()){
            if (studentMarks.getMarks() == null) {
                result.rejectValue("studentMarks", "error.student.mark.required");
                break;
            }
        }
    }
}