package bd.org.quantum.education.residence.abason;

import bd.org.quantum.common.utils.Views;
import bd.org.quantum.education.common.AuditData;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "res_residence")
public class Residence extends AuditData {

    private String name;
    private String code;
    private String phone;
    private String email;
    private String responsiblePerson;
    private String address;
    private String remarks;

    @OneToMany(targetEntity = ResidenceClass.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            orphanRemoval = true)
    @JoinColumn(name = "residence_id", referencedColumnName = "id")
    @JsonView(Views.GetDetails.class)
    @OrderBy("id")
    private List<ResidenceClass> classList;

    @Transient
    private String action;
}
