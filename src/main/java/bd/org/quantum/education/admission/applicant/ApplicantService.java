package bd.org.quantum.education.admission.applicant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@Slf4j
public class ApplicantService {
    private final ApplicantRepository repository;

    public ApplicantService(ApplicantRepository repository) {
        this.repository = repository;
    }

    public Applicant get(Long id) {
        return repository.getById(id);
    }

    public Applicant getApplicantById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Applicant create(Applicant applicant) {
        return this.repository.save(applicant);
    }

    public void validateApplicant(Applicant applicant, BindingResult result) {

        if (applicant.getFormNo().trim().isEmpty()) {
            result.rejectValue("formNo", "error.required");
            applicant.setFormNo("");
        }
        if (applicant.getAppliedClass().equals(" ")) {
            result.rejectValue("appliedClass", "error.required");
        }

        if (applicant.getExamCenter().equals(" ")) {
            result.rejectValue("examCenter", "error.required");
        }
        if (applicant.getNameEn().trim().isEmpty()) {
            result.rejectValue("nameEn", "error.required");
            applicant.setNameEn("");
        }
        if (applicant.getNameBn().trim().isEmpty()) {
            result.rejectValue("nameBn", "error.required");
            applicant.setNameBn("");
        }
        if(applicant.getId() != null) {
            if(repository.existsByFormNoAndIdNot(applicant.getFormNo(), applicant.getId())) {
                result.rejectValue("formNo", "error.exists");
            }
        }
        if(applicant.getId() == null) {
            if(repository.existsByFormNo(applicant.getFormNo())) {
                result.rejectValue("formNo", "error.exists");
            }
        }
    }

    public Applicant update(Long id, Applicant applicantFrom) {
        Applicant applicant = repository.getById(id);
        BeanUtils.copyProperties(applicantFrom, applicant);
        return this.repository.save(applicant);
    }

    public Page<Applicant> search(ApplicantSearchCriteria searchForm) {

        Specification<Applicant> specification = ApplicantSpecification.all()
                .and(ApplicantSpecification.omniText(searchForm))
                .and(ApplicantSpecification.equalGender(searchForm.getGender()))
                .and(ApplicantSpecification.equalSession(searchForm.getSession()))
                .and(ApplicantSpecification.equalAppliedClass(searchForm.getAppliedClass()))
                .and(ApplicantSpecification.equalExamCenter(searchForm.getExamCenter()))
                .and(ApplicantSpecification.equalReligion(searchForm.getReligion()))
                .and(ApplicantSpecification.equalCommunity(searchForm.getCommunity()))
                .and(ApplicantSpecification.equalDistrict(searchForm.getDistrict()))
                .and(ApplicantSpecification.equalUpazila(searchForm.getUpazila()))
                .and(ApplicantSpecification.selection(searchForm.getSelected()))
                .and(ApplicantSpecification.admitted(searchForm.getAdmitted()));

        Page<Applicant> applicants;
        Pageable pageable = PageRequest.of(searchForm.getPage(), searchForm.getPageSize(),
                    Sort.by(Sort.Direction.fromString(searchForm.getSortDirection()),
                            searchForm.getSortBy()));

        if (searchForm.isUnpaged()) {
            List<Applicant> list = repository.findAll(specification, pageable.getSort());
            applicants = new PageImpl<>(list);
        } else {
            applicants = repository.findAll(specification, pageable);
        }
        return applicants;
    }

    public void updateImagePath(String path, Long id) {
        repository.updateImagePath(path, id);
    }
}