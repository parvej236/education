package bd.org.quantum.education.admission.applicant;

import bd.org.quantum.common.utils.SearchForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantSearchCriteria extends SearchForm {
    private String gender;
    private String formNo;
    private String session;
    private String appliedClass;
    private String examCenter;
    private String religion;
    private String community;
    private String district;
    private String upazila;
    private Boolean selected;
    private Boolean admitted;
}
