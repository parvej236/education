package bd.org.quantum.education.role;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Modules {
    ADMISSION_APPLICANT("Admission Applicant"),
    ADMISSION_STUDENT("Admission Student"),
    ADMISSION_DOCUMENT("Admission Document"),
    ADMISSION_APPOINTMENT("Admission Appointment"),

    ACADEMIC_REFERENCE("Academic Reference"),
    ACADEMIC_CLASS("Academic Class"),
    ACADEMIC_STUDENT("Academic Student"),
    ACADEMIC_EXAMINATION("Academic Examination"),
    ACADEMIC_ATTENDANCE("Academic Attendance"),
    ACADEMIC_DISCIPLINE("Academic Discipline"),

    RESIDENCE_REFERENCE("Residence Reference"),
    RESIDENCE_SEAT("Residence Seat"),
    RESIDENCE_ATTENDANCE("Residence Attendance"),
    RESIDENCE_DISCIPLINE("Residence Discipline");

    @NonNull
    private final String title;
}
