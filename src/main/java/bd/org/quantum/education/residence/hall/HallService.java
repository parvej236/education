package bd.org.quantum.education.residence.hall;

import bd.org.quantum.common.utils.SearchForm;
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
public class HallService {
    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public Hall createHall(Hall hall) {
        return hallRepository.save(hall);
    }

    public Hall getHallById(Long id) {
        return hallRepository.getById(id);
    }

    public Page<Hall> hallSearch(SearchForm form) {
        Specification<Hall> specification = Specification.where(StringUtils.isEmpty(form.getOmniText()) ? null : HallSpecification.omniText(form.getOmniText()));

        Page<Hall> hallList;

        Pageable pageable = PageRequest.of(form.getPage(), form.getPageSize(), Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy()));

        if (form.isUnpaged()) {
            List<Hall> list = hallRepository.findAll(specification, pageable.getSort());
            hallList = new PageImpl<>(list);
        } else {
            hallList = hallRepository.findAll(specification, pageable);
        }
        return hallList;
    }

    public Iterable<Hall> getAllHall() {
        return hallRepository.findAll();
    }

    public void validateHall(Hall hall, BindingResult result) {
        if (hall.getName().isEmpty()) {
            result.rejectValue("name", "error.required");
            hall.setName("");
        }

        if (hall.getCode().isEmpty()) {
            result.rejectValue("code", "error.required");
        }

        if (hall.getResidence().getId() == null) {
            result.rejectValue("residence.id", "error.required");
        }

        if (!hall.getName().isEmpty() && !hall.getCode().isEmpty() && isExistsHall(hall)) {
            result.rejectValue("name", "error.exists");
        }
    }

    public boolean isExistsHall(Hall ha) {
        Iterable<Hall> hallList = getAllHall();
        for (Hall hall : hallList) {
            if (!(Objects.equals(ha.getId(), hall.getId()))) {
                if (ha.getResidence().getId().equals(hall.getResidence().getId())) {
                    if (ha.getName().equalsIgnoreCase(hall.getName()) || ha.getCode().equalsIgnoreCase(hall.getCode())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
