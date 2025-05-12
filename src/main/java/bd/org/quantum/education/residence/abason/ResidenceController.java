package bd.org.quantum.education.residence.abason;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.academic.academic_class.AcademicClassService;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.academic.student.StudentSearchCriteria;
import bd.org.quantum.education.admission.student.Student;
import bd.org.quantum.education.admission.student.StudentService;
import bd.org.quantum.education.common.Group;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.common.Section;
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

@Slf4j
@Controller
public class ResidenceController {
    private final ResidenceService residenceService;
    private final AcademicReferenceService academicReferenceService;
    private final MessageSource messageSource;
    private final StudentService studentService;
    private final AcademicClassService academicClassService;

    public ResidenceController(ResidenceService residenceService,
                               AcademicReferenceService academicReferenceService,
                               AcademicClassService academicClassService,
                               StudentService studentService,
                               MessageSource messageSource) {
        this.residenceService = residenceService;
        this.academicReferenceService = academicReferenceService;
        this.messageSource = messageSource;
        this.studentService = studentService;
        this.academicClassService = academicClassService;
    }

    @GetMapping(Routes.RESIDENCE_RESIDENCE_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_VIEW"})
    public String residenceEntry(@RequestParam(name = "id", defaultValue = "0") Long id,
                                 @RequestParam(name = "result", required = false) boolean result,
                                 @RequestParam(name = "action", required = false) String action,
                                 Model model) {

        model.addAttribute("residence", (id > 0) ? residenceService.getResidenceById(id) : new Residence());
        model.addAttribute("entryLink", Routes.RESIDENCE_RESIDENCE_ENTRY);
        model.addAttribute("classes", academicReferenceService.getAllClass());

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "residence.create.success", model);
        }
        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "residence.update.success", model);
        }

        return "residence/abason/residence-entry";
    }

    @PostMapping(Routes.RESIDENCE_RESIDENCE_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_CREATE"})
    public String residenceEntry(@Valid Residence residence, BindingResult result, Model model) {

        residenceService.validateResidence(residence, result);
        String action = residence.getAction();

        try {
            if (!result.hasErrors()) {
                Residence res = residenceService.createResidence(residence);
                return "redirect:" + Routes.RESIDENCE_RESIDENCE_ENTRY + "?id=" + res.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "residence.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "residence.create.error", model);
        }

        model.addAttribute("entryLink", Routes.RESIDENCE_RESIDENCE_ENTRY);
        model.addAttribute("residence", residence);
        model.addAttribute("classes", academicReferenceService.getAllClass());

        return "residence/abason/residence-entry";
    }

    @GetMapping(Routes.RESIDENCE_RESIDENCE_LIST)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_VIEW"})
    public String residenceList(Model model) {
        model.addAttribute("searchUrl", Routes.RESIDENCE_RESIDENCE_SEARCH);
        model.addAttribute("entryUrl", Routes.RESIDENCE_RESIDENCE_ENTRY);
        return "residence/abason/residence-list";
    }

    @GetMapping(Routes.RESIDENCE_RESIDENCE_SEARCH)
    @ResponseBody
    public Page<Residence> residenceSearch(SearchForm form) {
        return residenceService.residenceSearch(form);
    }

    @GetMapping(Routes.RESIDENCE_STUDENT_LIST)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_VIEW"})
    public String studentList(Model model) {
        model.addAttribute("openUrl", Routes.RESIDENCE_STUDENT_PROFILE);
        model.addAttribute("searchUrl", Routes.ACADEMIC_STUDENT_SEARCH + "?organ=residence");
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        return "residence/student/res-student-list";
    }

    @GetMapping(Routes.RESIDENCE_STUDENT_PROFILE)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_VIEW"})
    public String studentProfile(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("academicClassList", academicClassService.getStudentAcademicClassList(id));
        return "residence/student/student-profile";
    }

    @GetMapping(Routes.RESIDENCE_STUDENT_SEARCH)
    @ResponseBody
    public Page<Student> studentSearch(StudentSearchCriteria searchCriteria) {
        return studentService.studentList(searchCriteria);
    }
}
