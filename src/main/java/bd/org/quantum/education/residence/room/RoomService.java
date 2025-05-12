package bd.org.quantum.education.residence.room;

import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.education.residence.seat.Seat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room getRoomById(Long id) {
        return roomRepository.getById(id);
    }

    public Page<Room> roomSearch(SearchForm form) {
        Specification<Room> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null : RoomSpecification.omniText(form.getOmniText()));

        Page<Room> roomList;
        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<Room> list = roomRepository.findAll(specification, pageable.getSort());
            roomList = new PageImpl<>(list);
        } else {
            roomList = roomRepository.findAll(specification, pageable);
        }
        return roomList;
    }

    public Iterable<Room> getAllRoom() {
        return roomRepository.findAll();
    }

    public Room createRoom(Room room) {
        for (Seat seat : room.getSeats()) {
            seat.setRoom(room);
        }
        return roomRepository.save(room);
    }

    public void validateRoom(Room room, BindingResult result) {
        if (room.getCode().trim().isEmpty()) {
            result.rejectValue("code", "error.required");
            room.setCode("");
        }

        if (room.getDescription().trim().isEmpty()) {
            result.rejectValue("description", "error.required");
            room.setDescription("");
        }

        if (room.getHall().getId() == null) {
            result.rejectValue("hall.id", "error.required");
        }

        if (!room.getCode().isEmpty() && isExistsRoom(room)) {
            result.rejectValue("code", "error.exists");
        }
    }

    public boolean isExistsRoom(Room ro) {
        Iterable<Room> roomList = getAllRoom();
        for (Room room : roomList) {
            if (!(Objects.equals(ro.getId(), room.getId()))) {
                if (ro.getCode().equalsIgnoreCase(room.getCode()) && Objects.equals(ro.getHall().getId(), room.getHall().getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
