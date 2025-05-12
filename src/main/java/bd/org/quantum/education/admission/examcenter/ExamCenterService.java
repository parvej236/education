package bd.org.quantum.education.admission.examcenter;

import bd.org.quantum.common.utils.SearchForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@Slf4j
public class ExamCenterService {


    private final ExamCenterRepository repository;

    public ExamCenter get(Long id) {
        return repository.getById(id);
    }

    public ExamCenterService(ExamCenterRepository repository) {
        this.repository = repository;
    }

    public boolean existsExamCenterByCode(String examCenterCode, Long examCenterId) {
        if(examCenterId != null) {
            return false;
        }
        return repository.existsByCode(examCenterCode);
    }
    public ExamCenter examCenterEntry(ExamCenter examCenter) {
        return this.repository.save(examCenter);
    }
    public ExamCenter update(Long id, ExamCenter examCenterForm) {
        ExamCenter examCenter = repository.getById(id);
        BeanUtils.copyProperties(examCenterForm, examCenter);
        examCenterForm.setCode(repository.getById(id).getCode());
    return this.repository.save(examCenter);
    }

    public Page<ExamCenter> search(SearchForm searchForm) {

        Specification<ExamCenter> specification = ExamCenterSpecification.all()
                .and(ExamCenterSpecification.omniText(searchForm));

        Page<ExamCenter> examCenters;
        Pageable pageable = PageRequest.of(
                searchForm.getPage(),
                searchForm.getPageSize(),
                Sort.by(Sort.Direction.fromString(searchForm.getSortDirection()), searchForm.getSortBy())
        );

        if (searchForm.isUnpaged()) {
            List<ExamCenter> list = repository.findAll(specification, pageable.getSort());
            examCenters = new PageImpl<>(list);
        } else {
            examCenters = repository.findAll(specification, pageable);
        }
        return examCenters;
    }

    public void validateExamCenter(ExamCenter examCenter, BindingResult result, Long id) {
        if(examCenter.getCode().isEmpty() || existsExamCenterByCode(examCenter.getCode(), id)){
            if(examCenter.getCode().isEmpty()){
                result.rejectValue("code", "error.required");
            }else {
                result.rejectValue("code", "exam.center.code.exists");
            }
        }
        if(examCenter.getName().trim().isEmpty()){
            result.rejectValue("name", "error.required");
        }
        if(examCenter.getDistrict().trim().isEmpty()){
            result.rejectValue("district", "error.required");
        }
        if(examCenter.getId() != null){
            if(repository.existsByCodeAndIdNot(examCenter.getCode(), examCenter.getId())){
                result.rejectValue("code", "error.exists");
            }
        }
    }

    public List<ExamCenter> getAllExamCenters() {
        return repository.findAll();
    }
}
