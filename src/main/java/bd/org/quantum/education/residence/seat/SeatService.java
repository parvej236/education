package bd.org.quantum.education.residence.seat;

import bd.org.quantum.common.utils.SearchForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Slf4j
@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat save(Seat seat){
        return seatRepository.save(seat);
    }

    public Seat getById(Long id) {
        return seatRepository.findById(id).orElse(null);
    }

    public Page<Seat> seatSearch(SearchForm searchForm) {
        Specification<Seat> specification = Specification.where(SeatSpecification.omniText(searchForm.getOmniText()))
                .and(SeatSpecification.statusIsFree());

        Page<Seat> seatList;
        Pageable pageable = PageRequest.of(
                searchForm.getPage(),
                searchForm.getPageSize(),
                Sort.by(Sort.Direction.fromString(searchForm.getSortDirection()), searchForm.getSortBy())
        );

        if (searchForm.isUnpaged()) {
            List<Seat> list = seatRepository.findAll(specification, pageable.getSort());
            seatList = new PageImpl<>(list);
        } else {
            seatList = seatRepository.findAll(specification, pageable);
        }
        return seatList;
    }

    public void validation(SeatBookingLog seatBookingLog, BindingResult result) {
        if(seatBookingLog.getReleaseDate().isEmpty()){
            result.rejectValue("releaseDate", "error.required");
        }
    }
}
