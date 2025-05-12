package bd.org.quantum.education.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Long id;
    private String name;
    private String employeeId;
    private String phone;
    private String branch;
    private String department;
    private String designation;
}