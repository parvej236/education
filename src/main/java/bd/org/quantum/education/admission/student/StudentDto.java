package bd.org.quantum.education.admission.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String nameEn;
    private String gender;
    private String community;
    private String religion;
    private String dob;
    private String imagePath;
    private String studentId;
    private String cls;
    private String institution;
    private String residence;
    private String status;
    private String base64;
}
