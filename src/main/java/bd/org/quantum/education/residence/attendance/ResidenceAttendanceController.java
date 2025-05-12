package bd.org.quantum.education.residence.attendance;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.academic.reference.AcademicReferenceService;
import bd.org.quantum.education.common.*;
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
public class ResidenceAttendanceController {
    private final ResidenceAttendanceService service;
    private final AcademicReferenceService academicReferenceService;
    private final MessageSource messageSource;

    public ResidenceAttendanceController(ResidenceAttendanceService service,
                                         AcademicReferenceService academicReferenceService,
                                         MessageSource messageSource) {
        this.service = service;
        this.academicReferenceService = academicReferenceService;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.RESIDENCE_ATTENDANCE_SHEET_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_ATTENDANCE_CREATE"})
    public String attendanceEntry(Model model) {
        model.addAttribute("attendanceSheet", new ResidenceAttendance());
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("attendanceTypes", AttendanceType.getResidenceAttendanceType());
        return "residence/attendance/attendance-sheet-entry";
    }

    @PostMapping(Routes.RESIDENCE_ATTENDANCE_SHEET_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_ATTENDANCE_CREATE"})
    public String attendanceEntry(@Valid ResidenceAttendance residenceAttendance, BindingResult result, Model model) {
        service.validateAttendance(residenceAttendance, result);
        try {
            if (!result.hasErrors()) {
                residenceAttendance = service.save(residenceAttendance);
                return "redirect:" + Routes.RESIDENCE_ATTENDANCE_ENTRY + "?id=" + residenceAttendance.getId();
            } else {
                SubmitResult.error(messageSource, "residence.attendance.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "residence.attendance.create.error", model);
        }
        model.addAttribute("attendanceSheet", residenceAttendance);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("attendanceTypes", AttendanceType.getResidenceAttendanceType());
        return "residence/attendance/attendance-sheet-entry";
    }

    @GetMapping(Routes.RESIDENCE_ATTENDANCE_SHEET_LIST)
    @SecurityCheck(permissions = {"RESIDENCE_ATTENDANCE_VIEW"})
    public String attendanceList(Model model) {
        model.addAttribute("searchUrl", Routes.RESIDENCE_ATTENDANCE_SHEET_SEARCH);
        model.addAttribute("entryUrl", Routes.RESIDENCE_ATTENDANCE_ENTRY);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        model.addAttribute("attendanceTypes", AttendanceType.getResidenceAttendanceType());
        return "residence/attendance/attendance-sheet-list";
    }

    @GetMapping(Routes.RESIDENCE_ATTENDANCE_SHEET_SEARCH)
    @ResponseBody
    public Page<ResidenceAttendance> attendanceSearch(ResidenceAttendanceSearchCriteria searchForm) {
        return service.attendanceSearch(searchForm);
    }

    @GetMapping(Routes.RESIDENCE_ATTENDANCE_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_ATTENDANCE_VIEW"})
    public String attendanceStudent(@RequestParam(name = "id") Long id,
                                    @RequestParam(name = "action", defaultValue = "0") Integer action,
                                    @RequestParam(name = "result", required = false) boolean result,
                                    Model model) {
        model.addAttribute("attendanceSheet", service.getAttendanceSheetById(id));
        model.addAttribute("currentQuantaa", StudentStatus.CURRENT.getTitle());
        model.addAttribute("entryUrl", Routes.RESIDENCE_ATTENDANCE_ENTRY);
        if (result && action == 0) {
            SubmitResult.success(messageSource, "residence.attendance.save.success", model);
        }
        if (result && action == 1) {
            SubmitResult.success(messageSource, "residence.attendance.update.success", model);
        }
        if (result && action == 2) {
            SubmitResult.success(messageSource, "residence.attendance.submit.success", model);
        }
        return "residence/attendance/attendance-entry";
    }

    @PostMapping(Routes.RESIDENCE_ATTENDANCE_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_ATTENDANCE_CREATE"})
    public String attendanceStudentSave(@Valid ResidenceAttendance residenceAttendance, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                ResidenceAttendance attendance = service.getAttendanceSheetById(residenceAttendance.getId());
                attendance.setStatusList(residenceAttendance.getStatusList());
                if (residenceAttendance.getAction() == 0 || residenceAttendance.getAction() == 1) {
                    attendance.setStatus(1);
                } else {
                    attendance.setStatus(2);
                }
                service.save(attendance);
                return "redirect:" + Routes.RESIDENCE_ATTENDANCE_ENTRY + "?id=" + residenceAttendance.getId() + "&action=" + residenceAttendance.getAction() + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "residence.attendance.save.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "residence.attendance.save.error", model);
        }
        model.addAttribute("attendanceSheet", service.getAttendanceSheetById(residenceAttendance.getId()));
        model.addAttribute("currentQuantaa", StudentStatus.CURRENT.getTitle());
        model.addAttribute("entryUrl", Routes.RESIDENCE_ATTENDANCE_ENTRY);
        return "residence/attendance/attendance-entry";
    }
}