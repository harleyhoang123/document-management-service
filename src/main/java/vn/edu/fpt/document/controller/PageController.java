package vn.edu.fpt.document.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.fpt.document.dto.common.GeneralResponse;
import vn.edu.fpt.document.dto.request.page.CreatePageRequest;
import vn.edu.fpt.document.dto.request.page.UpdatePageRequest;
import vn.edu.fpt.document.dto.response.page.CreatePageResponse;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;

@RequestMapping("${app.application-context}/public/api/v1/pages")
public interface PageController {

    @PostMapping("/{page-id}/page")
    ResponseEntity<GeneralResponse<CreatePageResponse>> createPageInPage(@PathVariable(name = "page-id") String pageId, @RequestBody CreatePageRequest request);

    @PutMapping("/{parent-page-id}/{page-id}")
    ResponseEntity<GeneralResponse<Object>> updatePageInPage(@PathVariable(name = "parent-page-id") String parentPageId, @PathVariable(name = "page-id") String pageId, UpdatePageRequest request);

    @DeleteMapping("/{parent-page-id}/{page-id}")
    ResponseEntity<GeneralResponse<Object>> deletePageInPage(@PathVariable(name = "parent-page-id") String parentPageId, @PathVariable(name = "page-id") String pageId);

    @GetMapping("/{page-id}/{member-id}")
    ResponseEntity<GeneralResponse<GetPageDetailResponse>> getPageDetail(@PathVariable(name = "page-id") String pageId, @PathVariable(name = "member-id") String memberId);
}
