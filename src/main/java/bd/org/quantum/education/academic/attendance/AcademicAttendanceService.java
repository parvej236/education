package bd.org.quantum.education.academic.attendance;

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
public class AcademicAttendanceService {
    private final AcademicAttendanceRepository repository;
    private final AcademicReferenceService academicReferenceService;

    public AcademicAttendanceService(AcademicAttendanceRepository repository,
                                     AcademicReferenceService academicReferenceService) {
        this.repository = repository;
        this.academicReferenceService = academicReferenceService;
    }

    public AcademicAttendance save(AcademicAttendance academicAttendance) {
        return repository.save(academicAttendance);
    }

    public Page<AcademicAttendance> getAcademicAttendanceList(AcademicAttendanceSearchCriteria searchForm) {
        UserDetails user = UserContext.getPrincipal().getUserDetails();
        List<Long> accessInstitutions = new ArrayList<>();

        if (user.getSuperUser()) {
            for (Institution institution : academicReferenceService.getAllInstitution()) {
                accessInstitutions.add(institution.getId());
            }
        } else {
            UserAccess userAccess = academicReferenceService.getUserAccessByUserId(user.getId());
            accessInstitutions = userAccess.getAllAccessInstitutionId();
        }

        Specification<AcademicAttendance> specification = Specification
                .where(AcademicAttendanceSpecification.omniText(searchForm.getOmniText()))
                .and(AcademicAttendanceSpecification.equalDate(searchForm.getDate()))
                .and(AcademicAttendanceSpecification.equalType(searchForm.getAttendanceType()))
                .and(AcademicAttendanceSpecification.equalInstitution(searchForm.getInstitution()))
                .and(AcademicAttendanceSpecification.equalCls(searchForm.getCls()))
                .and(AcademicAttendanceSpecification.equalClsGroup(searchForm.getClsGroup()))
                .and(AcademicAttendanceSpecification.equalSection(searchForm.getSection()))
                .and(AcademicAttendanceSpecification.equalSession(searchForm.getSession()))
                .and(AcademicAttendanceSpecification.inAccessInstitutions(accessInstitutions));

        Page<AcademicAttendance> academicAttendancePage;

        Pageable pageable = PageRequest.of(
                searchForm.getPage(),
                searchForm.getPageSize(),
                Sort.by(Sort.Direction.DESC, searchForm.getSortBy())
        );

        if (searchForm.isUnpaged()) {
            List<AcademicAttendance> list = repository.findAll(specification, pageable.getSort());
            academicAttendancePage = new PageImpl<>(list);
        } else {
            academicAttendancePage = repository.findAll(specification, pageable);
        }
        return academicAttendancePage;
    }

    public AcademicAttendance getAttendanceSheetById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void validationAttendanceSheet(AcademicAttendance academicAttendance, BindingResult result) {
        boolean isExistsError = false;

        if (academicAttendance.getType().trim().isEmpty()) {
            result.rejectValue("type", "error.required");
            isExistsError = true;
        }

        if (academicAttendance.getDate().isEmpty()) {
            result.rejectValue("date", "error.required");
            isExistsError = true;
        }

        if (StringUtils.isEmpty(academicAttendance.getInstitution())) {
            result.rejectValue("institution", "error.required");
        }

        if (StringUtils.isEmpty(academicAttendance.getAcademicClass())) {
            result.rejectValue("academicClass.id", "error.required");
            isExistsError = true;
        }

        if (isExistsError) {
            return;
        }

        Long academicClass = academicAttendance.getAcademicClass().getId();
        String type = academicAttendance.getType();
        String date = academicAttendance.getDate();
        boolean isExist = repository.existsByAcademicClassIdAndTypeAndDate(academicClass, type, date);
        if (isExist) {
            result.rejectValue("academicClass.id", "error.exists");
        }
    }
}
