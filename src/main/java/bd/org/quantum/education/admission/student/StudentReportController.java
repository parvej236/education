package bd.org.quantum.education.admission.student;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.common.*;
import bd.org.quantum.education.residence.abason.ResidenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class StudentReportController {
    private final AcademicReferenceService academicReferenceService;
    private final StudentReportService studentReportService;
    private final ResidenceService residenceService;
    private final CommonService commonService;

    public StudentReportController(AcademicReferenceService academicReferenceService, StudentReportService studentReportService,
                                   ResidenceService residenceService, CommonService commonService) {
        this.academicReferenceService = academicReferenceService;
        this.studentReportService = studentReportService;
        this.residenceService = residenceService;
        this.commonService = commonService;
    }

    @GetMapping(Routes.STUDENT_REPORT)
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_REPORT"})
    public String studentReport(Model model) {
        model.addAttribute("reportCriteria", new StudentReportCriteria());
        model.addAttribute("reportType", ReportType.getStudentReportTypes());
        model.addAttribute("institutions", academicReferenceService.getAllInstitution());
        model.addAttribute("classes", academicReferenceService.getAllClass());
        model.addAttribute("residences", residenceService.getAllResidence());
        model.addAttribute("religions", Religion.values());
        model.addAttribute("communities", Community.values());
        model.addAttribute("studentStatusList", StudentStatus.values());
        model.addAttribute("districts", commonService.getDistrictList());
        return "admission/student/student-report-criteria";
    }

    @PostMapping(value = Routes.STUDENT_REPORT, params = "format=HTML")
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_REPORT"})
    public String studentReport(@RequestBody StudentReportCriteria criteria, Model model) {
        ReportData reportData = studentReportService.generateApplicantReports(criteria);
        model.addAttribute("reportData", reportData);
        return String.format("admission/student/%s", reportData.getView());
    }

    @PostMapping(value = Routes.STUDENT_REPORT, params = "format=PDF")
    @SecurityCheck(permissions = {"ADMISSION_STUDENT_REPORT"})
    @ResponseBody
    public ReportData studentReport(@RequestBody StudentReportCriteria criteria){
        return studentReportService.generateApplicantReports(criteria);
    }
}
