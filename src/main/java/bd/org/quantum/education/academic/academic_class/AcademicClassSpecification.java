package bd.org.quantum.education.academic.academic_class;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;
import java.util.List;

public class AcademicClassSpecification {
    public static Specification<AcademicClass> omniText(String omniText) {
        if (StringUtils.isEmpty(omniText)) {
            return null;
        }
        return (root, query, builder) -> {
            Predicate likeInstitution = builder.like(builder.lower(root.get(AcademicClass_.institution).get("name")), contains(omniText.toLowerCase()));

            Predicate likeClass = builder.like(builder.lower(root.get(AcademicClass_.clazz).get("name")), contains(omniText.toLowerCase()));

            Predicate likeClassGroup = builder.like(builder.lower(root.get(AcademicClass_.classGroup)), contains(omniText.toLowerCase()));

            Predicate likeSection = builder.like(builder.lower(root.get(AcademicClass_.section)), contains(omniText.toLowerCase()));

            return builder.or(likeInstitution, likeClass, likeClassGroup, likeSection);
        };
    }

    public static Specification<AcademicClass> institutionEquals(Long institution) {
        if (institution == null || institution == 0) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicClass_.institution).get("id"), institution);
    }

    public static Specification<AcademicClass> classEquals(Long clazz) {
        if (clazz == null || clazz == 0) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicClass_.clazz).get("id"), clazz);
    }

    public static Specification<AcademicClass> classGroupEquals(String classGroup) {
        if (StringUtils.isEmpty(classGroup)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicClass_.classGroup), classGroup);
    }

    public static Specification<AcademicClass> sectionEquals(String section) {
        if (StringUtils.isEmpty(section)) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicClass_.section), section);
    }

    public static Specification<AcademicClass> sessionEquals(Integer session) {
        if (session == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicClass_.session), session);
    }

    public static Specification<AcademicClass> statusEquals(Integer status) {
        if (status == null) {
            return null;
        }

        //Academic Class List - all | 0 1 2 3
        //Promotable List | 2 && releaseStatus 0
        //Releasable List | 2 && releaseStatus 0
        Specification<AcademicClass> spc = null;

        if (status == 0) {
            spc = (root, query, builder) -> builder.equal(root.get(AcademicClass_.status), 0);
        } else if (status == 1) {
            spc = (root, query, builder) -> builder.notEqual(root.get(AcademicClass_.status), 0);
        }else if (status == 2) {
            spc = (root, query, builder) -> builder.equal(root.get(AcademicClass_.status), 2);
        }
        return spc;
    }

    public static Specification<AcademicClass> releaseStatusEquals(Integer releaseStatus) {
        if (releaseStatus == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get(AcademicClass_.releaseStatus), releaseStatus);
    }

    public static Specification<AcademicClass> inAccessInstitutions(List<Long> accessInstitutions) {
        return (root, query, builder) -> root.get(AcademicClass_.institution).get("id").in(accessInstitutions);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}