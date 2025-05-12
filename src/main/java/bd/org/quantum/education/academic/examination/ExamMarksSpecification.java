package bd.org.quantum.education.academic.examination;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.text.MessageFormat;
import java.util.List;

public class ExamMarksSpecification {
    public static Specification<ExamMarks> omniText(String omniText) {
        return (root, query, builder) -> {
            Predicate likeInstitution =
                    builder.like(
                            builder.lower(root.get(ExamMarks_.academicClass).get("institution").get("name")),
                            contains(omniText.toLowerCase()));

            Predicate likeClass =
                    builder.like(
                            builder.lower(root.get(ExamMarks_.academicClass).get("clazz").get("name")),
                            contains(omniText.toLowerCase()));

            Predicate likeClassGroup =
                    builder.like(
                            builder.lower(root.get(ExamMarks_.academicClass).get("classGroup")),
                            contains(omniText.toLowerCase()));

            Predicate likeSection =
                    builder.like(
                            builder.lower(root.get(ExamMarks_.academicClass).get("section")),
                            contains(omniText.toLowerCase()));

            Predicate likeSubject =
                    builder.like(
                            builder.lower(root.get(ExamMarks_.subject).get("name")),
                            contains(omniText.toLowerCase()));

            Predicate likeExam =
                    builder.like(
                            builder.lower(root.get(ExamMarks_.exam).get("name")),
                            contains(omniText.toLowerCase()));

            return builder.or(likeInstitution, likeClass, likeClassGroup, likeSection, likeSubject, likeExam);
        };
    }

    public static Specification<ExamMarks> institutionEquals(Long institution) {
        return (root, query, builder) -> builder
                .equal(root.get(ExamMarks_.academicClass).get("institution").get("id"), institution);
    }

    public static Specification<ExamMarks> academicClassEquals(Long academicClass) {
        return (root, query, builder) -> builder
                .equal(root.get(ExamMarks_.academicClass).get("id"), academicClass);
    }

    public static Specification<ExamMarks> subjectEquals(Long subject) {
        return (root, query, builder) -> builder
                .equal(root.get(ExamMarks_.subject).get("id"), subject);
    }

    public static Specification<ExamMarks> examEquals(Long exam) {
        return (root, query, builder) -> builder
                .equal(root.get(ExamMarks_.exam).get("id"), exam);
    }

    public static Specification<ExamMarks> inAccessInstitutions(List<Long> accessInstitutions) {
        return (root, query, builder) -> root.get(ExamMarks_.academicClass).get("institution").get("id").in(accessInstitutions);
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
