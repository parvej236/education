package bd.org.quantum.education.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportData {
    private ReportHeader header;
    private String footer;
    private String orientation;
    private String pageSize;
    private String view;
    private Map<String, Object> result;
}
