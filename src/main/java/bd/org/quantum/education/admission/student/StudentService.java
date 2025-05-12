package bd.org.quantum.education.admission.student;

import bd.org.quantum.authorizer.helper.UserContext;
import bd.org.quantum.authorizer.model.UserDetails;
import bd.org.quantum.common.resttemplate.RestTemplateService;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.UrlUtils;
import bd.org.quantum.education.academic.reference.*;
import bd.org.quantum.education.academic.student.AcademicStudentSpecification;
import bd.org.quantum.education.academic.student.StudentSearchCriteria;
import bd.org.quantum.education.admission.document.Document;
import bd.org.quantum.education.admission.document.DocumentService;
import bd.org.quantum.education.common.Branch;
import bd.org.quantum.education.common.CommonDao;
import bd.org.quantum.education.common.Country;
import bd.org.quantum.education.common.District;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StudentService {
    private static final String countries = "/api/country-list";
    private static final String districts = "/api/district-list";
    private static final String branches = "/api/branches";

    @Value("${resource.api.url}")
    private String resourceApiUrl;

    private final StudentRepository repository;
    private final CommonDao commonDao;
    private final ClassRepository classRepository;
    private final RestTemplateService restService;
    private final DocumentService documentService;
    private final AcademicReferenceService academicReferenceService;

    public StudentService(StudentRepository repository,
                          CommonDao commonDao,
                          ClassRepository classRepository,
                          RestTemplateService restService,
                          DocumentService documentService,
                          AcademicReferenceService academicReferenceService) {
        this.repository = repository;
        this.commonDao = commonDao;
        this.classRepository = classRepository;
        this.restService = restService;
        this.documentService = documentService;
        this.academicReferenceService = academicReferenceService;
    }

    public Student create(Student student) {
        return repository.save(student);
    }

    public Student getStudentById(Long id) {
        return repository.getById(id);
    }

    public boolean existsStudentByStudentId(String studentId) {
        return repository.existsByStudentId(studentId);
    }

    public boolean existsStudentByStudentIdForAnother(String studentId, Long studentIdToExclude) {
        return repository.existsByStudentIdAndIdNot(studentId, studentIdToExclude);
    }

    public Student update(Student studentForm) {
        Student student = repository.getById(studentForm.getId());
        BeanUtils.copyProperties(studentForm, student);
        return repository.save(student);
    }

    public Page<Student> search(StudentSearchCriteria searchCriteria) {
        Specification<Student> specification = Specification
                .where(StudentSpecification.omniText(searchCriteria.getOmniText()))
                .and(StudentSpecification.institutionEquals(searchCriteria.getInstitution()))
                .and(StudentSpecification.sessionEquals(searchCriteria.getSession()))
                .and(StudentSpecification.genderEquals(searchCriteria.getGender()))
                .and(StudentSpecification.religionEquals(searchCriteria.getReligion()))
                .and(StudentSpecification.communityEquals(searchCriteria.getCommunity()))
                .and(StudentSpecification.examCenterEquals(searchCriteria.getExamCenter()))
                .and(StudentSpecification.districtEquals(searchCriteria.getDistrict()))
                .and(StudentSpecification.upazilaEquals(searchCriteria.getUpazila()));

        Page<Student> students;
        Pageable pageable = PageRequest.of(
                searchCriteria.getPage(),
                searchCriteria.getPageSize(),
                Sort.by(Sort.Direction.fromString(searchCriteria.getSortDirection()), searchCriteria.getSortBy())
        );

        if (searchCriteria.isUnpaged()) {
            List<Student> list = repository.findAll(specification, pageable.getSort());
            students = new PageImpl<>(list);
        } else {
            students = repository.findAll(specification, pageable);
        }
        return students;

    }

    public Page<Student> studentList(StudentSearchCriteria searchCriteria) {
        Specification<Student> specification = Specification
                .where(AcademicStudentSpecification.omniText(searchCriteria.getOmniText()))
                .and(AcademicStudentSpecification.equalInstitution(searchCriteria.getInstitution()))
                .and(AcademicStudentSpecification.isInClass(searchCriteria.getIsInClass()))
                .and(AcademicStudentSpecification.hasAccess(searchCriteria));

        Page<Student> academicStudentPage;

        Pageable pageable = PageRequest.of(
                searchCriteria.getPage(),
                searchCriteria.getPageSize(),
                Sort.by(Sort.Direction.fromString(searchCriteria.getSortDirection()), searchCriteria.getSortBy())
        );

        if (searchCriteria.isUnpaged()) {
            List<Student> list = repository.findAll(specification, pageable.getSort());
            academicStudentPage = new PageImpl<>(list);
        } else {
            academicStudentPage = repository.findAll(specification, pageable);
        }
        return academicStudentPage;
    }

    public List<Student> getStudentInfoByNameOrQuantaaId(String nameOrQuantaaId) {
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

        return repository.getStudentInfoByNameOrQuantaaId(nameOrQuantaaId.toLowerCase(), accessInstitutions);
    }

    public List<Clazz> getAcademicClasses() {
        return classRepository.findAll();
    }

    public List<Branch> branches() {
        final String uri = UrlUtils.getUrl(resourceApiUrl, branches);
        return restService.getForList(uri, new ParameterizedTypeReference<>() {
        });
    }

    public List<Country> countries() {
        final String uri = UrlUtils.getUrl(resourceApiUrl, countries);
        return restService.getForList(uri, new ParameterizedTypeReference<>() {
        });
    }

    public List<District> districts() {
        final String uri = UrlUtils.getUrl(resourceApiUrl, districts);
        return restService.getForList(uri, new ParameterizedTypeReference<>() {
        });
    }

    public void validateStudent(Student student, BindingResult result, String status) {
        String studentId = student.getStudentId();
        String institutionName = student.getInstitutionName();

        if (studentId == null || studentId.trim().isEmpty()) {
            result.rejectValue("studentId", "error.required");
        }

        if (institutionName == null || institutionName.trim().isEmpty()) {
            result.rejectValue("institutionName", "error.required");
        }

        if (studentId != null && !studentId.isEmpty()) {
            if ("create".equals(status) && existsStudentByStudentId(studentId)) {
                result.rejectValue("studentId", "error.exists");
            }
            if ("update".equals(status) && existsStudentByStudentIdForAnother(studentId, student.getId())) {
                result.rejectValue("studentId", "error.exists");
            }
        }
    }

    public void changeStatus(Long id) {
        commonDao.setAdmittedApplicant(id);
    }

    public void validateStudentDocument(Student student, BindingResult result) {
        if (student.getId() == null) {
            result.rejectValue("id", "error.required");
        }

        ArrayList<Document> list = new ArrayList<>();

        for (int i = 0; i < student.getDocuments().size(); i++) {
            if (student.getDocuments().get(i).getId() == null) {
                continue;
            }
            boolean flag = true;
            for (int j = i + 1; j < student.getDocuments().size(); j++) {
                if (student.getDocuments().get(i).getId().equals(student.getDocuments().get(j).getId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.add(documentService.findById(student.getDocuments().get(i).getId()));
            }
        }
        student.setDocuments(list);
    }

    public Boolean isDocumentAssignedToStudent(Long studentId) {
        Student student = getStudentById(studentId);
        return !student.getDocuments().isEmpty();
    }

    public Page<Student> studentDocumentSearch(SearchForm form) {
        Specification<Student> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null : StudentSpecification.omniText(form.getOmniText()));

        Page<Student> students;
        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<Student> list = repository.findAll(specification, pageable.getSort());
            students = new PageImpl<>(list);
        } else {
            students = repository.findAll(specification, pageable);
        }
        return students;
    }
}
