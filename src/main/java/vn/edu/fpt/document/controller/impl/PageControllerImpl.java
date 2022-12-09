package vn.edu.fpt.document.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.document.constant.ResponseStatusEnum;
import vn.edu.fpt.document.controller.PageController;
import vn.edu.fpt.document.dto.common.GeneralResponse;
import vn.edu.fpt.document.dto.request.page.CreatePageRequest;
import vn.edu.fpt.document.dto.request.page.UpdatePageRequest;
import vn.edu.fpt.document.dto.response.page.CreatePageResponse;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;
import vn.edu.fpt.document.factory.ResponseFactory;
import vn.edu.fpt.document.service.DocumentService;
import vn.edu.fpt.document.service.PageService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PageControllerImpl implements PageController {

    private final ResponseFactory responseFactory;
    private final PageService pageService;

    @Override
    public ResponseEntity<GeneralResponse<CreatePageResponse>> createPageInPage(String pageId, CreatePageRequest request) {
        return responseFactory.response(pageService.createPageInPage(pageId, request));
    }

    @Override
    public ResponseEntity<GeneralResponse<Object>> updatePageInPage(String parentPageId, String pageId, UpdatePageRequest request) {
        pageService.updatePageInPage(parentPageId, pageId, request);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }


    @Override
    public ResponseEntity<GeneralResponse<Object>> deletePageInPage(String parentPageId, String pageId) {
        pageService.deletePageInPage(parentPageId, pageId);
        return responseFactory.response(ResponseStatusEnum.SUCCESS);
    }

    @Override
    public ResponseEntity<GeneralResponse<GetPageDetailResponse>> getPageDetail(String pageId) {
        return responseFactory.response(pageService.getPageDetail(pageId));
    }
}
