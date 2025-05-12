package bd.org.quantum.education.residence.seat;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.admission.student.Student;
import bd.org.quantum.education.admission.student.StudentService;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.residence.abason.Residence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
public class SeatController {
    private final SeatService seatService;
    private final SeatBookingLogService seatBookingLogService;
    private final MessageSource messageSource;
    private final StudentService studentService;

    public SeatController(SeatService seatService,
                          SeatBookingLogService seatBookingLogService,
                          MessageSource messageSource,
                          StudentService studentService) {
        this.seatService = seatService;
        this.seatBookingLogService = seatBookingLogService;
        this.messageSource = messageSource;
        this.studentService = studentService;
    }

    @GetMapping(Routes.RESIDENCE_SEAT_LIST)
    @SecurityCheck(permissions = {"RESIDENCE_SEAT_VIEW"})
    public String seatList(@RequestParam(name = "result", required = false) boolean result, Model model) {
        model.addAttribute("searchUrl", Routes.RESIDENCE_SEAT_SEARCH);
        model.addAttribute("assignUrl", Routes.RESIDENCE_SEAT_ASSIGN);

        if (result) {
            SubmitResult.success(messageSource, "seat.assign.success", model);
        }
        return "residence/seat/seat-list";
    }

    @GetMapping(Routes.RESIDENCE_SEAT_SEARCH)
    @ResponseBody
    public Page<Seat> search(SearchForm searchForm) {
        return seatService.seatSearch(searchForm);
    }

    @GetMapping(Routes.RESIDENCE_SEAT_ASSIGN)
    @SecurityCheck(permissions = {"RESIDENCE_SEAT_VIEW"})
    public String assignSeat(@RequestParam(name = "id") Long id, Model model) {
        Seat seat = seatService.getById(id);
        SeatBookingLog seatBookingLog = new SeatBookingLog();
        seatBookingLog.setSeat(seat);
        model.addAttribute("seatBookingLog", seatBookingLog);
        model.addAttribute("entryUrl", Routes.RESIDENCE_SEAT_ASSIGN);
        model.addAttribute("studentProfile", Routes.RESIDENCE_STUDENT_PROFILE);
        return "residence/seat/assign-seat";
    }

    @PostMapping(Routes.RESIDENCE_SEAT_ASSIGN)
    @SecurityCheck(permissions = {"RESIDENCE_SEAT_ASSIGN"})
    public String assignSeat(@Valid SeatBookingLog seatBookingLog, BindingResult result, Model model) {
        seatBookingLogService.validateAssignSeat(seatBookingLog, result);
        try {
            if (!result.hasErrors()) {
                Student student = studentService.getStudentById(seatBookingLog.getStudentRowId());
                Seat seat = seatService.getById(seatBookingLog.getSeat().getId());
                Residence residence = seat.getRoom().getHall().getResidence();

                student.setResidenceId(residence.getId());
                student.setResidenceName(residence.getName());
                studentService.create(student);

                seatBookingLog.setSeat(seat);
                seatBookingLog.setStatus("booked");

                seat.setStatus("booked");
                seatService.save(seat);

                seatBookingLogService.save(seatBookingLog);

                return "redirect:" + Routes.RESIDENCE_SEAT_LIST + "?result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "seat.assign.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "seat.assign.error", model);
        }

        model.addAttribute("entryUrl", Routes.RESIDENCE_SEAT_ASSIGN);
        model.addAttribute("seatBookingLog", seatBookingLog);
        model.addAttribute("studentProfile", Routes.RESIDENCE_STUDENT_PROFILE);

        return "residence/seat/assign-seat";
    }

    @GetMapping(Routes.RESIDENCE_SEAT_BOOKING_LIST)
    @SecurityCheck(permissions = {"RESIDENCE_SEAT_VIEW"})
    private String seatBookingList(Model model) {
        model.addAttribute("searchUrl", Routes.RESIDENCE_SEAT_BOOKING_SEARCH + "?status=0");
        model.addAttribute("releaseUrl", Routes.RESIDENCE_SEAT_RELEASE);
        model.addAttribute("buttonTitle", "Release");
        return "residence/seat/seat-booking-list";
    }

    @GetMapping(Routes.RESIDENCE_SEAT_BOOKING_SEARCH)
    @ResponseBody
    public Page<SeatBookingLog> bookingSearch(@RequestParam(name = "status") Integer status, SearchForm searchForm) {
        return seatBookingLogService.seatBookingSearch(searchForm, status);
    }

    @GetMapping(Routes.RESIDENCE_SEAT_RELEASE)
    @SecurityCheck(permissions = {"RESIDENCE_SEAT_VIEW"})
    private String releaseSeat(@RequestParam(name = "id") Long id, Model model) {
        SeatBookingLog seatBookingLog = seatBookingLogService.getById(id);
        model.addAttribute("seatBookingLog", seatBookingLog);
        model.addAttribute("releaseUrl", Routes.RESIDENCE_SEAT_RELEASE);
        return "residence/seat/release-seat";
    }

    @PostMapping(Routes.RESIDENCE_SEAT_RELEASE)
    @SecurityCheck(permissions = {"RESIDENCE_SEAT_RELEASE"})
    private String releaseSeat(@ModelAttribute SeatBookingLog seatBookingLog, BindingResult result, Model model) {
        seatService.validation(seatBookingLog, result);
        try {
            if (!result.hasErrors()) {
                SeatBookingLog s = seatBookingLogService.getById(seatBookingLog.getId());
                s.setReleaseDate(seatBookingLog.getReleaseDate());
                s.setStatus("released");

                Seat seat = seatService.getById(seatBookingLog.getSeat().getId());
                seat.setStatus("free");
                seatService.save(seat);

                seatBookingLogService.save(seatBookingLog);
                return "redirect:" + Routes.RESIDENCE_SEAT_BOOKING_LIST;

            } else {
                SubmitResult.error(messageSource, "seat.release.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "seat.release.error", model);
        }

        return "redirect:" + Routes.RESIDENCE_SEAT_RELEASE + "?id="+ seatBookingLog.getId() + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
    }

    @GetMapping(Routes.RESIDENCE_SEAT_BOOKING_LOG)
    @SecurityCheck(permissions = {"RESIDENCE_SEAT_VIEW"})
    private String bookingLog(Model model) {
        model.addAttribute("searchUrl", Routes.RESIDENCE_SEAT_BOOKING_SEARCH + "?status=1");
        model.addAttribute("releaseUrl", Routes.RESIDENCE_SEAT_RELEASE);
        model.addAttribute("buttonTitle", "Open");
        return "residence/seat/seat-booking-list";
    }
}
