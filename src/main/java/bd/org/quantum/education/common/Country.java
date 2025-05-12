package bd.org.quantum.education.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    private Long id;
    private String name;
    private String nameIso2;
    private String nameIso3;
    private Integer dialCode;
    private String currency;
    private String currencyCode;
    private String symbol;
}