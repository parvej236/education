package bd.org.quantum.education.academic.academic_class;

import bd.org.quantum.authorizer.helper.UserContext;
import bd.org.quantum.authorizer.model.UserDetails;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.academic.reference.Institution;
import bd.org.quantum.education.academic.reference.UserAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AcademicClassService {
    private final AcademicClassRepository academicClassRepository;
    private final AcademicReferenceService academicReferenceService;

    public AcademicClassService(AcademicClassRepository academicClassRepository,
                                AcademicReferenceService academicReferenceService) {
        this.academicClassRepository = academicClassRepository;
        this.academicReferenceService = academicReferenceService;
    }

    public AcademicClass createAcademicClass(AcademicClass academicClass) {
        return academicClassRepository.save(academicClass);
    }

    public Page<AcademicClass> academicClassSearch(AcademicClassSearchCriteria search) {
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

        Specification<AcademicClass> specification = Specification
                .where(AcademicClassSpecification.omniText(search.getOmniText()))
                .and(AcademicClassSpecification.institutionEquals(search.getInstitution()))
                .and(AcademicClassSpecification.classEquals(search.getClazz()))
                .and(AcademicClassSpecification.classGroupEquals(search.getClassGroup()))
                .and(AcademicClassSpecification.sectionEquals(search.getSection()))
                .and(AcademicClassSpecification.sessionEquals(search.getSession()))
                .and(AcademicClassSpecification.statusEquals(search.getStatus()))
                .and(AcademicClassSpecification.releaseStatusEquals(search.getReleaseStatus()))
                .and(AcademicClassSpecification.inAccessInstitutions(accessInstitutions));

        Page<AcademicClass> academicClassList;
        Pageable pageable = PageRequest.of(
                search.getPage(),
                search.getPageSize(),
                Sort.by(Sort.Direction.DESC, "session")
        );

        if (search.isUnpaged()) {
            List<AcademicClass> list = academicClassRepository.findAll(specification, pageable.getSort());
            academicClassList = new PageImpl<>(list);
        } else {
            academicClassList = academicClassRepository.findAll(specification, pageable);
        }
        return academicClassList;
    }

    public AcademicClass getAcademicClassById(Long id) {
        return academicClassRepository.getById(id);
    }

    public List<AcademicClass> getAcademicClassList() {
        return academicClassRepository.findAll(Sort.by(Sort.Direction.DESC, "session"));
    }

    public List<AcademicClass> getAcademicClassListByInstitution(Long id, Integer status) {
        return academicClassRepository.findAllByInstitutionIdAndStatusOrderBySessionDesc(id, status);
    }

    public void validateAcademicClass(AcademicClass academicClass, BindingResult result) {
        int flag = 0;

        if (academicClass.getClazz() == null || academicClass.getClazz().getId() == 0) {
            result.rejectValue("clazz", "error.required");
            flag = 1;
        }

        if (academicClass.getInstitution().getId() == null || academicClass.getInstitution().getId() == 0) {
            result.rejectValue("institution", "error.required");
            flag = 1;
        }

        if (academicClass.getSession() == null) {
            result.rejectValue("session", "error.required");
            flag = 1;
        }

        if (flag == 0) {
            if (isExistsAcademicClass(academicClass)) {
                result.rejectValue("institution", "error.exists");
            }
        }
    }

    public boolean isExistsAcademicClass(AcademicClass academicClass) {
        List<AcademicClass> academicClassList = getAcademicClassList();
        for (AcademicClass acaClass : academicClassList) {
            if (academicClass.getInstitution().getId().equals(acaClass.getInstitution().getId())
                    && academicClass.getClazz().getId().equals(acaClass.getClazz().getId())
                    && academicClass.getClassGroup().equals(acaClass.getClassGroup())
                    && academicClass.getSection().equals(acaClass.getSection())
                    && academicClass.getSession().equals(acaClass.getSession())) {
                return true;
            }
        }
        return false;
    }

    public void validateAcaClass(AcademicClass academicClass, BindingResult result) {
        for (SubjectDetails details : academicClass.getSubjectDetailsList()) {
            if (details.getSubject() == null || details.getSubjectType() == null) {
                result.rejectValue("institution", "error.required");
                return;
            }
        }

        for (int i = 0; i < academicClass.getSubjectDetailsList().size(); i++) {
            for (int j = i + 1; j < academicClass.getSubjectDetailsList().size(); j++) {
                if (academicClass.getSubjectDetailsList().get(i).getSubject().getId().equals(academicClass.getSubjectDetailsList().get(j).getSubject().getId())) {
                    result.rejectValue("institution", "error.duplicate");
                    return;
                }
            }
        }
    }

    public AcademicClass setStatus(AcademicClass academicClass, String action) {
        if (action.equals("new") && academicClass.getSubjectDetailsList() != null) {
            academicClass.setStatus(1);
        }

        if (action.equals("update") && academicClass.getSubjectDetailsList() == null) {
            academicClass.setStatus(null);
        }

        if (action.equals("submit") && academicClass.getSubjectDetailsList() != null) {
            academicClass.setStatus(2);
        }

        return academicClass;
    }

    public void validAcademicPromotion(AcademicClassPromotion academicClassPromotion, BindingResult result) {
        boolean flag = true;
        List<PromotionStudent> promotionStudents = academicClassPromotion.getStudentList();

        if (promotionStudents != null) {
            for (PromotionStudent promotion : promotionStudents) {
                if (promotion.getIsPromotion() != null) {
                    flag = false;
                    break;
                }
            }
        }

        if (flag) {
            result.rejectValue("studentList", "class.promotion.student.promotion.required");
        }

        if (academicClassPromotion.getNewAcademicClass() == null) {
            result.rejectValue("newAcademicClass", "error.required");
        }
    }

    public List<AcademicClass> getStudentAcademicClassList(Long id) {
        List<AcademicClass> academicClassList = getAcademicClassList();
        List<AcademicClass> list = new ArrayList<>();
        for (AcademicClass academicClass : academicClassList) {
            for (AcademicClassStudent academicClassStudent : academicClass.getStudentList()) {
                if (academicClassStudent.getStudent().getId().equals(id)) {
                    list.add(academicClass);
                    break;
                }
            }
        }
        Collections.reverse(list);
        return list;
    }
}
