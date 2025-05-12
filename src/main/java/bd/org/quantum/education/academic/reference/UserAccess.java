package bd.org.quantum.education.academic.reference;

import bd.org.quantum.education.common.AuditData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "aca_user_access")
public class UserAccess extends AuditData {
    private Long userId;
    private String name;
    private String email;
    private String mobileNumber;
    private String registrationNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_access", referencedColumnName = "id")
    private List<AccessInstitution> institutions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_access", referencedColumnName = "id")
    private List<AccessResidence> residences;

    public List<Long> getAllAccessInstitutionId() {
        List<Long> institutionIds = new ArrayList<>();
        for (AccessInstitution institution : institutions) {
            if (institution.getIsAccessed()) {
                institutionIds.add(institution.getInstitution().getId());
            }
        }
        return institutionIds;
    }

    public List<Long> getAllAccessResidenceId() {
        List<Long> residenceIds = new ArrayList<>();
        for (AccessResidence residence : residences) {
            if (residence.getIsAccessed()) {
                residenceIds.add(residence.getResidence().getId());
            }
        }
        return residenceIds;
    }
}
