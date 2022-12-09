package vn.edu.fpt.document.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.document.dto.common.GeneralResponse;
import vn.edu.fpt.document.dto.request.page.CreatePageRequest;
import vn.edu.fpt.document.dto.request.page.UpdatePageRequest;
import vn.edu.fpt.document.dto.response.document.CreateDocumentResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentDetailResponse;
import vn.edu.fpt.document.dto.response.page.CreatePageResponse;

@RequestMapping("${app.application-context}/public/api/v1/documents")
public interface DocumentController {

    @DeleteMapping("/{project-id}")
    ResponseEntity<GeneralResponse<Object>> deleteDocument(@PathVariable(name = "project-id") String projectId);

    @GetMapping("/{project-id}")
    ResponseEntity<GeneralResponse<GetDocumentDetailResponse>> getDocumentDetail(@PathVariable(name = "project-id") String projectId);

    @PostMapping("/{document-id}/page")
    ResponseEntity<GeneralResponse<CreatePageResponse>> createPageInDocument(@PathVariable(name = "document-id") String documentId, @RequestBody CreatePageRequest request);

    @PutMapping("/{document-id}/{page-id}")
    ResponseEntity<GeneralResponse<Object>> updatePageInDocument(@PathVariable(name = "document-id") String documentId, @PathVariable(name = "page-id") String pageId, UpdatePageRequest request);

    @DeleteMapping("/{document-id}/{page-id}")
    ResponseEntity<GeneralResponse<Object>> deletePageInDocument(@PathVariable(name = "document-id") String documentId, @PathVariable(name = "page-id") String pageId);
}
