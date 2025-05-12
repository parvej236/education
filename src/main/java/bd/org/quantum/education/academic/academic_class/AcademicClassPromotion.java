package bd.org.quantum.education.academic.academic_class;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcademicClassPromotion {
    private Long preAcademicClass;
    private Long newAcademicClass;
    private List<PromotionStudent> studentList;
}
