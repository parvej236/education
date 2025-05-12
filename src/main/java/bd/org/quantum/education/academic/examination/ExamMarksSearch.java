package bd.org.quantum.education.academic.examination;

import bd.org.quantum.common.utils.SearchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamMarksSearch extends SearchForm {
    private Long institution;
    private Long academicClass;
    private Long subject;
    private Long exam;
}
