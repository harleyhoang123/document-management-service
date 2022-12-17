package vn.edu.fpt.document.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.document.dto.common.AuditableResponse;
import vn.edu.fpt.document.entity.common.Auditor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class GetPageVersionsResponse extends AuditableResponse {

    private static final long serialVersionUID = 2813053135448748349L;
    private String contentId;
    private Integer version;
}
