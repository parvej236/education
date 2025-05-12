package bd.org.quantum.education.academic.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String mobileNumber;
    private String registrationNumber;
}
