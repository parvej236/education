package bd.org.quantum.education.admission.applicant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantReportCriteria {
    private String reportType;
    private String gender;
    private String session;
    private String cls;
    private String religion;
    private String community;
    private String examCenter;
    private String district;
    private String upazila;
    private String union;
    private Boolean isSelected;
    private Boolean isAdmitted;
    private String reportFormat;
}
