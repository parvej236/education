package bd.org.quantum.education.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Group {
    Science("Science"),
    Business("Business"),
    Humanities("Humanities"),
    Wood_Working("Wood Working"),
    General_Mechanics("General Mechanics"),
    General_Electronics("General Electronics"),
    Wielding_Fabrication("Wielding & Fabrication"),
    Computer_Information_Technology("Computer & Information Technology");

    private final String title;

    public static List<String> getAll() {
        List<String> list = new ArrayList<>();
        for (Group group : Group.values()) {
            list.add(group.getTitle());
        }
        return list;
    }
}
