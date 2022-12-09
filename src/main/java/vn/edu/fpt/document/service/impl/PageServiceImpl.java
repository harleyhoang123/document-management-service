package vn.edu.fpt.document.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.fpt.document.constant.ResponseStatusEnum;
import vn.edu.fpt.document.dto.common.ActivityResponse;
import vn.edu.fpt.document.dto.request.page.CreatePageRequest;
import vn.edu.fpt.document.dto.request.page.UpdatePageRequest;
import vn.edu.fpt.document.dto.response.page.CreatePageResponse;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;
import vn.edu.fpt.document.dto.response.page.GetPageResponse;
import vn.edu.fpt.document.entity.Activity;
import vn.edu.fpt.document.entity._Document;
import vn.edu.fpt.document.entity._Page;
import vn.edu.fpt.document.exception.BusinessException;
import vn.edu.fpt.document.repository.DocumentRepository;
import vn.edu.fpt.document.repository.PageRepository;
import vn.edu.fpt.document.service.PageService;
import vn.edu.fpt.document.service.UserInfoService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageServiceImpl implements PageService {
    private final DocumentRepository documentRepository;
    private final PageRepository pageRepository;
    private final UserInfoService userInfoService;

    @Override
    public CreatePageResponse createPageInDocument(String documentId, CreatePageRequest request) {
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document ID not exist"));
        List<_Page> pages = document.getPages();
        if (pages.stream().anyMatch(m->m.getTitle().equals(request.getTitle()))){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page title already exist");
        }
        _Page page = _Page.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        try {
            page = pageRepository.save(page);
            log.info("Create page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create page in database: " + ex.getMessage());
        }
        pages.add(page);
        document.setPages(pages);
        try {
            documentRepository.save(document);
            log.info("Add page to list in document success");
        } catch (Exception ex) {
            throw new BusinessException("Can't add page to list in document in database: " + ex.getMessage());
        }
        return CreatePageResponse.builder()
                .pageId(page.getPageId())
                .build();
    }

    @Override
    public CreatePageResponse createPageInPage(String pageId, CreatePageRequest request) {
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> pages = page.getPages();
        if (pages.stream().anyMatch(m->m.getTitle().equals(request.getTitle()))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Child page name already exist in parent page");
        }
        _Page childPage = _Page.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        try {
            page = pageRepository.save(childPage);
            log.info("Create child page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create child page in database: " + ex.getMessage());
        }
        pages.add(childPage);
        page.setPages(pages);
        try {
            pageRepository.save(page);
            log.info("Add child page to list in page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't add child page to list in page in database: " + ex.getMessage());
        }
        return CreatePageResponse.builder()
                .pageId(childPage.getPageId())
                .build();
    }

    @Override
    public void updatePageInDocument(String documentId, String pageId, UpdatePageRequest request) {
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> pages = document.getPages();
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        if (Objects.nonNull(request.getTitle())) {
            if (pages.stream().anyMatch(m->m.getTitle().equals(request.getTitle()))) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page name already exist in document");
            }
            page.setTitle(request.getTitle());
        }
        if (Objects.nonNull(request.getContent())) {
            page.setContent(request.getContent());
        }
        try {
            pageRepository.save(page);
            log.info("Update page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update page in database: " + ex.getMessage());
        }
    }

    @Override
    public void updatePageInPage(String parentPageId, String childPageId, UpdatePageRequest request) {
        _Page parentPage = pageRepository.findById(parentPageId)
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> childPages = parentPage.getPages();
        _Page childPage = pageRepository.findById(childPageId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Child Page ID not exist"));
        if (Objects.nonNull(request.getTitle())) {
            if (childPages.stream().anyMatch(m->m.getTitle().equals(request.getTitle()))) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Child page name already exist in document");
            }
            childPage.setTitle(request.getTitle());
        }
        if (Objects.nonNull(request.getContent())) {
            childPage.setContent(request.getContent());
        }
        try {
            pageRepository.save(childPage);
            log.info("Update page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update page in database: " + ex.getMessage());
        }
    }

    @Override
    public void deletePageInDocument(String documentId, String pageId) {
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> pages = document.getPages();
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        if (pages.stream().noneMatch(m->m.getPageId().equals(pageId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist in Document");
        }
        try {
            pageRepository.deleteById(pageId);
            log.info("Delete page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete page in database: " + ex.getMessage());
        }
        pages.removeIf(m->m.getPageId().equals(pageId));
        document.setPages(pages);
        try {
            documentRepository.save(document);
            log.info("Remove page from list in document success");
        } catch (Exception ex) {
            throw new BusinessException("Can't remove page from list in document in database: " + ex.getMessage());
        }
        if (page.getPages().stream().count() > 0) {
            for (_Page p : page.getPages() ) {
                deletePageInPage(pageId, p.getPageId());
            }
        }
    }

    @Override
    public void deletePageInPage(String parentPageId, String childPageId) {
        _Page parentPage = pageRepository.findById(parentPageId)
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> childPages = parentPage.getPages();
        _Page childPage = pageRepository.findById(childPageId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Child Page ID not exist"));
        if (childPages.stream().noneMatch(m->m.getPageId().equals(childPageId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Child page ID not exist in parent page");
        }
        try {
            pageRepository.deleteById(childPageId);
            log.info("Delete child page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete child page in database: " + ex.getMessage());
        }
        childPages.removeIf(m->m.getPageId().equals(childPageId));
        parentPage.setPages(childPages);
        try {
            pageRepository.save(parentPage);
            log.info("Remove page from list in parent page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't remove page from list in parent page in database: " + ex.getMessage());
        }
        if (childPage.getPages().stream().count() > 0) {
            for (_Page p : childPage.getPages() ) {
                deletePageInPage(childPageId, p.getPageId());
            }
        }
    }

    @Override
    public GetPageDetailResponse getPageDetail(String pageId) {
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        GetPageDetailResponse getPageDetailResponse = GetPageDetailResponse.builder()
                .pageId(pageId)
                .title(page.getTitle())
                .content(page.getContent())
                .pages(page.getPages().stream().map(this::convertPageToGetPageResponse).collect(Collectors.toList()))
                .activities(page.getActivities().stream().map(this::convertActivityToActivityResponse).collect(Collectors.toList()))
                .build();
        return getPageDetailResponse;
    }

    private GetPageResponse convertPageToGetPageResponse(_Page page) {
        return GetPageResponse.builder()
                .pageId(page.getPageId())
                .title(page.getTitle())
                .build();
    }

    private ActivityResponse convertActivityToActivityResponse(Activity activity) {

        return ActivityResponse.builder()
                .userInfo(userInfoService.getUserInfo(activity.getChangeBy().getAccountId()))
                .edited(activity.getChangedData())
                .createdDate(activity.getChangedDate())
                .activityType(activity.getType())
                .build();
    }
}
