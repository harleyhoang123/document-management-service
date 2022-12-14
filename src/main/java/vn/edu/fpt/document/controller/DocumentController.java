package vn.edu.fpt.document.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.document.dto.common.GeneralResponse;
import vn.edu.fpt.document.dto.common.PageableResponse;
import vn.edu.fpt.document.dto.request.page.CreatePageRequest;
import vn.edu.fpt.document.dto.request.page.UpdatePageRequest;
import vn.edu.fpt.document.dto.response.document.CreateDocumentResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentByAccountIdResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentDetailResponse;
import vn.edu.fpt.document.dto.response.document.GetPageOfDocumentResponse;
import vn.edu.fpt.document.dto.response.page.CreatePageResponse;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;

import java.util.List;

@RequestMapping("${app.application-context}/public/api/v1/documents")
public interface DocumentController {

    @DeleteMapping("/{document-id}")
    ResponseEntity<GeneralResponse<Object>> deleteDocument(@PathVariable(name = "document-id") String documentId);

    @GetMapping("/{document-id}")
    ResponseEntity<GeneralResponse<GetDocumentDetailResponse>> getDocumentDetail(@PathVariable(name = "document-id") String documentId);

    @GetMapping("/{document-id}/pages")
    ResponseEntity<GeneralResponse<List<GetPageOfDocumentResponse>>> getPageOfDocument(@PathVariable(name = "document-id") String documentId);

    @PostMapping("/{document-id}/pages/page")
    ResponseEntity<GeneralResponse<CreatePageResponse>> createPageInDocument(@PathVariable(name = "document-id") String documentId, @RequestBody CreatePageRequest request);

    @PutMapping("/{document-id}/{page-id}")
    ResponseEntity<GeneralResponse<Object>> updatePageInDocument(@PathVariable(name = "document-id") String documentId, @PathVariable(name = "page-id") String pageId, UpdatePageRequest request);

    @DeleteMapping("/{document-id}/{page-id}")
    ResponseEntity<GeneralResponse<Object>> deletePageInDocument(@PathVariable(name = "document-id") String documentId, @PathVariable(name = "page-id") String pageId);

    @GetMapping("/account/{account-id}")
    ResponseEntity<GeneralResponse<PageableResponse<GetDocumentByAccountIdResponse>>> getDocumentByAccountId(@PathVariable(name = "account-id") String accountId);

}
