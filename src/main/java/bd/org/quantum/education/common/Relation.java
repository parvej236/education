package bd.org.quantum.education.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum Relation {
    FATHER("Father"),
    MOTHER("Mother"),
    BROTHER("Brother"),
    SISTER("Sister"),
    GRANDFATHER("Grandfather"),
    GRANDMOTHER("Grandmother"),
    UNCLE("Uncle"),
    AUNT("Aunt"),
    NEPHEW("Nephew"),
    NIECE("Niece"),
    COUSIN("Cousin"),
    STEPFATHER("Stepfather"),
    STEPMOTHER("Stepmother"),
    STEPBROTHER("Stepbrother"),
    STEPSISTER("Stepsister"),
    BROTHER_IN_LAW("Brother-in-Law"),
    SISTER_IN_LAW("Sister-in-Law"),
    HUSBAND("Husband"),
    WIFE("Wife"),
    SON("Son"),
    DAUGHTER("Daughter"),
    FATHER_IN_LAW("Father-in-Law"),
    MOTHER_IN_LAW("Mother-in-Law"),
    SON_IN_LAW("Son-in-Law"),
    DAUGHTER_IN_LAW("Daughter-in-Law"),
    TEACHER("Teacher"),
    STUDENT("Student"),
    FRIEND("Friend"),
    CLASSMATE("Classmate"),
    DOCTOR("Doctor"),
    COLLEAGUE("Colleague"),
    NEIGHBOR("Neighbor"),
    RELATIVE("Relative"),
    KNOWS_AS("Knows as"),
    FOSTER_FATHER("Foster Father"),
    FOSTER_MOTHER("Foster Mother"),
    NOMINATED_GUARDIAN("Nominated Guardian"),
    OTHERS("Others");

    private final String title;

    public static List<Relation> getAllRelations() {
        return new ArrayList<>(Arrays.asList(Relation.values()));
    }
}
