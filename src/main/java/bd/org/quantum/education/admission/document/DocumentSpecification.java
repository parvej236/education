package bd.org.quantum.education.admission.document;

import bd.org.quantum.common.utils.SearchForm;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;

public class DocumentSpecification {
    public static Specification<Document> all() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static Specification<Document> omniText(SearchForm searchForm) {

        if (searchForm.getOmniText() == null || StringUtils.isEmpty(searchForm.getOmniText())) {
            return null;
        }

        return (root, query, builder) -> {
            Predicate likeDocumentName =
                    builder.like(
                            builder.lower(root.get(Document_.documentName)),
                            contains(searchForm.getOmniText().toLowerCase()));

            Predicate likeDocumentCode =
                    builder.like(
                            builder.lower(root.get(Document_.documentCode)),
                            contains(searchForm.getOmniText().toLowerCase()));

            return builder.or(likeDocumentName, likeDocumentCode);
        };
    }

    static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
