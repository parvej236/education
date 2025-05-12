package bd.org.quantum.education.admission.appointment;

import bd.org.quantum.common.utils.SearchForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class GuardianAppointmentService {
    private final GuardianAppointmentRepository repository;

    public GuardianAppointmentService(GuardianAppointmentRepository repository) {
        this.repository = repository;
    }

    public GuardianAppointment saveAppointment(@Valid GuardianAppointment guardianAppointment) {
        return repository.save(guardianAppointment);
    }

    public void validateAppointment(@Valid GuardianAppointment guardianAppointment, BindingResult result) {

        if (guardianAppointment.getAppointmentOn() == null) {
            result.rejectValue("appointmentOn", "error.required");
        }

        if (guardianAppointment.getNumberOfGuaridian() == null || guardianAppointment.getNumberOfGuaridian() < 0) {
            result.rejectValue("numberOfGuaridian", "error.required");
        }

        if (guardianAppointment.getReason().equals("0")) {
            result.rejectValue("reason", "error.required");
        }

        if (guardianAppointment.getStudent().getId() == null) {
            result.rejectValue("student", "error.required");
            return;
        }

        if (guardianAppointment.getGuardians() == null) {
            result.rejectValue("guardians", "guardian.error.required");
        }
    }

    public Page<GuardianAppointment> guardianAppointmentSearch(SearchForm form) {
        Specification<GuardianAppointment> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null : GuardianAppointmentSpecification.omniText(form.getOmniText()));

        Page<GuardianAppointment> guardianAppointmentPages;

        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<GuardianAppointment> list = repository.findAll(specification, pageable.getSort());
            guardianAppointmentPages = new PageImpl<>(list);
        } else {
            guardianAppointmentPages = repository.findAll(specification, pageable);
        }

        return guardianAppointmentPages;
    }

    public GuardianAppointment getGuardianAppointmentById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
