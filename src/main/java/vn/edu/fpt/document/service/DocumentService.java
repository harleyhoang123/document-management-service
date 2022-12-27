package vn.edu.fpt.document.service;

import vn.edu.fpt.document.constant.ResponseStatusEnum;
import vn.edu.fpt.document.dto.common.PageableResponse;
import vn.edu.fpt.document.dto.event.GenerateProjectAppEvent;
import vn.edu.fpt.document.dto.event.ModifyMembersToWorkspaceEvent;
import vn.edu.fpt.document.dto.request.document.GetMemberIdResponse;
import vn.edu.fpt.document.dto.response.document.CreateDocumentResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentByAccountIdResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentDetailResponse;
import vn.edu.fpt.document.dto.response.document.GetPageOfDocumentResponse;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;

import java.util.List;

public interface DocumentService {
    CreateDocumentResponse createDocument(GenerateProjectAppEvent event);

    void deleteDocument(String documentId);

    GetDocumentDetailResponse getDocumentDetail(String documentId);

    PageableResponse<GetDocumentByAccountIdResponse> getDocumentByAccountId(String accountId);

    List<GetPageOfDocumentResponse> getPageOfDocument(String documentId);

    GetPageDetailResponse getPageOverview(String documentId);

    GetMemberIdResponse getMemberId(String documentId);

    void modifyMembersToWorkspace(ModifyMembersToWorkspaceEvent event);
}
