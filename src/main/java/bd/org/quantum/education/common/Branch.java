package bd.org.quantum.education.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    private Long id;
    private String name;
    private String code;
    private String mobile;
    private String email;
    private String address;
    private String district;
    private int type;
    private int status;
    private Long parent;

}
