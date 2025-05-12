package bd.org.quantum.education.academic.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseClass {
    private Long academicClassId;
    private List<ReleaseStudent> students;
}
