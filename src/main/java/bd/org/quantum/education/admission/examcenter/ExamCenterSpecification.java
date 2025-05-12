package bd.org.quantum.education.admission.examcenter;

import bd.org.quantum.common.utils.SearchForm;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class ExamCenterSpecification {

    public static Specification<ExamCenter> all() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static Specification<ExamCenter> omniText(SearchForm searchForm) {

        if (searchForm.getOmniText() == null || StringUtils.isEmpty(searchForm.getOmniText())) {
            return null;
        }

        return (root, query, builder) -> {
            Predicate likeName =
                    builder.like(
                            builder.lower(root.get(ExamCenter_.name)),
                            contains(searchForm.getOmniText().toLowerCase()));

            Predicate likeCode =
                    builder.like(
                            builder.lower(root.get(ExamCenter_.code)),
                            contains(searchForm.getOmniText().toLowerCase()));

            return builder.or(likeName, likeCode);
        };
    }

    static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
