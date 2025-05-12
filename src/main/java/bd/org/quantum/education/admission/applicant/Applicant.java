package bd.org.quantum.education.admission.applicant;

import bd.org.quantum.common.utils.DateUtils;
import bd.org.quantum.education.common.AuditData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.Period;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "adm_applicant")
public class Applicant extends AuditData {

    private String formNo;
    private String appliedClass;
    private String session;
    private String examCenter;

    @DateTimeFormat(pattern = DateUtils.DD_MM_YYYY)
    private LocalDate applyDate;

    private String nameEn;
    private String nameBn;

    private String gender;
    private String community;
    private String religion;

    @DateTimeFormat(pattern = DateUtils.DD_MM_YYYY)
    private LocalDate birthDate;
    private String birthCertificateNumber;

    private String presentCountry;
    private String presentDistrict;
    private String presentUpazila;
    private String presentUnion;
    private String presentAddress;

    private String permanentCountry;
    private String permanentDistrict;
    private String permanentUpazila;
    private String permanentUnion;
    private String permanentAddress;

    private boolean selected = false;
    private boolean admitted = false;
    private String comment;
    private String imagePath;

    @Transient
    private String action;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String age;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String base64;

    public String getAge() {
        if (this.getBirthDate() != null) {
            Period period = Period.between(this.getBirthDate(), LocalDate.now());
            return period.getYears() + " Years " + period.getMonths() + " Months " + period.getDays() + " Days";
        }
        return "";
    }
}