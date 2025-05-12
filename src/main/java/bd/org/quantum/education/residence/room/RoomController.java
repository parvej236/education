package bd.org.quantum.education.residence.room;

import bd.org.quantum.authorizer.helper.SecurityCheck;
import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.common.utils.SubmitResult;
import bd.org.quantum.education.common.Routes;
import bd.org.quantum.education.residence.hall.HallService;
import bd.org.quantum.education.residence.seat.Seat;
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
import java.util.Comparator;

@Slf4j
@Controller
public class RoomController {
    private final RoomService roomService;
    private final HallService hallService;
    private final MessageSource messageSource;

    public RoomController(RoomService roomService, HallService hallService, MessageSource messageSource) {
        this.roomService = roomService;
        this.hallService = hallService;
        this.messageSource = messageSource;
    }

    @GetMapping(Routes.RESIDENCE_ROOM_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_VIEW"})
    public String RoomEntry(@RequestParam(name = "id", defaultValue = "0") Long id,
                            @RequestParam(name = "result", required = false) boolean result,
                            @RequestParam(name = "action", required = false) String action,
                            Model model){
        Room room = roomService.getRoomById(id);
        if(id > 0) {
            room.getSeats().sort(Comparator.comparing(Seat::getId));
        }
        model.addAttribute("room", (id > 0) ? room : new Room());
        model.addAttribute("entryLink", Routes.RESIDENCE_ROOM_ENTRY);
        model.addAttribute("hallList", hallService.getAllHall());

        if(result && action.equals("new")){
            SubmitResult.success(messageSource, "room.create.success", model);
        }
        if(result && action.equals("update")){
            SubmitResult.success(messageSource, "room.update.success", model);
        }
        return "residence/room/room-entry";
    }

    @PostMapping(Routes.RESIDENCE_ROOM_ENTRY)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_CREATE"})
    public String roomEntry(@Valid Room room, BindingResult result, Model model){
        roomService.validateRoom(room, result);
        String action = room.getAction();

        try {
            if (!result.hasErrors()) {
                room = roomService.createRoom(room);
                model.addAttribute("room", room);
                return "redirect:" + Routes.RESIDENCE_ROOM_ENTRY + "?id=" + room.getId() + "&action=" + action + "&result=" + String.valueOf(!result.hasErrors()).toUpperCase();
            } else {
                SubmitResult.error(messageSource, "room.create.error.select", model);
            }
        } catch (Exception e) {
            SubmitResult.error(messageSource, "room.create.error", model);
        }

        model.addAttribute("entryLink", Routes.RESIDENCE_ROOM_ENTRY);
        model.addAttribute("hallList", hallService.getAllHall());

        return "residence/room/room-entry";
    }

    @GetMapping(Routes.RESIDENCE_ROOM_LIST)
    @SecurityCheck(permissions = {"RESIDENCE_REFERENCE_VIEW"})
    public String roomList(Model model) {
        model.addAttribute("searchUrl", Routes.RESIDENCE_ROOM_SEARCH);
        model.addAttribute("entryUrl", Routes.RESIDENCE_ROOM_ENTRY);
        return "residence/room/room-list";
    }

    @GetMapping(Routes.RESIDENCE_ROOM_SEARCH)
    @ResponseBody
    public Page<Room> roomSearch(SearchForm form) {
        return roomService.roomSearch(form);
    }
}
