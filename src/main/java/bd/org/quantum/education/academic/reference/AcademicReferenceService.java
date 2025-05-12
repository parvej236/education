package bd.org.quantum.education.academic.reference;

import bd.org.quantum.common.resttemplate.RestTemplateService;
import bd.org.quantum.common.utils.SearchForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AcademicReferenceService {

    private final String PERMITTED_USERS_URL = "/api/users-by-service-and-name-reg-or-phone/";

    @Value("${spring.service.name}")
    private String serviceName;

    @Value("${user.api.url}")
    private String userApiUrl;

    private final InstitutionRepository institutionRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectTypeRepository subjectTypeRepository;
    private final ExamRepository examRepository;
    private final UserAccessRepository userAccessRepository;
    private final RestTemplateService restService;

    public AcademicReferenceService(InstitutionRepository institutionRepository,
                                    ClassRepository classRepository,
                                    SubjectRepository subjectRepository,
                                    SubjectTypeRepository subjectTypeRepository,
                                    ExamRepository examRepository, UserAccessRepository userAccessRepository, RestTemplateService restService) {

        this.institutionRepository = institutionRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.subjectTypeRepository = subjectTypeRepository;
        this.examRepository = examRepository;
        this.userAccessRepository = userAccessRepository;
        this.restService = restService;
    }

    public Institution createInstitution(Institution institution) {
        return institutionRepository.save(institution);
    }

    public Institution getInstitutionById(Long id) {
        return institutionRepository.getById(id);
    }

    public Page<Institution> institutionSearch(SearchForm form) {
        Specification<Institution> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null
                        : InstitutionSpecification.omniText(form.getOmniText()));

        Page<Institution> institutionList;

        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<Institution> list = institutionRepository.findAll(specification, pageable.getSort());
            institutionList = new PageImpl<>(list);
        } else {
            institutionList = institutionRepository.findAll(specification, pageable);
        }

        return institutionList;
    }

    public Clazz createClass(Clazz clazz) {
        return classRepository.save(clazz);
    }

    public Clazz getClassById(Long id) {
        return classRepository.getById(id);
    }

    public Page<Clazz> classSearch(SearchForm form) {
        Specification<Clazz> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null
                        : ClassSpecification.omniText(form.getOmniText()));

        Page<Clazz> classList;
        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<Clazz> list = classRepository.findAll(specification, pageable.getSort());
            classList = new PageImpl<>(list);
        } else {
            classList = classRepository.findAll(specification, pageable);
        }

        return classList;
    }

    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.getById(id);
    }

    public Page<Subject> subjectSearch(SearchForm form) {
        Specification<Subject> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null
                        : SubjectSpecification.omniText(form.getOmniText()));

        Page<Subject> subjectList;
        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<Subject> list = subjectRepository.findAll(specification, pageable.getSort());
            subjectList = new PageImpl<>(list);
        } else {
            subjectList = subjectRepository.findAll(specification, pageable);
        }
        return subjectList;
    }

    public SubjectType createSubjectType(SubjectType subjectType) {
        return subjectTypeRepository.save(subjectType);
    }

    public SubjectType getSubjectTypeById(Long id) {
        return subjectTypeRepository.getById(id);
    }

    public Page<SubjectType> subjectTypeSearch(SearchForm form) {

        Specification<SubjectType> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null
                        : SubjectTypeSpecification.omniText(form.getOmniText()));

        Page<SubjectType> subjectTypeList;
        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<SubjectType> list = subjectTypeRepository.findAll(specification, pageable.getSort());
            subjectTypeList = new PageImpl<>(list);
        } else {
            subjectTypeList = subjectTypeRepository.findAll(specification, pageable);
        }
        return subjectTypeList;
    }

    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    public Exam getExamById(Long id) {
        return examRepository.getById(id);
    }

    public Page<Exam> examSearch(SearchForm form) {
        Specification<Exam> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null
                        : ExamSpecification.omniText(form.getOmniText()));

        Page<Exam> examList;
        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<Exam> list = examRepository.findAll(specification, pageable.getSort());
            examList = new PageImpl<>(list);
        } else {
            examList = examRepository.findAll(specification, pageable);
        }
        return examList;
    }

    public Iterable<Institution> getAllInstitution() {
        return institutionRepository.findAll();
    }

    public List<Clazz> getAllClass() {
        return classRepository.findAll();
    }

    public List<Subject> getAllSubject() {
        return subjectRepository.getAllSubject();
    }

    public List<SubjectType> getAllSubjectType() {
        return subjectTypeRepository.getAllSubjectType();
    }

    public List<Exam> getAllExam() {
        return examRepository.getAllExam();
    }

    public Institution validateInstitution(Institution institution, BindingResult result) {
        boolean isRequired = false;

        if(institution.getName().trim().isEmpty()){
            result.rejectValue("name", "error.required");
            institution.setName("");
        }

        if(institution.getCode().trim().isEmpty()){
            result.rejectValue("code", "error.required");
            institution.setCode("");
        }

        if(institution.getPhone().trim().isEmpty()){
            result.rejectValue("phone", "error.required");
            institution.setPhone("");
        }

        for (InstitutionClass institutionClass : institution.getClassList()) {
            if(institutionClass.getClazz().getId() == null){
                result.rejectValue("classList", "error.required");
                isRequired = true;
                break;
            }
        }

        if(!isRequired){
            boolean isBreak = false;
            for (int i = 0; i < institution.getClassList().size(); i++) {
                for (int j = i + 1; j < institution.getClassList().size(); j++) {
                    if(institution.getClassList().get(i).getClazz().getId().equals(institution.getClassList().get(j).getClazz().getId())){
                        result.rejectValue("classList", "error.duplicate");
                        isBreak = true;
                        break;
                    }
                }
                if(isBreak){
                    break;
                }
            }
        }

        if(!institution.getName().isEmpty() && !institution.getCode().isEmpty() && isExistsInstitution(institution)){
            result.rejectValue("name", "error.exists");
        }
        return institution;
    }

    private boolean isExistsInstitution(Institution in) {
        Iterable<Institution> institutionList = getAllInstitution();
        for (Institution institution : institutionList){
            if(!(Objects.equals(in.getId(), institution.getId()))){
                if(in.getName().equalsIgnoreCase(institution.getName()) || in.getCode().equalsIgnoreCase(institution.getCode())){
                    return true;
                }
            }
        }
        return false;
    }

    public void validateClass(Clazz clazz, BindingResult result) {
        if(clazz.getName().trim().isEmpty()){
            result.rejectValue("name", "error.required");
            clazz.setName("");
        }

        if(clazz.getLevel().trim().isEmpty()){
            result.rejectValue("level", "error.required");
            clazz.setLevel("");
        }

        if(!clazz.getName().isEmpty() && isExistsClass(clazz)){
            result.rejectValue("name", "error.exists");
        }
    }

    private boolean isExistsClass(Clazz cl) {
        Iterable<Clazz> classList = getAllClass();
        for (Clazz clazz : classList){
            if(!(Objects.equals(cl.getId(), clazz.getId()))){
                if(cl.getName().equalsIgnoreCase(clazz.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    public void validateSubject(Subject subject, BindingResult result) {
        if (subject.getName().trim().isEmpty()) {
            result.rejectValue("name", "error.required");
            subject.setName("");
        }

        if (subject.getCode().trim().isEmpty()) {
            result.rejectValue("code", "error.required");
            subject.setCode("");
        }

        if(!subject.getName().isEmpty() && !subject.getCode().isEmpty() && isExistsSubject(subject)){
            result.rejectValue("name", "error.exists");
        }
    }

    private boolean isExistsSubject(Subject sub) {
        List<Subject> subjectList = getAllSubject();
        for (Subject subject: subjectList){
            if(!(Objects.equals(sub.getId(), subject.getId()))){
                if(sub.getName().equalsIgnoreCase(subject.getName()) || sub.getCode().equalsIgnoreCase(subject.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void validateSubjectType(SubjectType subjectType, BindingResult result) {
        if (subjectType.getName().trim().isEmpty()) {
            result.rejectValue("name", "error.required");
            subjectType.setName("");
        }

        if(!subjectType.getName().isEmpty() && isExistsSubjectType(subjectType)){
            result.rejectValue("name", "error.exists");
        }
    }

    private boolean isExistsSubjectType(SubjectType subType) {
        List<SubjectType> subjectTypeList = getAllSubjectType();
        for (SubjectType subjectType : subjectTypeList){
            if(!(Objects.equals(subType.getId(), subjectType.getId()))){
                if(subType.getName().equalsIgnoreCase(subjectType.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    public void validateExam(Exam exam, BindingResult result) {
        if (exam.getName().trim().isEmpty()) {
            result.rejectValue("name", "error.required");
            exam.setName("");
        }

        if(!exam.getName().isEmpty() && isExistsExam(exam)){
            result.rejectValue("name", "error.exists");
        }
    }

    private boolean isExistsExam(Exam ex) {
        List<Exam> examList = getAllExam();
        for (Exam exam : examList){
            if(!(Objects.equals(ex.getId(), exam.getId()))){
                if(ex.getName().equalsIgnoreCase(exam.getName())){
                    return true;
                }
            }
        }
        return false;
    }

    public List<UserDto> getUserByNameRegOrPhone(String nameRegOrPhone) {
        final String uri = userApiUrl + PERMITTED_USERS_URL + serviceName + "?nameRegOrPhone="+nameRegOrPhone;
        return restService.getForList(uri, new ParameterizedTypeReference<>() {
        });
    }

    public void validateUserAccess(@Valid UserAccess userAccess, BindingResult result) {
        if (userAccess.getUserId() == null) {
            result.rejectValue("userId", "error.required");
        }
    }

    public UserAccess createUserAccess(UserAccess userAccess) {
        return userAccessRepository.save(userAccess);
    }

    public UserAccess getUserAccessByUserId(Long userId) {
        return userAccessRepository.findByUserId(userId);
    }

    public boolean checkExistUserAccess(Long userId) {
        return getUserAccessByUserId(userId) != null;
    }
}
