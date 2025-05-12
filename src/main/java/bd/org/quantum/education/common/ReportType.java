package bd.org.quantum.education.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum ReportType {
    APPLICANT_LIST(1, "Applicant List"),
    QUANTAA_LIST(2, "Quanta List");
//    QUANTAA_LIST_DETAILS(2, "Quanta List Details"),
//    QUANTAA_LIST_CUSTOM(2, "Quanta List (Custom Column)"),
//    QUANTAA_STATUS(2, "Quanta Status"),
//    QUANTAA_ID_CARD(2, "Quanta ID Card"),
//    QUANTAA_GUARDIAN_CARD(2, "Quanta Guardian Card"),
//    QUANTAA_ADDRESS_ENVELOPE(2, "Quanta Address Envelope");

    private final Integer category;
    private final String title;

    public static List<ReportType> getApplicantReportTypes() {
        List<ReportType> list = new ArrayList<>();
        list.add(APPLICANT_LIST);
        return list;
    }

    public static List<ReportType> getStudentReportTypes() {
        List<ReportType> list = new ArrayList<>();
        for (ReportType reportType : ReportType.values()) {
            if (reportType.getCategory() == 2) {
                list.add(reportType);
            }
        }
        return list;
    }
}
