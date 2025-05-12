package bd.org.quantum.education.admission.guardian;

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
public class GuardianService {
    private final GuardianRepository repository;

    public GuardianService(GuardianRepository repository) {
        this.repository = repository;
    }

    public void validateGuardian(@Valid Guardian guardian, BindingResult result) {
        if (guardian.getStudent().getId() == null) {
            result.rejectValue("student", "error.required");
        }
        if (guardian.getNameEn().trim().isEmpty()) {
            result.rejectValue("nameEn", "error.required");
        }
        if (guardian.getNameBn().trim().isEmpty()) {
            result.rejectValue("nameBn", "error.required");
        }
        if (guardian.getAction().equals("update") && guardian.getRelation().equals("0")) {
            result.rejectValue("relation", "error.required");
        }
        if (guardian.getPrimaryPhone().trim().isEmpty()) {
            result.rejectValue("primaryPhone", "error.required");
        }
        if (guardian.getIndentityNumber().trim().isEmpty()) {
            result.rejectValue("indentityNumber", "error.required");
        }
    }

    public Guardian saveGuardian(@Valid Guardian guardian) {
        return repository.save(guardian);
    }

    public Guardian getGuardianById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<Guardian> guardianSearch(SearchForm form) {
        Specification<Guardian> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null : GuardianSpecification.omniText(form.getOmniText()));

        Page<Guardian> guardianPages;

        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<Guardian> list = repository.findAll(specification, pageable.getSort());
            guardianPages = new PageImpl<>(list);
        } else {
            guardianPages = repository.findAll(specification, pageable);
        }

        return guardianPages;
    }

    public void updateImagePath(String path, Long id) {
        repository.updateImagePath(path, id);
    }

    public List<Guardian> getGuardianListByStudentId(Long id) {
        return repository.findAllByStudentId(id);
    }
}
