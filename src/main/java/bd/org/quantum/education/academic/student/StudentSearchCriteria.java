package bd.org.quantum.education.academic.student;

import bd.org.quantum.common.utils.SearchForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentSearchCriteria extends SearchForm {
    private Long institution;
    private String clazz;
    private String classGroup;
    private String section;
    private String session;
    private Boolean isInClass;
    private List<Long> accessInstitutions;
    private List<Long> accessResidences;
    private String organ;
    private String examCenter;
    
    private String gender;
    private String community;
    private String religion;
    private String district;
    private String upazila;
}
