package bd.org.quantum.education.admission.applicant;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class ApplicantSpecification {

    public static Specification<Applicant> all() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static Specification<Applicant> omniText(ApplicantSearchCriteria searchForm) {

        if (searchForm.getOmniText() == null || StringUtils.isEmpty(searchForm.getOmniText())) {
            return null;
        }

        return (root, query, builder) -> {
            Predicate likeNameEn =
                    builder.like(
                            builder.lower(root.get(Applicant_.nameEn)),
                            contains(searchForm.getOmniText().toLowerCase()));

            Predicate likeNameBn =
                    builder.like(
                            builder.lower(root.get(Applicant_.nameBn)),
                            contains(searchForm.getOmniText().toLowerCase()));

            Predicate likeFormNo =
                    builder.like(
                            builder.lower(root.get(Applicant_.formNo)),
                            contains(searchForm.getOmniText().toLowerCase()));

            return builder.or(likeNameBn, likeNameEn, likeFormNo);
        };
    }

    public static Specification<Applicant> equalGender(String gender) {
        if (gender == null || StringUtils.isEmpty(gender.trim())) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(builder.lower(root.get(Applicant_.gender)), gender);
    }

    public static Specification<Applicant> equalSession(String session) {
        if (session == null || StringUtils.isEmpty(session.trim())) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.session), session.trim());
    }

    public static Specification<Applicant> equalAppliedClass(String appliedClass) {
        if (appliedClass == null || StringUtils.isEmpty(appliedClass.trim())) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.appliedClass), appliedClass.trim());
    }

    public static Specification<Applicant> equalExamCenter(String examCenter) {
        if (examCenter == null || StringUtils.isEmpty(examCenter.trim())) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.examCenter), examCenter.trim());
    }

    public static Specification<Applicant> equalReligion(String religion) {
        if (religion == null || StringUtils.isEmpty(religion.trim())) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.religion), religion.trim());
    }

    public static Specification<Applicant> equalCommunity(String community) {
        if (community == null || StringUtils.isEmpty(community.trim())) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.community), community.trim());
    }

    public static Specification<Applicant> equalDistrict(String district) {
        if (district == null || StringUtils.isEmpty(district.trim())) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.presentDistrict), district.trim());
    }

    public static Specification<Applicant> equalUpazila(String upazila) {
        if (upazila == null || StringUtils.isEmpty(upazila.trim())) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.presentUpazila), upazila.trim());
    }

    public static Specification<Applicant> selection(Boolean selection) {
        if (selection == null) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.selected), selection);
    }

    public static Specification<Applicant> admitted(Boolean admitted) {
        if (admitted == null) {
            return null;
        }

        return (root, query, builder) -> builder
                .equal(root.get(Applicant_.admitted), admitted);
    }

    static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
