package bd.org.quantum.education.academic.attendance;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;
import java.util.List;

public class AcademicAttendanceSpecification {
    public static Specification<AcademicAttendance> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeInstitution = builder.like(builder.lower(root.get(AcademicAttendance_.institution)), contains(omniText.toLowerCase()));
            Predicate likeCls = builder.like(builder.lower(root.get(AcademicAttendance_.academicClass).get("clazz").get("name")), contains(omniText.toLowerCase()));
            Predicate likeClsGroup = builder.like(builder.lower(root.get(AcademicAttendance_.academicClass).get("classGroup")), contains(omniText.toLowerCase()));
            return builder.or(likeInstitution, likeCls, likeClsGroup);
        };
    }

    public static Specification<AcademicAttendance> equalType(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicAttendance_.type), type);
    }

    public static Specification<AcademicAttendance> equalDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicAttendance_.date), date);
    }

    public static Specification<AcademicAttendance> equalInstitution(Long institution) {
        if (StringUtils.isEmpty(institution) || institution == 0) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicAttendance_.academicClass).get("institution").get("id"), institution);
    }

    public static Specification<AcademicAttendance> equalCls(Long cls) {
        if (StringUtils.isEmpty(cls)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicAttendance_.academicClass).get("clazz").get("id"), cls);
    }

    public static Specification<AcademicAttendance> equalClsGroup(String clsGroup) {
        if (StringUtils.isEmpty(clsGroup)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicAttendance_.academicClass).get("classGroup"), clsGroup);
    }

    public static Specification<AcademicAttendance> equalSection(String section) {
        if (StringUtils.isEmpty(section)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicAttendance_.academicClass).get("section"), section);
    }

    public static Specification<AcademicAttendance> equalSession(Integer session) {
        if (session == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicAttendance_.academicClass).get("session"), session);
    }

    public static Specification<AcademicAttendance> inAccessInstitutions(List<Long> accessInstitutions) {
        return (root, query, builder) -> root.get(AcademicAttendance_.academicClass).get("institution").get("id").in(accessInstitutions);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
