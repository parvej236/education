package bd.org.quantum.education.admission.student;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.academic.student.StudentSearchCriteria;
import bd.org.quantum.education.admission.applicant.Applicant;
import bd.org.quantum.education.admission.applicant.ApplicantSearchCriteria;
import bd.org.quantum.education.admission.applicant.ApplicantService;
import bd.org.quantum.education.admission.examcenter.ExamCenterService;
import bd.org.quantum.education.common.CommonService;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.common.StudentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class StudentController {

    private final MessageSource messageSource;
    private final StudentService service;
    private final ApplicantService applicantService;
    private final AcademicReferenceService academicReferenceService;
    private final CommonService commonService;
    private final ExamCenterService examCenterService;

    public StudentController(StudentService service,
                             ApplicantService applicantService,
                             AcademicReferenceService academicReferenceService,
                             MessageSource messageSource, CommonService commonService,
                             ExamCenterService examCenterService) {
        this.service = service;
        this.applicantService = applicantService;
        this.academicReferenceService = academicReferenceService;
        this.messageSource = messageSource;
        this.commonService = commonService;
        this.examCenterService = examCenterService;
    }

    @GetMapping(Routes.STUDENTS)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_VIEW"})
    public String index(Model model) {
        model.addAttribute("searchUrl", Routes.SEARCH_STUDENT);
        model.addAttribute("createUrl", Routes.CREATE_STUDENT);
        model.addAttribute("openUrl", Routes.UPDATE_STUDENT);
        model.addAttribute("religionList", commonService.religions());
        model.addAttribute("communityList", commonService.communities());
        model.addAttribute("upazilasByDistrict", Routes.UPAZILAS);
        model.addAttribute("examCenterList", examCenterService.getAllExamCenters());
        referenceData(model);
        return "admission/student/student-list";
    }

    @GetMapping(Routes.SEARCH_STUDENT)
    @ResponseBody
    public Page<Student> search(StudentSearchCriteria searchCriteria) {
        return service.search(searchCriteria);
    }

    @GetMapping(Routes.PERMITTED_APPLICANTS)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_CREATE"})
    public String create(Model model) {
        model.addAttribute("searchUrl", Routes.PERMITTED_APPLICANTS_SEARCH);
        model.addAttribute("entryUrl", Routes.CREATE_STUDENT);
        return "admission/student/student-search";
    }

    @GetMapping(Routes.PERMITTED_APPLICANTS_SEARCH)
    @ResponseBody
    public Page<Applicant> create(ApplicantSearchCriteria searchForm) {
        searchForm.setAdmitted(false);
        searchForm.setSelected(true);
        return applicantService.search(searchForm);
    }

    @GetMapping(Routes.CREATE_STUDENT)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_CREATE"})
    public String createStudent(@RequestParam(name = "applicantId") Long applicantId, Model model) {
        Student student = new Student();
        student.setApplicant(applicantService.get(applicantId));
        model.addAttribute("student", student);
        model.addAttribute("entryUrl", Routes.CREATE_STUDENT);
        referenceData(model);
        return "admission/student/student-form";
    }

    @PostMapping(Routes.CREATE_STUDENT)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_CREATE"})
    public String createStudent(@Valid Student student, BindingResult result, Model model) {
        service.validateStudent(student, result, "create");
        String action = student.getAction();
        try {
            if (!result.hasErrors()) {
                Student std = service.create(student);
                service.changeStatus(std.getApplicant().getId());
                return "redirect:" + Routes.UPDATE_STUDENT + "?id=" + std.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase() ;
            } else {
                SubmitResult.error(messageSource, "student.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "student.create.error", model);
        }

        student.setApplicant(applicantService.get(student.getApplicant().getId()));
        model.addAttribute("student", student);
        model.addAttribute("entryUrl", Routes.CREATE_STUDENT);
        referenceData(model);

        return "admission/student/student-form";
    }

    @GetMapping(Routes.UPDATE_STUDENT)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_VIEW"})
    public String update(@RequestParam(name="id", defaultValue = "0") Long id,
                         @RequestParam(name = "result", required = false) boolean result,
                         @RequestParam(name = "action", required = false) String action,
                         Model model) {
        Student student = new Student();
        if (id != null && id > 0) {
            student = service.getStudentById(id);
        }
        model.addAttribute("student", student);

        if(result && action.equals("new")) {
            SubmitResult.success(messageSource, "student.create.success", model);
        }

        if(result && action.equals("update")) {
            SubmitResult.success(messageSource, "student.update.success", model);
        }

        model.addAttribute("entryUrl", Routes.UPDATE_STUDENT);
        referenceData(model);
        return "admission/student/student-form";
    }

    @PostMapping(Routes.UPDATE_STUDENT)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_CREATE"})
    public String update(@Valid Student student, BindingResult result, Model model) {
        service.validateStudent(student, result, "update");
        String action = student.getAction();
        try {
            if (!result.hasErrors()) {
                Student existingStudent = service.getStudentById(student.getId());
                student.setDocuments(existingStudent.getDocuments());
                student.setResidenceId(existingStudent.getResidenceId());
                student.setResidenceName(existingStudent.getResidenceName());
                student.setCurrentAcademicClassId(existingStudent.getCurrentAcademicClassId());
                student.setCurrentAcademicClassInfo(existingStudent.getCurrentAcademicClassInfo());
                service.update(student);
                return "redirect:" + Routes.UPDATE_STUDENT + "?id=" + student.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "student.update.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "student.update.error", model);
        }
        model.addAttribute("student", student);
        referenceData(model);
        return "admission/student/student-form";
    }

    @GetMapping(Routes.ASSIGN_DOCUMENT)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_VIEW"})
    public String assignDocument(@RequestParam(name = "id", defaultValue = "0") Long id, @RequestParam(name = "result", required = false) boolean result, Model model) {
        Student student = new Student();
        if (id != null && id > 0) {
            student = service.getStudentById(id);
        }
        if (result) {
            SubmitResult.success(messageSource, "document.assign.success", model);
        }
        model.addAttribute("student", student);
        model.addAttribute("entryUrl", Routes.ASSIGN_DOCUMENT);
        model.addAttribute("documentCheckUrl", Routes.IS_ASSIGNED_DOCUMENT_BY_STUDENT_ID);
        return "admission/student/assign-document";
    }

    @PostMapping(Routes.ASSIGN_DOCUMENT)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_ASSIGN_DOCUMENT"})
    public String assignDocument(@Valid Student student, BindingResult result, Model model) {
        service.validateStudentDocument(student, result);
        try {
            if (!result.hasErrors()) {
                Student std = service.getStudentById(student.getId());
                std.setDocuments(student.getDocuments());
                std = service.create(std);
                return "redirect:" + Routes.ASSIGN_DOCUMENT + "?id=" + std.getId() + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "document.assign.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "document.assign.error", model);
        }
        model.addAttribute("student", student);
        model.addAttribute("entryUrl", Routes.ASSIGN_DOCUMENT);
        model.addAttribute("documentCheckUrl", Routes.IS_ASSIGNED_DOCUMENT_BY_STUDENT_ID);
        return "admission/student/assign-document";
    }

    @GetMapping(Routes.STUDENT_DOCUMENT_LIST)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_VIEW"})
    public String studentDocumentList(Model model) {
        model.addAttribute("searchUrl", Routes.STUDENT_DOCUMENT_SEARCH);
        model.addAttribute("entryUrl", Routes.ASSIGN_DOCUMENT);
        return "admission/student/student-document-list";
    }

    @GetMapping(Routes.STUDENT_DOCUMENT_SEARCH)
    @ResponseBody
    public Page<Student> studentDocumentSearch(SearchForm form) {
        return service.studentDocumentSearch(form);
    }

    @GetMapping(Routes.GET_STUDENT_BY_NAME_OR_QUANTAA_ID)
    @ResponseBody
    public ResponseEntity<List<Student>> getStudentInfoByNameOrQuantaaId(@RequestParam(name = "nameOrQuantaaId") String nameOrQuantaaId) {
        return new ResponseEntity<>(service.getStudentInfoByNameOrQuantaaId(nameOrQuantaaId), HttpStatus.OK);
    }

    @GetMapping(Routes.IS_ASSIGNED_DOCUMENT_BY_STUDENT_ID)
    @ResponseBody
    public ResponseEntity<Boolean> isDocumentAssignedToStudent(@RequestParam(name = "studentId") Long studentId) {
        return new ResponseEntity<>(service.isDocumentAssignedToStudent(studentId), HttpStatus.OK);
    }

    private void referenceData(Model model) {
        model.addAttribute("branches", service.branches());
        model.addAttribute("academicClasses", service.getAcademicClasses());
        model.addAttribute("countries", service.countries());
        model.addAttribute("districts", service.districts());
        model.addAttribute("studentStatusList", StudentStatus.values());
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
    }
}