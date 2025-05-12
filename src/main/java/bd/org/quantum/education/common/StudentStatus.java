package bd.org.quantum.education.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StudentStatus {
    CURRENT("Current Quanta"),
    OUTER("Higher Studying Quanta"),
    EX("Ex Quanta"),
    DROPOUT("Dropout Quanta"),
    SUSPENDED("Suspended Quanta"),
    EXPELLED("Expelled Quanta"),

    PROMOTED("Promoted"),
    NOT_PROMOTED("Not Promoted"),
    SHIFTED("Shifted"),

    NEW("Newly Admitted");

    @NonNull
    private final String title;
}
