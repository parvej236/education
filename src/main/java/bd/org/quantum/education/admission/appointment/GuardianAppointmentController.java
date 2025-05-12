package bd.org.quantum.education.admission.appointment;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.admission.guardian.Guardian;
import bd.org.quantum.education.admission.guardian.GuardianService;
import bd.org.quantum.education.admission.student.StudentService;
import bd.org.quantum.education.common.Routes;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class GuardianAppointmentController {
    private final GuardianAppointmentService service;
    private final GuardianService guardianService;
    private final StudentService  studentService;
    private final MessageSource messageSource;

    public GuardianAppointmentController(GuardianAppointmentService service,
                                         GuardianService guardianService,
                                         StudentService studentService,
                                         MessageSource messageSource) {
        this.service = service;
        this.guardianService = guardianService;
        this.studentService = studentService;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.GUARDIAN_APPOINTMENT_ENTRY)
    @SecurityCheck(permissions = {"ADMISSION_APPOINTMENT_VIEW"})
    public String guardianAppointmentEntry(@RequestParam(name = "id", defaultValue = "0") Long id,
                                           @RequestParam(name = "action", required = false) String action,
                                           @RequestParam(name = "result", required = false) boolean result,
                                           Model model) {
        model.addAttribute("guardianAppointment", (id > 0) ? service.getGuardianAppointmentById(id) : new GuardianAppointment());
        model.addAttribute("guardianListUrl", Routes.GET_GUARDIAN_LIST_BY_STUDENT_ID);
        model.addAttribute("entryUrl", Routes.GUARDIAN_APPOINTMENT_ENTRY);
        model.addAttribute("applicantUrl", Routes.CREATE_APPLICANT);

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "guardian.appointment.create.success", model);
        }

        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "guardian.appointment.update.success", model);
        }
        return "admission/appointment/guardian-appointment-entry";
    }

    @PostMapping(Routes.GUARDIAN_APPOINTMENT_ENTRY)
    @SecurityCheck(permissions = {"ADMISSION_APPOINTMENT_CREATE"})
    public String guardianAppointmentEntry(@Valid GuardianAppointment guardianAppointment, BindingResult result, Model model) {
        service.validateAppointment(guardianAppointment, result);
        String action = guardianAppointment.getAction();
        try {
            if (!result.hasErrors()) {
                List<Guardian> guardianList = new ArrayList<>();
                for (Guardian guardian : guardianAppointment.getGuardians()) {
                    guardianList.add(guardianService.getGuardianById(guardian.getId()));
                }
                guardianAppointment.setGuardians(guardianList);
                GuardianAppointment appointment = service.saveAppointment(guardianAppointment);
                return "redirect:" + Routes.GUARDIAN_APPOINTMENT_ENTRY + "?id=" + appointment.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                if (action.equals("new")) {
                    SubmitResult.error(messageSource, "guardian.appointment.create.error", model);
                } else {
                    SubmitResult.error(messageSource, "guardian.appointment.update.error", model);
                }
            }
        } catch (Exception e) {
            if (action.equals("new")) {
                SubmitResult.error(messageSource, "guardian.appointment.create.error", model);
            } else {
                SubmitResult.error(messageSource, "guardian.appointment.update.error", model);
            }
        }
        guardianAppointment.setStudent(studentService.getStudentById(guardianAppointment.getStudent().getId()));
        model.addAttribute("guardianAppointment", guardianAppointment);
        model.addAttribute("entryUrl", Routes.GUARDIAN_APPOINTMENT_ENTRY);
        model.addAttribute("guardianListUrl", Routes.GET_GUARDIAN_LIST_BY_STUDENT_ID);
        model.addAttribute("applicantUrl", Routes.CREATE_APPLICANT);
        return "admission/appointment/guardian-appointment-entry";
    }

    @GetMapping(Routes.GUARDIAN_APPOINTMENT_LIST)
    @SecurityCheck(permissions = {"ADMISSION_APPOINTMENT_VIEW"})
    public String guardianAppointmentList(Model model) {
        model.addAttribute("searchUrl", Routes.GUARDIAN_APPOINTMENT_SEARCH);
        model.addAttribute("entryUrl", Routes.GUARDIAN_APPOINTMENT_ENTRY);
        return "admission/appointment/guardian-appointment-list";
    }

    @GetMapping(Routes.GUARDIAN_APPOINTMENT_SEARCH)
    @ResponseBody
    public Page<GuardianAppointment> guardianAppointmentSearch(SearchForm form) {
        return service.guardianAppointmentSearch(form);
    }
}
