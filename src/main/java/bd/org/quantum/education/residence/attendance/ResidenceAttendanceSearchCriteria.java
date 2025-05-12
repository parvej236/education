package bd.org.quantum.education.residence.attendance;

import bd.org.quantum.common.utils.SearchForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResidenceAttendanceSearchCriteria extends SearchForm {
    private String date;
    private String attendanceType;
    private Long institution;
    private Long cls;
    private String clsGroup;
    private String section;
    private Integer session;
}
