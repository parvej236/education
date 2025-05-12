package bd.org.quantum.education.academic.examination;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.academic.academic_class.AcademicClass;
import bd.org.quantum.education.academic.academic_class.AcademicClassService;
import bd.org.quantum.education.academic.academic_class.SubjectDetails;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.common.StudentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class AcademicExaminationController {
    private final AcademicExaminationService service;
    private final AcademicReferenceService referenceService;
    private final AcademicClassService academicClassService;
    private final MessageSource messageSource;

    public AcademicExaminationController(AcademicExaminationService service,
                                         AcademicReferenceService referenceService,
                                         AcademicClassService academicClassService,
                                         MessageSource messageSource) {
        this.service = service;
        this.referenceService = referenceService;
        this.academicClassService = academicClassService;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.ACADEMIC_EXAM_MARK_SHEET_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_EXAMINATION_MARKS"})
    public String examMarksEntry(Model model) {
        model.addAttribute("examMarks", new ExamMarks());
        model.addAttribute("institutionList", referenceService.getAllInstitution());
        model.addAttribute("classInfoList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("subjectList", Routes.ACADEMIC_GET_SUBJECT_LIST_BASE_ON_ACADEMIC_CLASS);
        model.addAttribute("examList", referenceService.getAllExam());
        model.addAttribute("entryUrl", Routes.ACADEMIC_EXAM_MARK_SHEET_ENTRY);
        return "academic/examination/examination-mark-sheet-entry";
    }

    @PostMapping(Routes.ACADEMIC_EXAM_MARK_SHEET_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_EXAMINATION_MARKS"})
    public String examMarksEntry(@Valid ExamMarks examMarks, BindingResult result, Model model) {
        service.validation(examMarks, result);
        try {
            if (!result.hasErrors()) {
                examMarks = service.createExamMarks(examMarks);
                return "redirect:" + Routes.ACADEMIC_EXAM_MARK_ENTRY + "?id=" + examMarks.getId();
            } else {
                SubmitResult.error(messageSource, "examination.exam.marks.created.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "examination.exam.marks.created.error", model);
        }

        model.addAttribute("examMarks", examMarks);
        model.addAttribute("institutionList", referenceService.getAllInstitution());
        model.addAttribute("classInfoList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("subjectList", Routes.ACADEMIC_GET_SUBJECT_LIST_BASE_ON_ACADEMIC_CLASS);
        model.addAttribute("examList", referenceService.getAllExam());
        model.addAttribute("entryUrl", Routes.ACADEMIC_EXAM_MARK_SHEET_ENTRY);
        return "academic/examination/examination-mark-sheet-entry";
    }

    @GetMapping(Routes.ACADEMIC_EXAM_MARK_SHEET_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_EXAMINATION_VIEW"})
    public String examMarksList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_EXAM_MARKS_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_EXAM_MARK_ENTRY);
        model.addAttribute("institutionList", referenceService.getAllInstitution());
        model.addAttribute("classInfoList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("subjectList", Routes.ACADEMIC_GET_SUBJECT_LIST_BASE_ON_ACADEMIC_CLASS);
        model.addAttribute("examList", referenceService.getAllExam());
        return "academic/examination/examination-mark-sheet-list";
    }

    @GetMapping(Routes.ACADEMIC_EXAM_MARKS_SEARCH)
    @ResponseBody
    public Page<ExamMarks> examMarksSearch(ExamMarksSearch search) {
        return service.examMarksSearch(search);
    }

    @GetMapping(Routes.ACADEMIC_EXAM_MARK_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_EXAMINATION_VIEW"})
    public String examMarkEntry(@RequestParam(name = "id") Long id, @RequestParam(name = "action", defaultValue = "0") Integer action, @RequestParam(name = "result", required = false) boolean result, Model model) {
        ExamMarks examMarks = service.getExamMarksById(id);
        model.addAttribute("markSheet", examMarks);
        model.addAttribute("currentStudent", StudentStatus.CURRENT.getTitle());
        model.addAttribute("status", examMarks.getStatus());
        model.addAttribute("classInfoUrl", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("entryUrl", Routes.ACADEMIC_EXAM_MARK_ENTRY);
        if (result && action == 0) {
            SubmitResult.success(messageSource, "examination.exam.marks.saved.success", model);
        }
        if (result && action == 1) {
            SubmitResult.success(messageSource, "examination.exam.marks.updated.success", model);
        }
        if (result && action == 2) {
            SubmitResult.success(messageSource, "examination.exam.marks.submitted.success", model);
        }
        return "academic/examination/examination-mark-entry";
    }

    @PostMapping(Routes.ACADEMIC_EXAM_MARK_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_EXAMINATION_MARKS"})
    public String examMarkEntry(@Valid ExamMarks examMarks, BindingResult result, Model model) {
        service.studentMarksValidation(examMarks, result);
        try {
            if (!result.hasErrors()) {
                ExamMarks marks = service.getExamMarksById(examMarks.getId());
                marks.setStudentMarks(examMarks.getStudentMarks());
                if (examMarks.getAction() == 0 || examMarks.getAction() == 1) {
                    marks.setStatus(1);
                } else {
                    marks.setStatus(2);
                }
                service.createExamMarks(marks);
                return "redirect:" + Routes.ACADEMIC_EXAM_MARK_ENTRY + "?id=" + examMarks.getId() + "&action=" + examMarks.getAction() + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "examination.exam.marks.saved.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "examination.exam.marks.saved.error", model);
        }
        model.addAttribute("markSheet", service.getExamMarksById(examMarks.getId()));
        model.addAttribute("currentStudent", StudentStatus.CURRENT.getTitle());
        return "academic/examination/examination-mark-entry";
    }

    @GetMapping(Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION)
    @ResponseBody
    public String getAcademicClassList(@PathVariable Long id) {
        String option = "<option value='0'>-Please Select-</option>";
        List<AcademicClass> academicClassList = academicClassService.getAcademicClassListByInstitution(id, 2);

        for (AcademicClass academicClass : academicClassList) {
            option += "<option value='" + academicClass.getId() + "'>" + academicClass.getAcademicClassInfo() + "</option>";
        }
        return option;
    }

    @GetMapping(Routes.ACADEMIC_GET_SUBJECT_LIST_BASE_ON_ACADEMIC_CLASS)
    @ResponseBody
    public String getSubjectList(@PathVariable Long id) {
        String option = "<option value='0'>-Please Select-</option>";
        List<SubjectDetails> subjectDetailsList = academicClassService.getAcademicClassById(id).getSubjectDetailsList();

        for (SubjectDetails subjectDetails : subjectDetailsList) {
            option += "<option value='" + subjectDetails.getSubject().getId() + "'>" + subjectDetails.getSubject().getName() + "</option>";
        }
        return option;
    }
}
