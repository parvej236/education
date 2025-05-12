package bd.org.quantum.education.academic.student;

import bd.org.quantum.common.utils.SearchForm;
import bd.org.quantum.education.admission.student.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class AcademicStudentService {

    private final StudentShiftRepository studentShiftRepository;

    public AcademicStudentService(StudentShiftRepository studentShiftRepository) {
        this.studentShiftRepository = studentShiftRepository;
    }

    public StudentShift save(StudentShift studentShift) {
        return studentShiftRepository.save(studentShift);
    }

    public StudentShift getStudentShiftById(Long id) {
        return studentShiftRepository.findById(id).orElse(null);
    }

    public Page<StudentShift> shiftLogList(SearchForm searchForm) {
        Specification<StudentShift> specification = Specification
                .where(StudentShiftSpecification.omniText(searchForm.getOmniText()));

        Page<StudentShift> studentShiftPage;

        Pageable pageable = PageRequest.of(
                searchForm.getPage(),
                searchForm.getPageSize(),
                Sort.by(Sort.Direction.fromString(searchForm.getSortDirection()), searchForm.getSortBy())
        );

        if (searchForm.isUnpaged()) {
            List<StudentShift> list = studentShiftRepository.findAll(specification, pageable.getSort());
            studentShiftPage = new PageImpl<>(list);
        } else {
            studentShiftPage = studentShiftRepository.findAll(specification, pageable);
        }
        return studentShiftPage;
    }

    public void validationStudentAssignToClass(@Valid Student student, BindingResult result) {
        if (student.getCurrentAcademicClassId() == null || student.getCurrentAcademicClassId() == 0) {
            result.rejectValue("currentAcademicClassId", "error.required");
        }
    }
    public void validationShiftStudent (StudentShift studentShift, BindingResult result){
        if(studentShift.getShiftToInstitution().isEmpty()){
            result.rejectValue("shiftToInstitution", "error.required");
        }
        if(studentShift.getShiftToClassInfo().isEmpty()){
            result.rejectValue("shiftToClassInfo", "error.required");
        }
        if(studentShift.getCause().trim().isEmpty()){
            result.rejectValue("cause", "error.required");
        }
    }
}