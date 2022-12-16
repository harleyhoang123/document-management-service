package vn.edu.fpt.document.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.document.constant.ResponseStatusEnum;
import vn.edu.fpt.document.controller.DocumentController;
import vn.edu.fpt.document.dto.common.GeneralResponse;
import vn.edu.fpt.document.dto.common.PageableResponse;
import vn.edu.fpt.document.dto.request.page.CreatePageRequest;
import vn.edu.fpt.document.dto.request.page.UpdatePageRequest;
import vn.edu.fpt.document.dto.response.document.GetDocumentByAccountIdResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentDetailResponse;
import vn.edu.fpt.document.dto.response.document.GetPageOfDocumentResponse;
import vn.edu.fpt.document.dto.response.page.CreatePageResponse;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;
import vn.edu.fpt.document.factory.ResponseFactory;
import vn.edu.fpt.document.service.DocumentService;
import vn.edu.fpt.document.service.PageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DocumentControllerImpl implements DocumentController {

    private final ResponseFactory responseFactory;
    private final DocumentService documentService;
    private final PageService pageService;

    @Override
    public ResponseEntity<GeneralResponse<Object>> deleteDocument(String documentId) {
        documentService.deleteDocument(documentId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<GetPageDetailResponse>> getPageOverview(String documentId) {
        return responseFactory.response(documentService.getPageOverview(documentId));
    }

    @Override
    public ResponseEntity<GeneralResponse<GetDocumentDetailResponse>> getDocumentDetail(String documentId) {
        return responseFactory.response(documentService.getDocumentDetail(documentId));
    }

    @Override
    public ResponseEntity<GeneralResponse<List<GetPageOfDocumentResponse>>> getPageOfDocument(String documentId) {
        return responseFactory.response(documentService.getPageOfDocument(documentId));
    }

    @Override
    public ResponseEntity<GeneralResponse<CreatePageResponse>> createPageInDocument(String documentId, CreatePageRequest request) {
        return responseFactory.response(pageService.createPageInDocument(documentId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updatePageInDocument(String documentId, String pageId, UpdatePageRequest request) {
        pageService.updatePageInDocument(documentId, pageId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> deletePageInDocument(String documentId, String pageId) {
        pageService.deletePageInDocument(documentId, pageId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<PageableResponse<GetDocumentByAccountIdResponse>>> getDocumentByAccountId(String accountId) {
        return responseFactory.response(documentService.getDocumentByAccountId(accountId));
    }

}
