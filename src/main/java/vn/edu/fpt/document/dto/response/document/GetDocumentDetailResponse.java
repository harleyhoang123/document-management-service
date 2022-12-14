package vn.edu.fpt.document.dto.response.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.document.dto.common.UserInfoResponse;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetDocumentDetailResponse implements Serializable {

    private static final long serialVersionUID = 2662832600600560976L;
    private String documentId;
    private String documentName;
    private UserInfoResponse memberInfo;
    private List<LeftOffPageResponse> leftOff;
    private List<DiscoverPageResponse> discover;
}
