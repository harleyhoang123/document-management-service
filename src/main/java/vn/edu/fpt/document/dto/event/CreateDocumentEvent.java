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
public class CreateDocumentEvent implements Serializable {

    private static final long serialVersionUID = -2053179804783782331L;
    private String projectId;
    private String accountId;
}
