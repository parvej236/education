package bd.org.quantum.education.admission.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentReportCriteria {
    private String reportType;
    private Integer session;
    private String gender;
    private Long institution;
    private Long cls;
    private Long residence;
    private String religion;
    private String community;
    private String district;
    private String upazila;
    private String union;
    private String status;
    private String reportFormat;
}
