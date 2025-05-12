package bd.org.quantum.education.academic.attendance;

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
public class AcademicAttendanceController {
    private final AcademicAttendanceService service;
    private final AcademicReferenceService academicReferenceService;
    private final MessageSource messageSource;

    public AcademicAttendanceController(AcademicAttendanceService service,
                                        AcademicReferenceService academicReferenceService,
                                        MessageSource messageSource) {
        this.service = service;
        this.academicReferenceService = academicReferenceService;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.ACADEMIC_ATTENDANCE_SHEET_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_ATTENDANCE_CREATE"})
    public String academicAttendanceSheetEntry(Model model) {
        model.addAttribute("attendanceSheet", new AcademicAttendance());
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("attendanceTypes", AttendanceType.getAcademicAttendanceType());
        return "academic/attendance/attendance-sheet-entry";
    }

    @PostMapping(Routes.ACADEMIC_ATTENDANCE_SHEET_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_ATTENDANCE_CREATE"})
    public String academicAttendanceSheetEntry(@Valid AcademicAttendance academicAttendance, BindingResult result, Model model) {
        service.validationAttendanceSheet(academicAttendance, result);
        try {
            if (!result.hasErrors()) {
                academicAttendance = service.save(academicAttendance);
                return "redirect:" + Routes.ACADEMIC_ATTENDANCE_ENTRY + "?id=" + academicAttendance.getId();
            } else {
                SubmitResult.error(messageSource, "academic.attendance.sheet.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "academic.attendance.sheet.create.error", model);
        }
        model.addAttribute("attendanceSheet", new AcademicAttendance());
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("attendanceTypes", AttendanceType.getAcademicAttendanceType());
        return "academic/attendance/attendance-sheet-entry";
    }

    @GetMapping(Routes.ACADEMIC_ATTENDANCE_SHEET_LIST)
    @SecurityCheck(permissions = {"ACADEMIC_ATTENDANCE_VIEW"})
    public String academicAttendanceList(Model model) {
        model.addAttribute("searchUrl", Routes.ACADEMIC_ATTENDANCE_SHEET_SEARCH);
        model.addAttribute("entryUrl", Routes.ACADEMIC_ATTENDANCE_ENTRY);
        model.addAttribute("institutionList", academicReferenceService.getAllInstitution());
        model.addAttribute("classList", Routes.ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION);
        model.addAttribute("groupList", Group.getAll());
        model.addAttribute("sectionList", Section.values());
        model.addAttribute("attendanceTypes", AttendanceType.getAcademicAttendanceType());
        return "academic/attendance/attendance-sheet-list";
    }

    @GetMapping(Routes.ACADEMIC_ATTENDANCE_SHEET_SEARCH)
    @ResponseBody
    public Page<AcademicAttendance> academicAttendanceList(AcademicAttendanceSearchCriteria searchForm) {
        return service.getAcademicAttendanceList(searchForm);
    }

    @GetMapping(Routes.ACADEMIC_ATTENDANCE_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_ATTENDANCE_VIEW"})
    public String academicAttendanceEntry(@RequestParam(name = "id") Long id,
                                          @RequestParam(name = "action", defaultValue = "0") Integer action,
                                          @RequestParam(name = "result", required = false) boolean result,
                                          Model model) {
        model.addAttribute("attendanceSheet", service.getAttendanceSheetById(id));
        model.addAttribute("currentQuantaa", StudentStatus.CURRENT.getTitle());
        model.addAttribute("entryUrl", Routes.ACADEMIC_ATTENDANCE_ENTRY);
        if (result && action == 0) {
            SubmitResult.success(messageSource, "academic.attendance.save.success", model);
        }
        if (result && action == 1) {
            SubmitResult.success(messageSource, "academic.attendance.update.success", model);
        }
        if (result && action == 2) {
            SubmitResult.success(messageSource, "academic.attendance.submit.success", model);
        }
        return "academic/attendance/attendance-entry";
    }

    @PostMapping(Routes.ACADEMIC_ATTENDANCE_ENTRY)
    @SecurityCheck(permissions = {"ACADEMIC_ATTENDANCE_CREATE"})
    public String academicAttendanceEntry(@Valid AcademicAttendance academicAttendance, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                AcademicAttendance attendance = service.getAttendanceSheetById(academicAttendance.getId());
                attendance.setStatusList(academicAttendance.getStatusList());
                if (academicAttendance.getAction() == 0 || academicAttendance.getAction() == 1) {
                    attendance.setStatus(1);
                } else {
                    attendance.setStatus(2);
                }
                service.save(attendance);
                return "redirect:" + Routes.ACADEMIC_ATTENDANCE_ENTRY + "?id=" + academicAttendance.getId() + "&action=" + academicAttendance.getAction() + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "academic.attendance.save.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "academic.attendance.save.error", model);
        }
        model.addAttribute("attendanceSheet", service.getAttendanceSheetById(academicAttendance.getId()));
        model.addAttribute("currentQuantaa", StudentStatus.CURRENT.getTitle());
        model.addAttribute("entryUrl", Routes.ACADEMIC_ATTENDANCE_ENTRY);
        return "academic/attendance/attendance-entry";
    }
}
