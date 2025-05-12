package bd.org.quantum.education.authorizer;

import bd.org.quantum.authorizer.Authorizer;
import bd.org.quantum.authorizer.helper.MenuAuthorizer;
import bd.org.quantum.education.role.Permissions;
import org.springframework.stereotype.Component;

@Component
public class MenuAuthorizerImpl implements MenuAuthorizer {
    private final Authorizer authorizer;

    public MenuAuthorizerImpl(Authorizer authorizer) {
        this.authorizer = authorizer;
    }

    /*Admission Menu*/
    public boolean getCanViewAdmissionMenu() {
        return getCanViewApplicantMenu() || getCanViewStudentMenu() || getCanViewDocumentMenu() || getCanViewAppointmentMenu();
    }

    /*Applicant*/
    public boolean getCanViewApplicantMenu() {
        return getCanViewApplicant() || getCanCreateApplicant() || getCanManageExamCenter() || getCanGenerateApplicantReport();
    }

    public boolean getCanViewApplicant() {
        return authorizer.hasPermission(Permissions.ADMISSION_APPLICANT_VIEW.name());
    }

    public boolean getCanCreateApplicant() {
        return authorizer.hasPermission(Permissions.ADMISSION_APPLICANT_CREATE.name());
    }

    public boolean getCanManageExamCenter() {
        return authorizer.hasPermission(Permissions.ADMISSION_APPLICANT_EXAM_CENTER.name());
    }

    public boolean getCanGenerateApplicantReport() {
        return authorizer.hasPermission(Permissions.ADMISSION_APPLICANT_REPORT.name());
    }

    /*Student*/
    public boolean getCanViewStudentMenu() {
        return getCanViewStudent() || getCanCreateStudent() || getCanManageGuardain() || getCanAssignDocument() || getCanGenerateStudentReport();
    }

    public boolean getCanViewStudent() {
        return authorizer.hasPermission(Permissions.ADMISSION_STUDENT_VIEW.name());
    }

    public boolean getCanCreateStudent() {
        return authorizer.hasPermission(Permissions.ADMISSION_STUDENT_CREATE.name());
    }

    public boolean getCanManageGuardain() {
        return authorizer.hasPermission(Permissions.ADMISSION_STUDENT_GUARDAIN.name());
    }

    public boolean getCanAssignDocument() {
        return authorizer.hasPermission(Permissions.ADMISSION_STUDENT_ASSIGN_DOCUMENT.name());
    }

    public boolean getCanGenerateStudentReport() {
        return authorizer.hasPermission(Permissions.ADMISSION_STUDENT_REPORT.name());
    }

    /*Documents*/
    public boolean getCanViewDocumentMenu() {
        return getCanViewDocument() || getCanCreateDocument() || getCanGenerateDocumentReport();
    }

    public boolean getCanViewDocument() {
        return authorizer.hasPermission(Permissions.ADMISSION_DOCUMENT_VIEW.name());
    }

    public boolean getCanCreateDocument() {
        return authorizer.hasPermission(Permissions.ADMISSION_DOCUMENT_CREATE.name());
    }

    public boolean getCanGenerateDocumentReport() {
        return authorizer.hasPermission(Permissions.ADMISSION_DOCUMENT_REPORT.name());
    }

    /*Guardian Appointment*/
    public boolean getCanViewAppointmentMenu() {
        return getCanViewAppointment() || getCanCreateAppointment();
    }

    public boolean getCanViewAppointment() {
        return authorizer.hasPermission(Permissions.ADMISSION_APPOINTMENT_VIEW.name());
    }

    public boolean getCanCreateAppointment() {
        return authorizer.hasPermission(Permissions.ADMISSION_APPOINTMENT_CREATE.name());
    }

    /*Academic Menu*/
    public boolean getCanViewAcademicMenu() {
        return getCanViewAcademicReferenceMenu() || getCanViewAcademicClassMenu() || getCanViewAcademicStudentMenu() || getCanViewExaminationMenu() || getCanViewAcademicAttendanceMenu() || getCanViewAcademicDisciplineMenu();
    }

    /*Academic Reference*/
    public boolean getCanViewAcademicReferenceMenu() {
        return getCanViewAcademicReference() || getCanCreateAcademicReference() || getCanManageUserAccess();
    }

    public boolean getCanViewAcademicReference() {
        return authorizer.hasPermission(Permissions.ACADEMIC_REFERENCE_VIEW.name());
    }

    public boolean getCanCreateAcademicReference() {
        return authorizer.hasPermission(Permissions.ACADEMIC_REFERENCE_CREATE.name());
    }

    public boolean getCanManageUserAccess() {
        return authorizer.hasPermission(Permissions.ACADEMIC_REFERENCE_USER_ACCESS.name());
    }

    /*Academic Class*/
    public boolean getCanViewAcademicClassMenu() {
        return getCanViewAcademicClass() || getCanCreateAcademicClass() || getCanAssignSubject() || getCanManagePromotion();
    }

    public boolean getCanViewAcademicClass() {
        return authorizer.hasPermission(Permissions.ACADEMIC_CLASS_VIEW.name());
    }

    public boolean getCanCreateAcademicClass() {
        return authorizer.hasPermission(Permissions.ACADEMIC_CLASS_CREATE.name());
    }

    public boolean getCanAssignSubject() {
        return authorizer.hasPermission(Permissions.ACADEMIC_CLASS_SUBJECT_ASSIGN.name());
    }

    public boolean getCanManagePromotion() {
        return authorizer.hasPermission(Permissions.ACADEMIC_CLASS_PROMOTION.name());
    }

    /*Academic Student*/
    public boolean getCanViewAcademicStudentMenu() {
        return getCanViewAcademicStudent() || getCanAssignStudentToClass() || getCanShiftStudent() || getCanReleaseClass();
    }

    public boolean getCanViewAcademicStudent() {
        return authorizer.hasPermission(Permissions.ACADEMIC_STUDENT_VIEW.name());
    }

    public boolean getCanAssignStudentToClass() {
        return authorizer.hasPermission(Permissions.ACADEMIC_STUDENT_ASSIGN_TO_CLASS.name());
    }

    public boolean getCanShiftStudent() {
        return authorizer.hasPermission(Permissions.ACADEMIC_STUDENT_SHIFT.name());
    }

    public boolean getCanReleaseClass() {
        return authorizer.hasPermission(Permissions.ACADEMIC_STUDENT_RELEASE.name());
    }

    /*Academic Examination*/
    public boolean getCanViewExaminationMenu() {
        return getCanViewExamMarkSheet() || getCanManageExamMarkSheet() || getCanPublishResult() || getCanGenerateExamReport();
    }

    public boolean getCanViewExamMarkSheet() {
        return authorizer.hasPermission(Permissions.ACADEMIC_EXAMINATION_VIEW.name());
    }

    public boolean getCanManageExamMarkSheet() {
        return authorizer.hasPermission(Permissions.ACADEMIC_EXAMINATION_MARKS.name());
    }

    public boolean getCanPublishResult() {
        return authorizer.hasPermission(Permissions.ACADEMIC_EXAMINATION_RESULT.name());
    }

    public boolean getCanGenerateExamReport() {
        return authorizer.hasPermission(Permissions.ACADEMIC_EXAMINATION_REPORT.name());
    }

    /*Academic Attendance*/
    public boolean getCanViewAcademicAttendanceMenu() {
        return getCanViewAcademicAttendance() || getCanCreateAcademicAttendance() || getCanGenerateAcademicAttendanceReport();
    }

    public boolean getCanViewAcademicAttendance() {
        return authorizer.hasPermission(Permissions.ACADEMIC_ATTENDANCE_VIEW.name());
    }

    public boolean getCanCreateAcademicAttendance() {
        return authorizer.hasPermission(Permissions.ACADEMIC_ATTENDANCE_CREATE.name());
    }

    public boolean getCanGenerateAcademicAttendanceReport() {
        return authorizer.hasPermission(Permissions.ACADEMIC_ATTENDANCE_REPORT.name());
    }

    /*Academic Discipline*/
    public boolean getCanViewAcademicDisciplineMenu() {
        return getCanViewAcademicDiscipline() || getCanCreateAcademicDiscipline();
    }

    public boolean getCanViewAcademicDiscipline() {
        return authorizer.hasPermission(Permissions.ACADEMIC_DISCIPLINE_VIEW.name());
    }

    public boolean getCanCreateAcademicDiscipline() {
        return authorizer.hasPermission(Permissions.ACADEMIC_DISCIPLINE_CREATE.name());
    }

    /*Residence Menu*/
    public boolean getCanViewResidenceMenu() {
        return getCanViewResidenceReferenceMenu() || getCanViewSeatMenu() || getCanViewResidenceAttendanceMenu() || getCanViewResidenceDisciplineMenu();
    }

    /*Residence Reference*/
    public boolean getCanViewResidenceReferenceMenu() {
        return getCanViewResidenceReference() || getCanCreateResidenceReference();
    }

    public boolean getCanViewResidenceReference() {
        return authorizer.hasPermission(Permissions.RESIDENCE_REFERENCE_VIEW.name());
    }

    public boolean getCanCreateResidenceReference() {
        return authorizer.hasPermission(Permissions.RESIDENCE_REFERENCE_CREATE.name());
    }

    /*Residence Seat*/
    public boolean getCanViewSeatMenu() {
        return getCanViewSeat() || getCanAssignSeat() || getCanReleaseSeat();
    }

    public boolean getCanViewSeat() {
        return authorizer.hasPermission(Permissions.RESIDENCE_SEAT_VIEW.name());
    }

    public boolean getCanAssignSeat() {
        return authorizer.hasPermission(Permissions.RESIDENCE_SEAT_ASSIGN.name());
    }

    public boolean getCanReleaseSeat() {
        return authorizer.hasPermission(Permissions.RESIDENCE_SEAT_RELEASE.name());
    }

    /*Residence Attendance*/
    public boolean getCanViewResidenceAttendanceMenu() {
        return getCanViewResidenceAttendance() || getCanCreateResidenceAttendance() || getCanGenerateResidenceAttendanceReport();
    }

    public boolean getCanViewResidenceAttendance() {
        return authorizer.hasPermission(Permissions.RESIDENCE_ATTENDANCE_VIEW.name());
    }

    public boolean getCanCreateResidenceAttendance() {
        return authorizer.hasPermission(Permissions.RESIDENCE_ATTENDANCE_CREATE.name());
    }

    public boolean getCanGenerateResidenceAttendanceReport() {
        return authorizer.hasPermission(Permissions.RESIDENCE_ATTENDANCE_REPORT.name());
    }

    /*Residence Discipline*/
    public boolean getCanViewResidenceDisciplineMenu() {
        return getCanViewResidenceDiscipline() || getCanCreateResidenceDiscipline();
    }

    public boolean getCanViewResidenceDiscipline() {
        return authorizer.hasPermission(Permissions.RESIDENCE_DISCIPLINE_VIEW.name());
    }

    public boolean getCanCreateResidenceDiscipline() {
        return authorizer.hasPermission(Permissions.RESIDENCE_DISCIPLINE_CREATE.name());
    }
}
