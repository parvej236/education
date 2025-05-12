package bd.org.quantum.education.academic.academic_class;

import bd.org.quantum.common.utils.SearchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademicClassSearchCriteria extends SearchForm {
    private Long institution;
    private Long clazz;
    private String classGroup;
    private String section;
    private Integer session;
    private Integer status;
    private Integer releaseStatus;
}
