package bd.org.quantum.education.residence.hall;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.residence.abason.ResidenceService;
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
public class HallController {
    private final HallService hallService;
    private final ResidenceService residenceSerive;
    private final MessageSource messageSource;

    public HallController(HallService hallService, ResidenceService residenceSerive, MessageSource messageSource) {
        this.hallService = hallService;
        this.residenceSerive = residenceSerive;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.RESIDENCE_HALL_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_VIEW"})
    public String hallEntry(@RequestParam(name = "id", defaultValue = "0") Long id,
                            @RequestParam(name = "result", required = false) boolean result,
                            @RequestParam(name = "action", required = false) String action,
                            Model model) {

        model.addAttribute("hall", (id > 0) ? hallService.getHallById(id) : new Hall());
        model.addAttribute("entryLink", Routes.RESIDENCE_HALL_ENTRY);
        model.addAttribute("residenceList", residenceSerive.getAllResidence());

        if (result && action.equals("new")) {
            SubmitResult.success(messageSource, "hall.create.success", model);
        }
        if (result && action.equals("update")) {
            SubmitResult.success(messageSource, "hall.update.success", model);
        }
        return "residence/hall/hall-entry";
    }

    @PostMapping(Routes.RESIDENCE_HALL_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_CREATE"})
    public String hallEntry(@Valid Hall hall, BindingResult result, Model model) {
        hallService.validateHall(hall, result);
        String action = hall.getAction();

        try {
            if (!result.hasErrors()) {
                hall = hallService.createHall(hall);
                model.addAttribute("hall", hall);
                return "redirect:" + Routes.RESIDENCE_HALL_ENTRY + "?id=" + hall.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "hall.create.error", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "hall.create.error", model);
        }

        model.addAttribute("entryLink", Routes.RESIDENCE_HALL_ENTRY);
        model.addAttribute("residenceList", residenceSerive.getAllResidence());

        return "residence/hall/hall-entry";
    }

    @GetMapping(Routes.RESIDENCE_HALL_LIST)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_VIEW"})
    public String hallList(Model model) {
        model.addAttribute("searchUrl", Routes.RESIDENCE_HALL_SEARCH);
        model.addAttribute("entryUrl", Routes.RESIDENCE_HALL_ENTRY);
        return "residence/hall/hall-list";
    }

    @GetMapping(Routes.RESIDENCE_HALL_SEARCH)
    @ResponseBody
    public Page<Hall> hallSearch(SearchForm form) {
        return hallService.hallSearch(form);
    }
}
