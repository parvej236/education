package bd.org.quantum.education.residence.attendance;

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
public class ResidenceAttendanceService {
    private final ResidenceAttendanceRepository repository;
    private final AcademicReferenceService academicReferenceService;

    public ResidenceAttendanceService(ResidenceAttendanceRepository repository,
                                      AcademicReferenceService academicReferenceService) {
        this.repository = repository;
        this.academicReferenceService = academicReferenceService;
    }

    public ResidenceAttendance save(ResidenceAttendance residenceAttendance) {
        return repository.save(residenceAttendance);
    }

    public Page<ResidenceAttendance> attendanceSearch(ResidenceAttendanceSearchCriteria searchForm) {
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

        Specification<ResidenceAttendance> specification = Specification
                .where(ResidenceAttendanceSpecification.omniText(searchForm.getOmniText()))
                .and(ResidenceAttendanceSpecification.equalDate(searchForm.getDate()))
                .and(ResidenceAttendanceSpecification.equalType(searchForm.getAttendanceType()))
                .and(ResidenceAttendanceSpecification.equalInstitution(searchForm.getInstitution()))
                .and(ResidenceAttendanceSpecification.equalCls(searchForm.getCls()))
                .and(ResidenceAttendanceSpecification.equalClasGroup(searchForm.getClsGroup()))
                .and(ResidenceAttendanceSpecification.equalSection(searchForm.getSection()))
                .and(ResidenceAttendanceSpecification.equalSession(searchForm.getSession()))
                .and(ResidenceAttendanceSpecification.inAccessInstitutions(accessInstitutions));

        Page<ResidenceAttendance> residanceAttendancePage;

        Pageable pageable = PageRequest.of(
                searchForm.getPage(),
                searchForm.getPageSize(),
                Sort.by(Sort.Direction.DESC, searchForm.getSortBy())
        );

        if (searchForm.isUnpaged()) {
            List<ResidenceAttendance> list = repository.findAll(specification, pageable.getSort());
            residanceAttendancePage = new PageImpl<>(list);
        } else {
            residanceAttendancePage = repository.findAll(specification, pageable);
        }
        return residanceAttendancePage;
    }

    public ResidenceAttendance getAttendanceSheetById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void validateAttendance(ResidenceAttendance residenceAttendance, BindingResult result) {
        boolean isExistsError = false;

        if (StringUtils.isEmpty(residenceAttendance.getType())){
            result.rejectValue("type", "error.required");
            isExistsError = true;
        }

        if (StringUtils.isEmpty(residenceAttendance.getDate())){
            result.rejectValue("date", "error.required");
            isExistsError = true;
        }

        if (StringUtils.isEmpty(residenceAttendance.getInstitution())){
            result.rejectValue("institution", "error.required");
        }

        if (StringUtils.isEmpty(residenceAttendance.getAcademicClass())){
            result.rejectValue("academicClass.id", "error.required");
            isExistsError = true;
        }

        if (isExistsError){
            return;
        }

        Long academicClass = residenceAttendance.getAcademicClass().getId();
        String type = residenceAttendance.getType();
        String date = residenceAttendance.getDate();
        boolean isExist = repository.existsByAcademicClassIdAndTypeAndDate(academicClass, type, date);
        if (isExist) {
            result.rejectValue("academicClass.id", "error.exists");
        }
    }
}
