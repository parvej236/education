package bd.org.quantum.education.academic.academic_class;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.academic.reference.Institution;
import bd.org.quantum.education.academic.reference.InstitutionClass;
import bd.org.quantum.education.admission.student.Student;
import bd.org.quantum.education.admission.student.StudentService;
import bd.org.quantum.education.common.Group;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.common.Section;
import bd.org.quantum.education.common.StudentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class AcademicClassController {
    private final AcademicReferenceService academicReferenceService;
    private final AcademicClassService service;
    private final StudentService studentService;
    private final MessageSource messageSource;

    public AcademicClassController(AcademicReferenceService academicReferenceService,
                                   AcademicClassService service,
                                   StudentService studentService,
                                   MessageSource messageSource) {
        this.academicReferenceService = academicReferenceService;
        this.service = service;
        this.studentService = studentService;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.ACADEMIC_CLASS_CLASS_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_VIEW"})
    public String academicClassEntry(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "result", required = false) boolean result, Model model) {
        if (id == null) {
            model.addAttribute("academicClass", new AcademicClass());
            model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
            model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
            model.addAttribute("groupList", Group.getAll());
            model.addAttribute("sectionList", Section.values());
            model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_CLASS_ENTRY);
        } else {
            AcademicClass academicClass = service.getAcademicClassById(id);
            List<AcademicClassStudent> currentStudentList = new ArrayList<>();
            List<AcademicClassStudent> othersStudentList = new ArrayList<>();

            for (AcademicClassStudent academicStudent : academicClass.getStudentList()) {
                if (academicStudent.getStatus().equals(StudentStatus.CURRENT.getTitle())) {
                    currentStudentList.add(academicStudent);
                } else {
                    othersStudentList.add(academicStudent);
                }
            }
            model.addAttribute("currentStudentList", currentStudentList);
            model.addAttribute("othersStudentList", othersStudentList);
            model.addAttribute("academicClass", academicClass);
        }
        if (result) {
            SubmitResult.success(messageSource, "class.promotion.create.success", model);
        }
        return "academic/academic_class/academic-class-entry";
    }

    @PostMapping(Routes.ACADEMIC_CLASS_CLASS_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_CREATE"})
    public String academicClassEntry(@Valid AcademicClass academicClass, BindingResult result, Model model) {
        service.validateAcademicClass(academicClass, result);
        try {
            if (!result.hasErrors()) {
                academicClass.setReleaseStatus(0);
                academicClass = service.createAcademicClass(academicClass);
                return "redirect:" + Routes.ACADEMIC_CLASS_CLASS_ENTRY + "?id=" + academicClass.getId() + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "class.promotion.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "class.promotion.create.error", model);
        }
        model.addAttribute("academicClass", academicClass);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_CLASS_ENTRY);
        return "academic/academic_class/academic-class-entry";
    }

    @GetMapping(Routes.ACADEMIC_CLASS_CLASS_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_VIEW"})
    public String academicClassList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_CLASS_CLASS_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_CLASS_ENTRY);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        return "academic/academic_class/academic-class-list";
    }

    @GetMapping(Routes.ACADEMIC_CLASS_CLASS_SEARCH)
    @ResponseBody
    public Page<AcademicClass> academicClassSearch(@RequestParam(name = "status", required = false) Integer status, @RequestParam(name = "releaseStatus", required = false) Integer releaseStatus, AcademicClassSearchCriteria search) {
        search.setStatus(status);
        search.setReleaseStatus(releaseStatus);
        return service.academicClassSearch(search);
    }

    @GetMapping(Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION)
    @ResponseBody
    public String getInstitution(@PathVariable Long id) {
        String option = "<option value='0'>-Please Select-</option>";
        Institution institution = academicReferenceService.getInstitutionById(id);

        for (InstitutionClass clazz : institution.getClassList()) {
            option += "<option value='" + clazz.getClazz().getId() + "'>" + clazz.getClazz().getName() + "</option>";
        }
        return option;
    }

    @GetMapping(Routes.ACADEMIC_CLASS_SUBJECT_ASSIGNED_CLASS_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_SUBJECT_ASSIGN"})
    public String academicClassSubjectAssignedList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_CLASS_CLASS_SEARCH + "?status=1");
        model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_SUBJECT_ASSIGN);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        return "academic/academic_class/academic-class-subject-assigned-list";
    }

    @GetMapping(Routes.ACADEMIC_CLASS_SUBJECT_NOT_ASSIGNED_CLASS_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_SUBJECT_ASSIGN"})
    public String academicClassSubjectNotAssignedList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_CLASS_CLASS_SEARCH + "?status=0");
        model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_SUBJECT_ASSIGN);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        return "academic/academic_class/academic-class-subject-not-assigned-list";
    }

    @GetMapping(Routes.ACADEMIC_CLASS_SUBJECT_ASSIGN)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_SUBJECT_ASSIGN"})
    public String academicClassSubjectAssign(@RequestParam(name = "id") Long id, @RequestParam(name = "action", required = false) String action, @RequestParam(name = "result", required = false) boolean result, Model model) {
        model.addAttribute("academicClass", service.getAcademicClassById(id));
        model.addAttribute("subjectList", academicReferenceService.getAllSubject());
        model.addAttribute("subjectTypeList", academicReferenceService.getAllSubjectType());
        model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_SUBJECT_ASSIGN);

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "class.promotion.subject.assign.success", model);
        }
        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "class.promotion.subject.assign.update.success", model);
        }
        if (result && action.equals("submit")) {
            SubmitResult.success(messageSource, "class.promotion.subject.assign.submit.success", model);
        }
        return "academic/academic_class/academic-class-subject-assign";
    }

    @PostMapping(Routes.ACADEMIC_CLASS_SUBJECT_ASSIGN)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_SUBJECT_ASSIGN"})
    public String academicClassSubjectAssign(@Valid AcademicClass academicClass, BindingResult result, Model model) {
        service.validateAcaClass(academicClass, result);
        String action = academicClass.getAction();
        AcademicClass acaClass = service.getAcademicClassById(academicClass.getId());
        acaClass.setSubjectDetailsList(academicClass.getSubjectDetailsList());

        try {
            if (!result.hasErrors()) {
                acaClass = service.setStatus(acaClass, action);
                service.createAcademicClass(acaClass);
                return "redirect:" + Routes.ACADEMIC_CLASS_SUBJECT_ASSIGN + "?id=" + acaClass.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "class.promotion.subject.assign.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "class.promotion.subject.assign.error", model);
        }
        model.addAttribute("academicClass", service.getAcademicClassById(academicClass.getId()));
        model.addAttribute("subjectList", academicReferenceService.getAllSubject());
        model.addAttribute("subjectTypeList", academicReferenceService.getAllSubjectType());
        model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_SUBJECT_ASSIGN);
        return "academic/academic_class/academic-class-subject-assign";
    }

    @GetMapping(Routes.ACADEMIC_CLASS_PROMOTABLE_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_PROMOTION"})
    public String academicClassPromotableList(@RequestParam(name = "result", required = false) boolean result, Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_CLASS_CLASS_SEARCH + "?status=2&releaseStatus=0");
        model.addAttribute("entryUrl", Routes.ACADEMIC_CLASS_PROMOTION);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        if (result) {
            SubmitResult.success(messageSource, "class.promotion.student.promotion.create.success", model);
        }
        return "academic/academic_class/academic-class-promotion-list";
    }

    @GetMapping(Routes.ACADEMIC_CLASS_PROMOTION)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_PROMOTION"})
    public String academicClassPromotion(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("academicClassPromotion", new AcademicClassPromotion());
        model.addAttribute("academicClass", service.getAcademicClassById(id));
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("currentStudent", StudentStatus.CURRENT.getTitle());
        return "academic/academic_class/academic-class-promotion";
    }

    @PostMapping(Routes.ACADEMIC_CLASS_PROMOTION)
    @SecurityCheck(permissions = {"ACADEMIC_CLASS_PROMOTION"})
    public String academicClassPromotion(@Valid AcademicClassPromotion academicClassPromotion, BindingResult result, Model model) {
        service.validAcademicPromotion(academicClassPromotion, result);
        try {
            if (!result.hasErrors()) {
                AcademicClass preAcademicClass = service.getAcademicClassById(academicClassPromotion.getPreAcademicClass());
                AcademicClass newAcademicClass = service.getAcademicClassById(academicClassPromotion.getNewAcademicClass());

                for (PromotionStudent promotion : academicClassPromotion.getStudentList()) {
                    if (promotion.getIsPromotion() != null) {
                        Student student = studentService.getStudentById(promotion.getStudentId());

                        student.setCurrentAcademicClassId(newAcademicClass.getId());
                        student.setCurrentAcademicClassInfo(newAcademicClass.getAcademicClassInfo());
                        student.setInstitutionId(newAcademicClass.getInstitution().getId());
                        student.setInstitutionName(newAcademicClass.getInstitution().getName());

                        Student std = studentService.create(student);

                        newAcademicClass.getStudentList().add(new AcademicClassStudent(std, StudentStatus.CURRENT.getTitle(), "Promoted from previous class"));

                        int index = 0;
                        for (AcademicClassStudent academicStudent : preAcademicClass.getStudentList()) {
                            if (academicStudent.getStudent().getId().equals(promotion.getStudentId()) && academicStudent.getStatus().equals(StudentStatus.CURRENT.getTitle())) {
                                preAcademicClass.getStudentList().get(index).setStatus(StudentStatus.PROMOTED.getTitle());
                                break;
                            }
                            index++;
                        }
                    }
                }

                boolean flag = true;
                for (AcademicClassStudent student : preAcademicClass.getStudentList()) {
                    if (student.getStatus().equals(StudentStatus.CURRENT.getTitle())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    preAcademicClass.setStatus(3);
                }

                service.createAcademicClass(preAcademicClass);
                service.createAcademicClass(newAcademicClass);
                return "redirect:" + Routes.ACADEMIC_CLASS_PROMOTABLE_LIST + "?result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "class.promotion.student.promotion.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "class.promotion.student.promotion.create.error", model);
        }
        model.addAttribute("academicClassPromotion", new AcademicClassPromotion());
        model.addAttribute("academicClass", service.getAcademicClassById(academicClassPromotion.getPreAcademicClass()));
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("currentStudent", StudentStatus.CURRENT.getTitle());
        model.addAttribute("promotionList", academicClassPromotion.getStudentList());
        return "academic/academic_class/academic-class-promotion";
    }
}
