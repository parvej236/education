package bd.org.quantum.education.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Religion {
    ISLAM("Islam"),
    BUDDHISTS("Buddhists"),
    CHRISTIANITY("Christianity"),
    HINDUISM("Hinduism"),
    KRAMA("Krama"),
    ANIMISTS("Animists"),
    OTHER("Other");

    private final String title;
}
