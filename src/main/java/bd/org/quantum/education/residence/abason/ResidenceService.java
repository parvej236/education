package bd.org.quantum.education.residence.abason;

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
public class ResidenceService {
    private final ResidenceRepository residenceRepository;

    public ResidenceService(ResidenceRepository residenceRepository) {
        this.residenceRepository = residenceRepository;
    }

    public Residence createResidence(Residence residence) {
        return residenceRepository.save(residence);
    }

    public Iterable<Residence> getAllResidence() {
        return residenceRepository.findAll();
    }

    public Residence getResidenceById(Long id) {
        return residenceRepository.getById(id);
    }

    public Page<Residence> residenceSearch(SearchForm form) {
        Specification<Residence> specification = Specification
                .where(StringUtils.isEmpty(form.getOmniText()) ? null : ResidenceSpecification.omniText(form.getOmniText()));

        Page<Residence> residenceList;

        Pageable pageable = PageRequest.of(
                form.getPage(),
                form.getPageSize(),
                Sort.by(Sort.Direction.fromString(form.getSortDirection()), form.getSortBy())
        );

        if (form.isUnpaged()) {
            List<Residence> list = residenceRepository.findAll(specification, pageable.getSort());
            residenceList = new PageImpl<>(list);
        } else {
            residenceList = residenceRepository.findAll(specification, pageable);
        }

        return residenceList;
    }

    public void validateResidence(Residence residence, BindingResult result) {
        boolean isRequired = false;

        if (residence.getName().trim().isEmpty()) {
            result.rejectValue("name", "error.required");
            residence.setName("");
        }

        if (residence.getCode().trim().isEmpty()) {
            result.rejectValue("code", "error.required");
            residence.setCode("");
        }

        for (ResidenceClass residenceClass : residence.getClassList()) {
            if (residenceClass.getClazz().getId() == null) {
                result.rejectValue("classList", "error.required");
                isRequired = true;
                break;
            }
        }

        if (!isRequired) {
            boolean isBreak = false;
            for (int i = 0; i < residence.getClassList().size(); i++) {
                for (int j = i + 1; j < residence.getClassList().size(); j++) {
                    if (residence.getClassList().get(i).getClazz().getId().equals(residence.getClassList().get(j).getClazz().getId())) {
                        result.rejectValue("classList", "error.duplicate");
                        isBreak = true;
                        break;
                    }
                }
                if (isBreak) {
                    break;
                }
            }
        }

        if (!residence.getName().isEmpty() && !residence.getCode().isEmpty() && isExistsResidence(residence)) {
            result.rejectValue("name", "error.exists");
        }
    }

    public boolean isExistsResidence(Residence res) {
        Iterable<Residence> residenceList = getAllResidence();
        for (Residence residence : residenceList) {
            if (!(Objects.equals(res.getId(), residence.getId()))) {
                if (res.getName().equalsIgnoreCase(residence.getName()) || res.getCode().equalsIgnoreCase(residence.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }
}
