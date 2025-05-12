package bd.org.quantum.education.admission.applicant;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.admission.examcenter.ExamCenterService;
import bd.org.quantum.education.common.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class ApplicantReportController {
    private final AcademicReferenceService academicReferenceService;
    private final ExamCenterService examCenterService;
    private final ApplicantReportService applicantReportService;
    private final CommonService commonService;

    public ApplicantReportController(AcademicReferenceService academicReferenceService,
                                     ExamCenterService examCenterService,
                                     ApplicantReportService applicantReportService,
                                     CommonService commonService) {
        this.academicReferenceService = academicReferenceService;
        this.examCenterService = examCenterService;
        this.applicantReportService = applicantReportService;
        this.commonService = commonService;
    }

    @GetMapping(Routes.APPLICANT_REPORT)
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_REPORT"})
    public String applicantReport(Model model) {
        model.addAttribute("reportCriteria", new ApplicantReportCriteria());
        model.addAttribute("reportType", ReportType.getApplicantReportTypes());
        model.addAttribute("classes", academicReferenceService.getAllClass());
        model.addAttribute("religions", Religion.values());
        model.addAttribute("communities", Community.values());
        model.addAttribute("examCenters", examCenterService.getAllExamCenters());
        model.addAttribute("districts", commonService.getDistrictList());
        return "admission/applicant/applicant-report-criteria";
    }

    @PostMapping(value = Routes.APPLICANT_REPORT, params = "format=HTML")
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_REPORT"})
    public String applicantReport(@RequestBody ApplicantReportCriteria criteria, Model model) {
        ReportData reportData = applicantReportService.generateApplicantReports(criteria);
        model.addAttribute("reportData", reportData);
        return String.format("admission/applicant/%s", reportData.getView());
    }

    @PostMapping(value = Routes.APPLICANT_REPORT, params = "format=PDF")
    @SecurityCheck(permissions = {"ADMISSION_APPLICANT_REPORT"})
    @ResponseBody
    public ReportData getMemberReports(@RequestBody ApplicantReportCriteria criteria){
        return applicantReportService.generateApplicantReports(criteria);
    }
}
