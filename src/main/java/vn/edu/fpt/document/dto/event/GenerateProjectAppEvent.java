package vn.edu.fpt.document.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GenerateProjectAppEvent implements Serializable {

    private static final long serialVersionUID = -8724387457493635242L;
    private String projectId;
    private String accountId;
    private String projectName;
}
