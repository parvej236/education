package bd.org.quantum.education.admission.student;

import bd.org.quantum.education.admission.applicant.Applicant;
import bd.org.quantum.education.admission.document.Document;
import bd.org.quantum.education.common.AuditData;
import bd.org.quantum.education.common.Routes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adm_student")
public class Student extends AuditData {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant", unique = true, nullable = false)
    private Applicant applicant;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "adm_student_document",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private List<Document> documents;

    private String studentId;
    private String status;

    private Long institutionId;
    private String institutionName;

    private Long residenceId;
    private String residenceName;

    private Long currentAcademicClassId;
    private String currentAcademicClassInfo;

    @Transient
    private String action;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String studentInfo;

    public String getStudentInfo() {
        if (applicant == null) {
            return "";
        }

        String url = Routes.CREATE_APPLICANT + "?id=" + applicant.getId();
        String studentInfo = "";

        if (applicant.getNameEn() != null && applicant.getNameBn() != null) {
            studentInfo += "<a style='text-decoration: none' href='" + url + "' target='_blank'>" + applicant.getNameEn() + " (" + applicant.getNameBn() + ")<a><br>";
        } else if (applicant.getNameEn() != null && applicant.getNameBn() == null) {
            studentInfo += "<a style='text-decoration: none' href='" + url + "' target='_blank'>" + applicant.getNameEn() + "<a><br>";
        } else if (applicant.getNameEn() == null && applicant.getNameBn() != null) {
            studentInfo += "<a style='text-decoration: none' href='" + url + "' target='_blank'>" + applicant.getNameBn() + "<a><br>";
        }

        if (studentId != null) {
            studentInfo += studentId + "<br>";
        }

        if (applicant.getGender() != null) {
            studentInfo += applicant.getGender() + "<br>";
        }

        if (applicant.getReligion() != null && applicant.getCommunity() != null) {
            studentInfo += applicant.getReligion() + " (" + applicant.getCommunity() + ")<br>";
        } else if (applicant.getReligion() != null && applicant.getCommunity() == null) {
            studentInfo += applicant.getReligion() + "<br>";
        } else if (applicant.getReligion() == null && applicant.getCommunity() != null) {
            studentInfo += applicant.getCommunity() + "<br>";
        }

        if (applicant.getBirthDate() != null) {
            studentInfo += applicant.getBirthDate() + " (" + applicant.getAge() + ")<br>";
        }

        if (currentAcademicClassInfo != null) {
            studentInfo += currentAcademicClassInfo + "<br>";
        }

        if (institutionName != null && residenceName != null) {
            studentInfo += institutionName + ", " + residenceName + "<br>";
        } else if (institutionName != null && residenceName == null) {
            studentInfo += institutionName + "<br>";
        } else if (institutionName == null && residenceName != null) {
            studentInfo += residenceName + "<br>";
        }

        if (applicant.getApplyDate() != null) {
            studentInfo += "Since: " + applicant.getApplyDate();
        }

        return studentInfo;
    }
}
