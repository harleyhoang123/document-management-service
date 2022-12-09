package vn.edu.fpt.document.dto.response.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.document.dto.common.UserInfoResponse;
import vn.edu.fpt.document.dto.response.page.GetPageResponse;
import vn.edu.fpt.document.entity.MemberInfo;
import vn.edu.fpt.document.entity._Page;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetDocumentDetailResponse implements Serializable {

    private static final long serialVersionUID = 2662832600600560976L;
    private String documentId;
    private List<GetPageResponse> pages;
    private List<UserInfoResponse> members;
}
