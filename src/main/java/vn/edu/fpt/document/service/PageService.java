package vn.edu.fpt.document.service;

import vn.edu.fpt.document.dto.common.PageableResponse;
import vn.edu.fpt.document.dto.request.page.CreatePageRequest;
import vn.edu.fpt.document.dto.request.page.UpdatePageRequest;
import vn.edu.fpt.document.dto.response.page.CreatePageResponse;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;
import vn.edu.fpt.document.dto.response.page.GetPageVersionsResponse;

public interface PageService {
    CreatePageResponse createPageInDocument(String documentId, CreatePageRequest request);

    CreatePageResponse createPageInPage(String pageId, CreatePageRequest request);

    void updatePageInDocument(String documentId, String pageId, UpdatePageRequest request);

    void updatePageInPage(String pageId, UpdatePageRequest request);

    void deletePageInDocument(String documentId, String pageId);

    void deletePageInPage(String parentPageId, String childPageId);

    GetPageDetailResponse getPageDetail(String pageId, String memberId);

    void lockPage(String pageId);

    PageableResponse<GetPageVersionsResponse> getPageVersions(String pageId);

    void revertVersion(String pageId, String contentId);

    void deletePage(String pageId);
}
