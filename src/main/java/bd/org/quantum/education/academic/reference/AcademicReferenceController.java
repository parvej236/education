package bd.org.quantum.education.academic.reference;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.residence.abason.ResidenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class AcademicReferenceController {
    private final AcademicReferenceService service;
    private final ResidenceService residenceService;
    private final MessageSource messageSource;

    public AcademicReferenceController(AcademicReferenceService service,
                                       ResidenceService residenceService,
                                       MessageSource messageSource) {
        this.service = service;
        this.residenceService = residenceService;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.ACADEMIC_INSTITUTION_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String institutionEntry(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "result", required = false) boolean result, @RequestParam(name = "action", required = false) String action, Model model) {
        model.addAttribute("institution", (id > 0) ? service.getInstitutionById(id) : new Institution());
        model.addAttribute("classes", service.getAllClass());
        model.addAttribute("entryLink", Routes.ACADEMIC_INSTITUTION_ENTRY);

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "institution.create.success", model);
        }
        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "institution.update.success", model);
        }
        return "academic/reference/institution-entry";
    }

    @PostMapping(Routes.ACADEMIC_INSTITUTION_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_CREATE"})
    public String institutionEntry(@Valid Institution institution, BindingResult result, Model model) {
        institution = service.validateInstitution(institution, result);
        String action = institution.getAction();
        try {
            if (!result.hasErrors()) {
                institution = service.createInstitution(institution);
                model.addAttribute("institution", institution);
                return "redirect:" + Routes.ACADEMIC_INSTITUTION_ENTRY + "?id=" + institution.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "institution.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "institution.create.error", model);
        }
        model.addAttribute("entryLink", Routes.ACADEMIC_INSTITUTION_ENTRY);
        model.addAttribute("institution", institution);
        model.addAttribute("classes", service.getAllClass());
        return "academic/reference/institution-entry";
    }

    @GetMapping(Routes.ACADEMIC_INSTITUTION_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String institutionList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_INSTITUTION_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_INSTITUTION_ENTRY);
        return "academic/reference/institution-list";
    }

    @GetMapping(Routes.ACADEMIC_INSTITUTION_SEARCH)
    @ResponseBody
    public Page<Institution> institutionSearch(SearchForm form) {
        return service.institutionSearch(form);
    }

    @GetMapping(Routes.ACADEMIC_CLASS_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String classList(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "result", required = false) boolean result, @RequestParam(name = "action", required = false) String action, Model model) {
        model.addAttribute("class", id > 0 ? service.getClassById(id) : new Clazz());
        model.addAttribute("entryLink", Routes.ACADEMIC_CLASS_ENTRY);

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "class.create.success", model);
        }
        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "class.update.success", model);
        }
        return "academic/reference/class-entry";
    }

    @PostMapping(Routes.ACADEMIC_CLASS_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_CREATE"})
    public String classEntry(@Valid Clazz clazz, BindingResult result, Model model) {
        service.validateClass(clazz, result);
        String action = clazz.getAction();
        try {
            if (!result.hasErrors()) {
                clazz = service.createClass(clazz);
                model.addAttribute("class", clazz);
                return "redirect:" + Routes.ACADEMIC_CLASS_ENTRY + "?id=" + clazz.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "class.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "class.create.error", model);
        }
        model.addAttribute("entryLink", Routes.ACADEMIC_CLASS_ENTRY);
        model.addAttribute("class", clazz);
        return "academic/reference/class-entry";
    }

    @GetMapping(Routes.ACADEMIC_CLASS_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String classList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_CLASS_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_ENTRY);
        return "academic/reference/class-list";
    }

    @GetMapping(Routes.ACADEMIC_CLASS_SEARCH)
    @ResponseBody
    public Page<Clazz> classSearch(SearchForm form) {
        return service.classSearch(form);
    }

    @GetMapping(Routes.ACADEMIC_SUBJECT_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String subjectEntry(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "result", required = false) boolean result, @RequestParam(name = "action", required = false) String action, Model model) {
        model.addAttribute("subject", id > 0 ? service.getSubjectById(id) : new Subject());
        model.addAttribute("entryLink", Routes.ACADEMIC_SUBJECT_ENTRY);

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "subject.create.success", model);
        }

        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "subject.update.success", model);
        }
        return "academic/reference/subject-entry";
    }

    @PostMapping(Routes.ACADEMIC_SUBJECT_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_CREATE"})
    public String SubjectEntry(@Valid Subject subject, BindingResult result, Model model) {
        service.validateSubject(subject, result);
        String action = subject.getAction();
        try {
            if (!result.hasErrors()) {
                subject = service.createSubject(subject);
                return "redirect:" + Routes.ACADEMIC_SUBJECT_ENTRY + "?id=" + subject.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "subject.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "subject.create.error", model);
        }
        model.addAttribute("entryLink", Routes.ACADEMIC_SUBJECT_ENTRY);
        model.addAttribute("subject", subject);
        return "academic/reference/subject-entry";
    }

    @GetMapping(Routes.ACADEMIC_SUBJECT_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String subjectList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_SUBJECT_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_SUBJECT_ENTRY);
        return "academic/reference/subject-list";
    }

    @GetMapping(Routes.ACADEMIC_SUBJECT_SEARCH)
    @ResponseBody
    public Page<Subject> subjectSearch(SearchForm form) {
        return service.subjectSearch(form);
    }

    @GetMapping(Routes.ACADEMIC_SUBJECT_TYPE_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String subjectTypeEntry(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "result", required = false) boolean result, @RequestParam(name = "action", required = false) String action, Model model) {
        model.addAttribute("subjectType", id > 0 ? service.getSubjectTypeById(id) : new SubjectType());
        model.addAttribute("entryLink", Routes.ACADEMIC_SUBJECT_TYPE_ENTRY);

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "subject.type.create.success", model);
        }

        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "subject.type.update.success", model);
        }
        return "academic/reference/subject-type-entry";
    }

    @PostMapping(Routes.ACADEMIC_SUBJECT_TYPE_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_CREATE"})
    public String SubjectTypeEntry(@Valid SubjectType subjectType, BindingResult result, Model model) {
        service.validateSubjectType(subjectType, result);
        String action = subjectType.getAction();
        try {
            if (!result.hasErrors()) {
                subjectType = service.createSubjectType(subjectType);
                return "redirect:" + Routes.ACADEMIC_SUBJECT_TYPE_ENTRY + "?id=" + subjectType.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "subject.type.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "subject.type.create.error", model);
        }
        model.addAttribute("entryLink", Routes.ACADEMIC_SUBJECT_TYPE_ENTRY);
        return "academic/reference/subject-type-entry";
    }

    @GetMapping(Routes.ACADEMIC_SUBJECT_TYPE_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String subjectTypeList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_SUBJECT_TYPE_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_SUBJECT_TYPE_ENTRY);
        return "academic/reference/subject-type-list";
    }

    @GetMapping(Routes.ACADEMIC_SUBJECT_TYPE_SEARCH)
    @ResponseBody
    public Page<SubjectType> subjectTypeSearch(SearchForm form) {
        return service.subjectTypeSearch(form);
    }

    @GetMapping(Routes.ACADEMIC_EXAM_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String examEntry(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "result", required = false) boolean result, @RequestParam(name = "action", required = false) String action, Model model) {
        model.addAttribute("exam", id > 0 ? service.getExamById(id) : new Exam());
        model.addAttribute("entryLink", Routes.ACADEMIC_EXAM_ENTRY);

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "exam.create.success", model);
        }

        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "exam.update.success", model);
        }
        return "academic/reference/exam-entry";
    }

    @PostMapping(Routes.ACADEMIC_EXAM_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_CREATE"})
    public String examEntry(@Valid Exam exam, BindingResult result, Model model) {
        service.validateExam(exam, result);
        String action = exam.getAction();
        try {
            if (!result.hasErrors()) {
                exam = service.createExam(exam);
                return "redirect:" + Routes.ACADEMIC_EXAM_ENTRY + "?id=" + exam.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "exam.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "exam.create.error", model);
        }
        model.addAttribute("entryLink", Routes.ACADEMIC_EXAM_ENTRY);
        model.addAttribute("exam", exam);
        return "academic/reference/exam-entry";
    }

    @GetMapping(Routes.ACADEMIC_EXAM_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_VIEW"})
    public String examList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_EXAM_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_EXAM_ENTRY);
        return "academic/reference/exam-list";
    }

    @GetMapping(Routes.ACADEMIC_EXAM_SEARCH)
    @ResponseBody
    public Page<Exam> examSearch(SearchForm form) {
        return service.examSearch(form);
    }

    @GetMapping(Routes.ACADEMIC_USERS_BY_NAME_REG_OR_PHONE)
    @ResponseBody
    public List<UserDto> getUserByNameRegOrPhone(@RequestParam(name = "nameRegOrPhone") String nameRegOrPhone) {
        return service.getUserByNameRegOrPhone(nameRegOrPhone);
    }

    @GetMapping(Routes.ACADEMIC_USER_ACCESS)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_USER_ACCESS"})
    public String userAccess(@RequestParam(name = "userId", defaultValue = "0") Long userId,
                             @RequestParam(name = "result", required = false) boolean result,
                             Model model) {
        model.addAttribute("userAccess", (userId == 0) ? new UserAccess() : service.getUserAccessByUserId(userId));
        model.addAttribute("institutionList", service.getAllInstitution());
        model.addAttribute("residenceList", residenceService.getAllResidence());
        model.addAttribute("existCheckUrl", Routes.ACADEMIC_USER_EXIST_CHECK);
        model.addAttribute("entryUrl", Routes.ACADEMIC_USER_ACCESS);
        if (userId != 0 && result) {
            SubmitResult.success(messageSource, "user.access.update.success", model);
        }
        return "academic/reference/user-access";
    }

    @PostMapping(Routes.ACADEMIC_USER_ACCESS)
    @SecurityCheck(permissions = {"ACADEMIC_REFERENCE_USER_ACCESS"})
    public String userAccess(@Valid UserAccess userAccess, BindingResult result, Model model) {
        service.validateUserAccess(userAccess, result);
        UserAccess usrAccess;
        try {
            if (!result.hasErrors()) {
                usrAccess = service.createUserAccess(userAccess);
                return "redirect:" + Routes.ACADEMIC_USER_ACCESS + "?userId=" + usrAccess.getUserId() + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "user.access.update.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "user.access.update.error", model);
        }
        model.addAttribute("userAccess", userAccess);
        model.addAttribute("institutionList", service.getAllInstitution());
        model.addAttribute("existCheckUrl", Routes.ACADEMIC_USER_EXIST_CHECK);
        model.addAttribute("entryUrl", Routes.ACADEMIC_USER_ACCESS);
        return "academic/reference/user-access";
    }

    @GetMapping(Routes.ACADEMIC_USER_EXIST_CHECK)
    @ResponseBody
    public boolean checkExistUserAccess(@RequestParam(name = "userId") Long userId) {
        return service.checkExistUserAccess(userId);
    }
}