package bd.org.quantum.education.residence.attendance;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;
import java.util.List;

public class ResidenceAttendanceSpecification {
    public static Specification<ResidenceAttendance> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeInstitution = builder.like(builder.lower(root.get(ResidenceAttendance_.institution)), contains(omniText.toLowerCase()));
            Predicate likeCls = builder.like(builder.lower(root.get(ResidenceAttendance_.academicClass).get("clazz").get("name")), contains(omniText.toLowerCase()));
            Predicate likeClsGroup = builder.like(builder.lower(root.get(ResidenceAttendance_.academicClass).get("classGroup")), contains(omniText.toLowerCase()));
            return builder.or(likeInstitution, likeCls, likeClsGroup);
        };
    }

    public static Specification<ResidenceAttendance> equalType(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(ResidenceAttendance_.type), type);
    }

    public static Specification<ResidenceAttendance> equalDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(ResidenceAttendance_.date), date);
    }

    public static Specification<ResidenceAttendance> equalInstitution(Long institution) {
        if (StringUtils.isEmpty(institution) || institution == 0) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(ResidenceAttendance_.academicClass).get("institution").get("id"), institution);
    }

    public static Specification<ResidenceAttendance> equalCls(Long cls) {
        if (StringUtils.isEmpty(cls)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(ResidenceAttendance_.academicClass).get("clazz").get("id"), cls);
    }

    public static Specification<ResidenceAttendance> equalClsGroup(String clsGroup) {
        if (StringUtils.isEmpty(clsGroup)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(ResidenceAttendance_.academicClass).get("classGroup"), clsGroup);
    }

    public static Specification<ResidenceAttendance> equalSection(String section) {
        if (StringUtils.isEmpty(section)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(ResidenceAttendance_.academicClass).get("section"), section);
    }

    public static Specification<ResidenceAttendance> equalSession(Integer session) {
        if (session == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(ResidenceAttendance_.academicClass).get("session"), session);
    }

    public static Specification<ResidenceAttendance> equalClasGroup(String clsGroup) {
        return equalClsGroup(clsGroup);
    }

    public static Specification<ResidenceAttendance> inAccessInstitutions(List<Long> accessInstitutions) {
        return (root, query, builder) -> root.get(ResidenceAttendance_.academicClass).get("institution").get("id").in(accessInstitutions);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
