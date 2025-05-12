package bd.org.quantum.education.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum AttendanceType {
    RESIDENCE("Residence Attendance"),
    BREAKFAST("Breakfast Attendance"),
    LUNCH("Lunch Attendance"),
    DINNER("Dinner Attendance"),
    ACADEMIC_CLASS("Academic Attendance"),
    REFRESHMENT_CLASS("Refreshment Class Attendance");

    private final String title;

    public static List<AttendanceType> getAcademicAttendanceType(){
        List<AttendanceType> typeList = new ArrayList<>();
        typeList.add(ACADEMIC_CLASS);
        typeList.add(REFRESHMENT_CLASS);
        return typeList;
    }

    public static List<AttendanceType> getResidenceAttendanceType(){
        List<AttendanceType> typeList = new ArrayList<>();
        typeList.add(RESIDENCE);
        typeList.add(BREAKFAST);
        typeList.add(LUNCH);
        typeList.add(DINNER);
        return typeList;
    }
}
