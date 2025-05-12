package bd.org.quantum.education.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permissions {
    /*Admission*/
    ADMISSION_APPLICANT_VIEW("Can View Applicant"),
    ADMISSION_APPLICANT_CREATE("Can Create Applicant"),
    ADMISSION_APPLICANT_EXAM_CENTER("Can Manage Exam Center"),
    ADMISSION_APPLICANT_REPORT("Can Generate Report"),

    ADMISSION_STUDENT_VIEW("Can View Student"),
    ADMISSION_STUDENT_CREATE("Can Create Student"),
    ADMISSION_STUDENT_GUARDAIN("Can Manage Guardain"),
    ADMISSION_STUDENT_ASSIGN_DOCUMENT("Can Assign Document"),
    ADMISSION_STUDENT_REPORT("Can Generate Report"),

    ADMISSION_DOCUMENT_VIEW("Can View Document"),
    ADMISSION_DOCUMENT_CREATE("Can Create Document"),
    ADMISSION_DOCUMENT_REPORT("Can Generate Report"),

    ADMISSION_APPOINTMENT_VIEW("Can View Appointment"),
    ADMISSION_APPOINTMENT_CREATE("Can Create Appointment"),

    /*Academic*/
    ACADEMIC_REFERENCE_VIEW("Can View Reference"),
    ACADEMIC_REFERENCE_CREATE("Can Create Reference"),
    ACADEMIC_REFERENCE_USER_ACCESS("Can Manage User Access"),

    ACADEMIC_CLASS_VIEW("Can View Class"),
    ACADEMIC_CLASS_CREATE("Can Create Class"),
    ACADEMIC_CLASS_SUBJECT_ASSIGN("Can Assign Subject"),
    ACADEMIC_CLASS_PROMOTION("Can Mange Promotion"),

    ACADEMIC_STUDENT_VIEW("Can View Student"),
    ACADEMIC_STUDENT_ASSIGN_TO_CLASS("Can Assign To Class"),
    ACADEMIC_STUDENT_SHIFT("Can Shift Student"),
    ACADEMIC_STUDENT_RELEASE("Can Release Class"),

    ACADEMIC_EXAMINATION_VIEW("Can View Marks"),
    ACADEMIC_EXAMINATION_MARKS("Can Manage Marks"),
    ACADEMIC_EXAMINATION_RESULT("Can Publish Result"),
    ACADEMIC_EXAMINATION_REPORT("Can Generate Report"),

    ACADEMIC_ATTENDANCE_VIEW("Can View Attendance"),
    ACADEMIC_ATTENDANCE_CREATE("Can Create Attendance"),
    ACADEMIC_ATTENDANCE_REPORT("Can Generate Report"),

    ACADEMIC_DISCIPLINE_VIEW("Can View Discipline"),
    ACADEMIC_DISCIPLINE_CREATE("Can Create Discipline"),

    /*Residence*/
    RESIDENCE_REFERENCE_VIEW("Can View Reference"),
    RESIDENCE_REFERENCE_CREATE("Can Create Reference"),

    RESIDENCE_SEAT_VIEW("Can View Seat"),
    RESIDENCE_SEAT_ASSIGN("Can Assign Seat"),
    RESIDENCE_SEAT_RELEASE("Can Release Seat"),

    RESIDENCE_ATTENDANCE_VIEW("Can View Attendance"),
    RESIDENCE_ATTENDANCE_CREATE("Can Create Attendance"),
    RESIDENCE_ATTENDANCE_REPORT("Can Generate Report"),

    RESIDENCE_DISCIPLINE_VIEW("Can View Discipline"),
    RESIDENCE_DISCIPLINE_CREATE("Can Create Discipline");

    private final String title;
}
