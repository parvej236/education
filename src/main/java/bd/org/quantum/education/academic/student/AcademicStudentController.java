package bd.org.quantum.education.academic.student;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.authorizer.helper.UserContext;
import bd.org.quantum.authorizer.model.UserDetails;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.academic.academic_class.AcademicClass;
import bd.org.quantum.education.academic.academic_class.AcademicClassService;
import bd.org.quantum.education.academic.academic_class.AcademicClassStudent;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.academic.reference.Institution;
import bd.org.quantum.education.academic.reference.UserAccess;
import bd.org.quantum.education.admission.student.Student;
import bd.org.quantum.education.admission.student.StudentService;
import bd.org.quantum.education.common.Group;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.common.Section;
import bd.org.quantum.education.common.StudentStatus;
import bd.org.quantum.education.residence.abason.Residence;
import bd.org.quantum.education.residence.abason.ResidenceService;
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
public class AcademicStudentController {
    private final AcademicStudentService service;
    private final AcademicReferenceService academicReferenceService;
    private final StudentService studentService;
    private final AcademicClassService academicClassService;
    private final MessageSource messageSource;
    private final ResidenceService residenceService;

    public AcademicStudentController(AcademicReferenceService academicReferenceService,
                                     AcademicStudentService service,
                                     StudentService studentService,
                                     AcademicClassService academicClassService,
                                     MessageSource messageSource,
                                     ResidenceService residenceService) {
        this.academicReferenceService = academicReferenceService;
        this.service = service;
        this.studentService = studentService;
        this.academicClassService = academicClassService;
        this.messageSource = messageSource;
        this.residenceService = residenceService;
    }

    @GetMapping(Routes.ACADEMIC_STUDENT_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_VIEW"})
    public String studentList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_STUDENT_SEARCH + "?organ=academic");
        model.addAttribute("openUrl", Routes.ACADEMIC_STUDENT_PROFILE);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        return "academic/student/student-list";
    }

    @GetMapping(Routes.ACADEMIC_STUDENT_SEARCH)
    @ResponseBody
    public Page<Student> studentSearch(StudentSearchCriteria searchCriteria,
                                       @RequestParam(name = "isInClass", required = false) Boolean isInClass,
                                       @RequestParam(name = "organ", required = false) String organ) {
        UserDetails user = UserContext.getPrincipal().getUserDetails();
        List<Long> accessInstitutions = new ArrayList<>();
        List<Long> accessResidences = new ArrayList<>();

        if (user.getSuperUser()) {
            for(Institution institution : academicReferenceService.getAllInstitution()) {
                accessInstitutions.add(institution.getId());
            }
            for(Residence residence : residenceService.getAllResidence()) {
                accessResidences.add(residence.getId());
            }
        }else{
            UserAccess userAccess = academicReferenceService.getUserAccessByUserId(user.getId());
            accessInstitutions = userAccess.getAllAccessInstitutionId();
            accessResidences = userAccess.getAllAccessResidenceId();
        }
        searchCriteria.setOrgan(organ);
        searchCriteria.setIsInClass(isInClass);
        searchCriteria.setAccessInstitutions(accessInstitutions);
        searchCriteria.setAccessResidences(accessResidences);
        return studentService.studentList(searchCriteria);
    }

    @GetMapping(Routes.ACADEMIC_STUDENT_PROFILE)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_VIEW"})
    public String studentProfile(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("academicClassList", academicClassService.getStudentAcademicClassList(id));
        return "academic/student/student-profile";
    }

    @GetMapping(Routes.ACADEMIC_STUDENT_LIST_WITHOUT_CLASS)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_ASSIGN_TO_CLASS"})
    public String studentListWithoutClass(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_STUDENT_SEARCH + "?isInClass=false&organ=academic");
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        model.addAttribute("openUrl", Routes.ACADEMIC_ASSIGN_STUDENT_TO_CLASS);
        return "academic/student/student-list-without-class";
    }

    @GetMapping(Routes.ACADEMIC_ASSIGN_STUDENT_TO_CLASS)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_ASSIGN_TO_CLASS"})
    public String assignStudentToClass(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        return "academic/student/student-assign-to-class";
    }

    @PostMapping(Routes.ACADEMIC_ASSIGN_STUDENT_TO_CLASS)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_ASSIGN_TO_CLASS"})
    public String assignStudentToClass(@Valid Student student, BindingResult result, Model model) {
        service.validationStudentAssignToClass(student, result);
        try {
            if (!result.hasErrors()) {
                Student std = studentService.getStudentById(student.getId());
                AcademicClass academicClass = academicClassService.getAcademicClassById(student.getCurrentAcademicClassId());
                AcademicClassStudent classStudent = new AcademicClassStudent();

                std.setCurrentAcademicClassId(student.getCurrentAcademicClassId());
                std.setCurrentAcademicClassInfo(student.getCurrentAcademicClassInfo());
                std = studentService.create(std);

                classStudent.setStudent(std);
                classStudent.setStatus(StudentStatus.CURRENT.getTitle());
                classStudent.setRemarks(StudentStatus.NEW.getTitle());

                academicClass.getStudentList().add(classStudent);
                academicClassService.createAcademicClass(academicClass);

                return "redirect:" + Routes.ACADEMIC_STUDENT_LIST_WITHOUT_CLASS;
            } else {
                SubmitResult.error(messageSource, "student.assign.class.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "student.assign.class.error", model);
        }
        model.addAttribute("student", studentService.getStudentById(student.getId()));
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        return "academic/student/student-assign-to-class";
    }

    @GetMapping(Routes.ACADEMIC_SHIFT_STUDENT)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_SHIFT"})
    public String studentShift(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "result", required = false) boolean result, Model model) {
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("detailsUrl", Routes.ACADEMIC_STUDENT_PROFILE);

        if (id > 0) {
            StudentShift studentShift = service.getStudentShiftById(id);
            model.addAttribute("shiftDate", studentShift.getCreatedAt().toString().split(" ")[0]);
            model.addAttribute("studentShift", studentShift);
        } else {
            model.addAttribute("studentShift", new StudentShift());
        }

        if (result) {
            SubmitResult.success(messageSource, "class.promotion.student.shift.create.success", model);
        }

        return "academic/student/student-shift";
    }

    @PostMapping(Routes.ACADEMIC_SHIFT_STUDENT)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_SHIFT"})
    public String studentShift(@ModelAttribute StudentShift studentShift, BindingResult result, Model model) {
        service.validationShiftStudent(studentShift, result);
        try {
            if (!result.hasErrors()) {
                AcademicClass preAcademicClass = academicClassService.getAcademicClassById(studentShift.getPreAcademicClass());
                AcademicClass newAcademicClass = academicClassService.getAcademicClassById(studentShift.getNewAcademicClass());
                Student student = studentService.getStudentById(studentShift.getStudentRowId());

                student.setCurrentAcademicClassId(newAcademicClass.getId());
                student.setCurrentAcademicClassInfo(studentShift.getShiftToClassInfo());
                student.setInstitutionName(studentShift.getShiftToInstitution());

                student = studentService.create(student);

                for (AcademicClassStudent academicStudent : preAcademicClass.getStudentList()) {
                    if (academicStudent.getStudent().getStudentId().equals(studentShift.getStudentId())
                            && academicStudent.getStatus().equals(StudentStatus.CURRENT.getTitle())) {
                        academicStudent.setStatus(StudentStatus.SHIFTED.getTitle());
                        break;
                    }
                }

                newAcademicClass.getStudentList().add(new AcademicClassStudent(student, StudentStatus.CURRENT.getTitle(), StudentStatus.SHIFTED.getTitle()));
                academicClassService.createAcademicClass(preAcademicClass);
                academicClassService.createAcademicClass(newAcademicClass);

                studentShift.setShiftBy(UserContext.getPrincipal().getUserDetails().getName());
                studentShift = service.save(studentShift);
                return "redirect:" + Routes.ACADEMIC_SHIFT_STUDENT + "?id=" + studentShift.getId() + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "class.promotion.student.shift.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "class.promotion.student.shift.create.error", model);
        }
        return "academic/student/student-shift";
    }

    @GetMapping(Routes.ACADEMIC_SHIFT_LOG)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_SHIFT"})
    public String shiftLogList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_SHIFT_LOG_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_SHIFT_STUDENT);
        return "academic/student/student-shift-log";
    }

    @GetMapping(Routes.ACADEMIC_SHIFT_LOG_SEARCH)
    @ResponseBody
    public Page<StudentShift> shiftLogSearch(SearchForm searchForm) {
        return service.shiftLogList(searchForm);
    }

    @GetMapping(Routes.ACADEMIC_RELEASABLE_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_RELEASE"})
    public String academicClassReleasableList(@RequestParam(name = "result", required = false) boolean result, Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_CLASS_CLASS_SEARCH + "?status=2&releaseStatus=0");
        model.addAttribute("entryUrl", Routes.ACADEMIC_RELEASE_STUDENT);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        if (result) {
            SubmitResult.success(messageSource, "student.release.success", model);
        }
        return "academic/student/student-releasable-list";
    }

    @GetMapping(Routes.ACADEMIC_RELEASE_STUDENT)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_RELEASE"})
    public String academicReleaseStudent(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("releaseClass", new ReleaseClass());
        model.addAttribute("academicClass", academicClassService.getAcademicClassById(id));
        model.addAttribute("currentStudent", StudentStatus.CURRENT.getTitle());
        return "academic/student/student-release";
    }

    @PostMapping(Routes.ACADEMIC_RELEASE_STUDENT)
    @SecurityCheck(permissions = {"ACADEMIC_STUDENT_RELEASE"})
    public String academicReleaseStudent(@Valid ReleaseClass releaseClass, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                AcademicClass academicClass = academicClassService.getAcademicClassById(releaseClass.getAcademicClassId());
                if (releaseClass.getStudents() != null) {
                    for (ReleaseStudent releaseStudent : releaseClass.getStudents()) {
                        Student student = studentService.getStudentById(releaseStudent.getStudentId());
                        String status = "";

                        if (releaseStudent.getStatus() == 0) {
                            status = StudentStatus.OUTER.getTitle();
                        } else if (releaseStudent.getStatus() == 1) {
                            status = StudentStatus.EX.getTitle();
                        } else {
                            status = StudentStatus.DROPOUT.getTitle();
                        }

                        student = studentService.create(student);

                        for (AcademicClassStudent academicStudent : academicClass.getStudentList()) {
                            if (academicStudent.getStudent().getId().equals(student.getId())) {
                                academicStudent.setStatus(status);
                            }
                        }
                    }
                }
                academicClass.setReleaseStatus(1);
                academicClassService.createAcademicClass(academicClass);
                return "redirect:" + Routes.ACADEMIC_RELEASABLE_LIST + "?result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "student.release.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "student.release.error", model);
        }
        model.addAttribute("searchUrl", Routes.ACADEMIC_CLASS_CLASS_SEARCH + "?status=1");
        model.addAttribute("entryUrl", Routes.ACADEMIC_RELEASE_STUDENT);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        return "academic/student/student-release";
    }
}
