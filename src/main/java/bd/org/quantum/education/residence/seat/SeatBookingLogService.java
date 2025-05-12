package bd.org.quantum.education.residence.seat;

import bd.org.quantum.common.utils.SearchForm;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Service
public class SeatBookingLogService {

    private final SeatBookingLogRepository seatBookingLogRepository;

    public SeatBookingLogService(SeatBookingLogRepository seatBookingLogRepository) {
        this.seatBookingLogRepository = seatBookingLogRepository;
    }

    public void save(SeatBookingLog seatBookingLog) {
        seatBookingLogRepository.save(seatBookingLog);
    }

    public SeatBookingLog getById(Long id) {
        return seatBookingLogRepository.findById(id).orElse(null);
    }

    public Iterable<SeatBookingLog> getAll() {
        return seatBookingLogRepository.findAll();
    }

    public Page<SeatBookingLog> seatBookingSearch(SearchForm searchForm, Integer status) {
        Specification<SeatBookingLog> specification;
        if (status == 0) {
            specification = Specification.where(SeatBookingLogSpecification.omniText(searchForm.getOmniText()))
                    .and(SeatBookingLogSpecification.statusIsBook());
        } else {
            specification = Specification.where(SeatBookingLogSpecification.omniText(searchForm.getOmniText()));
        }

        Page<SeatBookingLog> seatBookingList;
        Pageable pageable = PageRequest.of(
                searchForm.getPage(),
                searchForm.getPageSize(),
                Sort.by(Sort.Direction.fromString(searchForm.getSortDirection()), searchForm.getSortBy())
        );

        if (searchForm.isUnpaged()) {
            List<SeatBookingLog> list = seatBookingLogRepository.findAll(specification, pageable.getSort());
            seatBookingList = new PageImpl<>(list);
        } else {
            seatBookingList = seatBookingLogRepository.findAll(specification, pageable);
        }
        return seatBookingList;
    }

    public void validateAssignSeat(@Valid SeatBookingLog seatBookingLog, BindingResult result) {
        if(seatBookingLog.getBookingDate() == null || seatBookingLog.getBookingDate().isBlank()) {
            result.rejectValue("bookingDate", "error.required");
        }

        if(seatBookingLog.getStudentId() == null || seatBookingLog.getStudentId().isBlank()) {
            result.rejectValue("studentId", "error.required");
        }
        if(isExistsSeat(seatBookingLog)){
            result.rejectValue("seat", "error.exists");
        }
    }

    public boolean isExistsSeat(SeatBookingLog stu) {
        Iterable<SeatBookingLog> studentList = getAll();
        for (SeatBookingLog seatBookingLog : studentList) {
            if (!(Objects.equals(stu.getId(), seatBookingLog.getId()))) {
                if(stu.getStudentId().equalsIgnoreCase(seatBookingLog.getStudentId()) && !Objects.equals(seatBookingLog.getStatus(), "released")){
                    return true;
                }
            }
        }
        return false;
    }
}
