package bd.org.quantum.education.common;

public final class Routes {

    public static final String ADMISSION = "/admission";
    public static final String APPLICANTS = ADMISSION + "/applicants";
    public static final String SEARCH_APPLICANT = ADMISSION + "/search-applicant";
    public static final String CREATE_APPLICANT = ADMISSION + "/create-applicant";
    public static final String APPLICANT_LIST = ADMISSION + "/applicant-list";
    public static final String APPLICANT_IMAGE_UPLOAD = ADMISSION + "/applicant/image/upload";
    public static final String APPLICANT_IMAGE_DOWNLOAD = ADMISSION + "/applicant/image/download";
    public static final String PERMITTED_APPLICANTS = ADMISSION + "/permitted-applicants";
    public static final String PERMITTED_APPLICANTS_SEARCH = ADMISSION + "/permitted-applicants-search";
    public static final String EXAM_CENTER_LIST = ADMISSION + "/exam-center-list";
    public static final String EXAM_CENTER_ENTRY = ADMISSION + "/exam-center-entry";
    public static final String EXAM_CENTER_UPDATE = ADMISSION + "/exam-center-update";
    public static final String SEARCH_EXAM_CENTER = ADMISSION + "/search-exam-center";
    public static final String APPLICANT_REPORT = ADMISSION + "/applicant-report";

    public static final String STUDENTS = ADMISSION + "/students";
    public static final String SEARCH_STUDENT = ADMISSION + "/search-student";
    public static final String CREATE_STUDENT = ADMISSION + "/create-student";
    public static final String UPDATE_STUDENT = ADMISSION + "/update-student";
    public static final String GET_STUDENT_BY_NAME_OR_QUANTAA_ID = ADMISSION + "/get-student-by-name-or-quanta-id";
    public static final String ASSIGN_DOCUMENT = ADMISSION + "/assign-document";
    public static final String STUDENT_DOCUMENT_LIST = ADMISSION + "/student-document-list";
    public static final String STUDENT_DOCUMENT_SEARCH = ADMISSION + "/student-document-search";
    public static final String IS_ASSIGNED_DOCUMENT_BY_STUDENT_ID = ADMISSION + "/is-assigned-document-by-student-id";
    public static final String GUARDIAN_ENTRY = ADMISSION + "/guardian-entry";
    public static final String GUARDIAN_LIST = ADMISSION + "/guardian-list";
    public static final String GUARDIAN_IMAGE_UPLOAD = ADMISSION + "/guardian/image/upload";
    public static final String GUARDIAN_IMAGE_DOWNLOAD = ADMISSION + "/guardian/image/download";
    public static final String GUARDIAN_SEARCH = ADMISSION + "/guardian-search";
    public static final String GET_GUARDIAN_LIST_BY_STUDENT_ID = ADMISSION + "/get-guardian-list-by-student-id";
    public static final String STUDENT_REPORT = ADMISSION + "/student-report";

    public static final String DOCUMENTS= ADMISSION + "/documents";
    public static final String DOCUMENTS_INFORMATION = ADMISSION + "/document-information";
    public static final String DOCUMENTS_INFORMATION_UPDATE = ADMISSION + "/update-document-information";
    public static final String DOCUMENTS_REPORT = ADMISSION + "/document-report";
    public static final String SEARCH_DOCUMENT = ADMISSION + "/document-search";
    public static final String GET_ADMISSION_DOCUMENT_BY_DOCUMENT_CODE = ADMISSION + "/get-admission-document-by-document-code";

    public static final String GUARDIAN_APPOINTMENT_ENTRY = ADMISSION + "/guardian-appointment-entry";
    public static final String GUARDIAN_APPOINTMENT_LIST = ADMISSION + "/guardian-appointment-list";
    public static final String GUARDIAN_APPOINTMENT_SEARCH = ADMISSION + "/guardian-appointment-search";

    public static final String ACADEMIC = "/academic";
    public static final String ACADEMIC_INSTITUTION_ENTRY = ACADEMIC + "/institution-entry";
    public static final String ACADEMIC_INSTITUTION_LIST = ACADEMIC + "/institution-list";
    public static final String ACADEMIC_INSTITUTION_SEARCH = ACADEMIC + "/institution-search";
    public static final String ACADEMIC_GET_CLASS_LIST_BASE_ON_INSTITUTION = "/institution/get/{id}";
    public static final String ACADEMIC_CLASS_ENTRY = ACADEMIC + "/class-entry";
    public static final String ACADEMIC_CLASS_LIST = ACADEMIC + "/class-list";
    public static final String ACADEMIC_CLASS_SEARCH = ACADEMIC + "/class-search";
    public static final String ACADEMIC_SUBJECT_ENTRY = ACADEMIC + "/subject-entry";
    public static final String ACADEMIC_SUBJECT_LIST = ACADEMIC + "/subject-list";
    public static final String ACADEMIC_SUBJECT_SEARCH = ACADEMIC + "/subject-search";
    public static final String ACADEMIC_SUBJECT_TYPE_ENTRY = ACADEMIC + "/subject-type-entry";
    public static final String ACADEMIC_SUBJECT_TYPE_LIST = ACADEMIC + "/subject-type-list";
    public static final String ACADEMIC_SUBJECT_TYPE_SEARCH = ACADEMIC + "/subject-type-search";
    public static final String ACADEMIC_EXAM_ENTRY = ACADEMIC + "/exam-entry";
    public static final String ACADEMIC_EXAM_LIST = ACADEMIC + "/exam-list";
    public static final String ACADEMIC_EXAM_SEARCH = ACADEMIC + "/exam-search";
    public static final String ACADEMIC_USER_ACCESS = ACADEMIC + "/user-access";
    public static final String ACADEMIC_USER_EXIST_CHECK = ACADEMIC + "/check-user-access-exist";
    public final static String ACADEMIC_USERS_BY_NAME_REG_OR_PHONE = ACADEMIC + "/users-by-name-reg-or-phone";

    public static final String ACADEMIC_CLASS_PROMOTABLE_LIST = ACADEMIC + "/academic-class-promotable-list";
    public static final String ACADEMIC_CLASS_PROMOTION = ACADEMIC + "/academic-class-promotion";
    public static final String ACADEMIC_CLASS_CLASS_ENTRY = ACADEMIC + "/academic-class-entry";
    public static final String ACADEMIC_CLASS_CLASS_LIST = ACADEMIC + "/academic-class-list";
    public static final String ACADEMIC_CLASS_CLASS_SEARCH = ACADEMIC + "/academic-class-search";
    public static final String ACADEMIC_CLASS_SUBJECT_ASSIGN = ACADEMIC + "/class-subject-assign";
    public static final String ACADEMIC_CLASS_SUBJECT_ASSIGNED_CLASS_LIST = ACADEMIC + "/subject-assigned-class-list";
    public static final String ACADEMIC_CLASS_SUBJECT_NOT_ASSIGNED_CLASS_LIST = ACADEMIC + "/subject-not-assigned-class-list";

    public static final String ACADEMIC_STUDENT_SEARCH = ACADEMIC + "/student-search";
    public static final String ACADEMIC_STUDENT_LIST = ACADEMIC + "/student-list";
    public static final String ACADEMIC_STUDENT_PROFILE = ACADEMIC + "/student-profile";
    public static final String ACADEMIC_SHIFT_STUDENT = ACADEMIC + "/shift-student";
    public static final String ACADEMIC_SHIFT_LOG = ACADEMIC + "/shift-log";
    public static final String ACADEMIC_SHIFT_LOG_SEARCH = ACADEMIC + "/shift-log-search";
    public static final String ACADEMIC_RELEASABLE_LIST = ACADEMIC + "/releasable-list";
    public static final String ACADEMIC_RELEASE_STUDENT = ACADEMIC + "/release-student";
    public static final String ACADEMIC_STUDENT_LIST_WITHOUT_CLASS = ACADEMIC + "/students-without-class";
    public static final String ACADEMIC_ASSIGN_STUDENT_TO_CLASS = ACADEMIC + "/assign-student-to-class";

    public static final String ACADEMIC_EXAM_MARK_SHEET_ENTRY = ACADEMIC + "/exam-mark-sheet-entry";
    public static final String ACADEMIC_EXAM_MARK_ENTRY = ACADEMIC + "/exam-mark-entry";
    public static final String ACADEMIC_EXAM_MARK_SHEET_LIST = ACADEMIC + "/exam-mark-sheet-list";
    public static final String ACADEMIC_EXAM_MARKS_SEARCH = ACADEMIC + "/exam-marks-search";
    public static final String ACADEMIC_GET_ACADEMIC_CLASS_LIST_BASE_ON_INSTITUTION = "/get-academic-class-list/{id}";
    public static final String ACADEMIC_GET_SUBJECT_LIST_BASE_ON_ACADEMIC_CLASS = "/get-subject-list/{id}";
    public static final String ACADEMIC_RESULT_PUBLICATION_ENTRY = ACADEMIC + "/result-publication-entry";
    public static final String ACADEMIC_EXAM_REPORT = ACADEMIC + "/exam-report";

    public static final String ACADEMIC_ATTENDANCE_ENTRY = ACADEMIC + "/attendance-entry";
    public static final String ACADEMIC_ATTENDANCE_SHEET_ENTRY = ACADEMIC + "/attendance-sheet-entry";
    public static final String ACADEMIC_ATTENDANCE_SHEET_SEARCH = ACADEMIC + "/attendance-sheet-search";
    public static final String ACADEMIC_ATTENDANCE_SHEET_LIST = ACADEMIC + "/attendance-sheet-list";
    public static final String ACADEMIC_ATTENDANCE_REPORT = ACADEMIC + "/attendance-report";

    public static final String ACADEMIC_CASE_ENTRY = ACADEMIC + "/case-entry";
    public static final String ACADEMIC_CASE_LIST = ACADEMIC + "/case-list";

    public static final String RESIDENCE = "/residence";
    public static final String RESIDENCE_RESIDENCE_LIST = RESIDENCE + "/residence-list";
    public static final String RESIDENCE_RESIDENCE_ENTRY = RESIDENCE + "/residence-entry";
    public static final String RESIDENCE_RESIDENCE_SEARCH = RESIDENCE + "/residence-search";
    public static final String RESIDENCE_HALL_LIST = RESIDENCE + "/hall-list";
    public static final String RESIDENCE_HALL_ENTRY = RESIDENCE + "/hall-entry";
    public static final String RESIDENCE_HALL_SEARCH = RESIDENCE + "/hall-search";
    public static final String RESIDENCE_ROOM_LIST = RESIDENCE + "/room-list";
    public static final String RESIDENCE_ROOM_ENTRY = RESIDENCE + "/room-entry";
    public static final String RESIDENCE_ROOM_SEARCH = RESIDENCE + "/room-search";

    public static final String RESIDENCE_SEAT_ASSIGN = RESIDENCE + "/seat-assign";
    public static final String RESIDENCE_SEAT_LIST = RESIDENCE + "/seat-list";
    public static final String RESIDENCE_SEAT_SEARCH = RESIDENCE + "/seat-search";
    public static final String RESIDENCE_SEAT_BOOKING_LIST = RESIDENCE + "/seat-booking-list";
    public static final String RESIDENCE_SEAT_BOOKING_SEARCH = RESIDENCE + "/seat-booking-search";
    public static final String RESIDENCE_SEAT_BOOKING_LOG = RESIDENCE + "/seat-booking-log";
    public static final String RESIDENCE_SEAT_RELEASE = RESIDENCE + "/seat-release";

    public static final String RESIDENCE_ATTENDANCE_SHEET_ENTRY = RESIDENCE + "/attendance-sheet-entry";
    public static final String RESIDENCE_ATTENDANCE_SHEET_LIST = RESIDENCE + "/attendance-sheet-list";
    public static final String RESIDENCE_ATTENDANCE_SHEET_SEARCH = RESIDENCE + "/attendance-sheet-search";
    public static final String RESIDENCE_ATTENDANCE_ENTRY = RESIDENCE + "/attendance-entry";
    public static final String RESIDENCE_STUDENT_LIST = RESIDENCE + "/student-list";
    public static final String RESIDENCE_STUDENT_SEARCH = RESIDENCE + "/student-search";
    public static final String RESIDENCE_STUDENT_PROFILE = RESIDENCE + "/student-profile";

    public static final String RESIDENCE_CASE_ENTRY = RESIDENCE + "/case-entry";
    public static final String RESIDENCE_CASE_LIST = RESIDENCE + "/case-list";

    public static final String UPAZILAS = "/upazilas";
    public static final String UNIONS = "/unions";
}
