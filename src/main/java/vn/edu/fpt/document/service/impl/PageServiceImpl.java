package vn.edu.fpt.document.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.edu.fpt.document.constant.ActivityTypeEnum;
import vn.edu.fpt.document.constant.ResponseStatusEnum;
import vn.edu.fpt.document.dto.common.ActivityResponse;
import vn.edu.fpt.document.dto.common.PageableResponse;
import vn.edu.fpt.document.dto.common.UserInfoResponse;
import vn.edu.fpt.document.dto.request.page.CreatePageRequest;
import vn.edu.fpt.document.dto.request.page.UpdatePageRequest;
import vn.edu.fpt.document.dto.response.page.CreatePageResponse;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;
import vn.edu.fpt.document.dto.response.page.GetPageVersionsResponse;
import vn.edu.fpt.document.entity.*;
import vn.edu.fpt.document.exception.BusinessException;
import vn.edu.fpt.document.repository.*;
import vn.edu.fpt.document.service.PageService;
import vn.edu.fpt.document.service.UserInfoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageServiceImpl implements PageService {
    private final DocumentRepository documentRepository;
    private final PageRepository pageRepository;
    private final UserInfoService userInfoService;
    private final ContentRepository contentRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final ActivityRepository activityRepository;
    private final VisitedRepository visitedRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public CreatePageResponse createPageInDocument(String documentId, CreatePageRequest request) {
        if (!ObjectId.isValid(documentId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document Id invalid in create page in document: " + documentId);
        }
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document Id not exist in create page in document: " + documentId));
        List<_Page> pages = document.getPages();
        if (pages.stream().anyMatch(m -> m.getTitle().equals(request.getTitle()))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page title already exist");
        }
        _Content content = _Content.builder()
                .content(request.getContent())
                .version(1)
                .createdBy(request.getMemberId())
                .createdDate(LocalDateTime.now())
                .build();
        try {
            content = contentRepository.save(content);
            log.info("Create content success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create content in database: " + ex.getMessage());
        }
        List<_Content> contents = new ArrayList<>();
        contents.add(content);
        _Page page = _Page.builder()
                .title(request.getTitle())
                .contents(contents)
                .currentVersion(1)
                .highestVersion(1)
                .build();
        try {
            page = pageRepository.save(page);
            log.info("Create page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create page in database: " + ex.getMessage());
        }
        pages.add(page);
        document.setPages(pages);

        MemberInfo memberInfo = memberInfoRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("created the document")
                .build();
        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        List<Activity> activities = page.getActivities();
        activities.add(activity);
        page.setActivities(activities);

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
        if (!ObjectId.isValid(pageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID invalid");
        }
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> pages = page.getPages();
        if (pages.stream().anyMatch(m -> m.getTitle().equals(request.getTitle()))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Child page name already exist in parent page");
        }
        _Content content = _Content.builder()
                .content(request.getContent())
                .version(1)
                .build();
        try {
            content = contentRepository.save(content);
            log.info("Create content success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create content in database: " + ex.getMessage());
        }
        List<_Content> contents = new ArrayList<>();
        contents.add(content);
        _Page childPage = _Page.builder()
                .title(request.getTitle())
                .contents(contents)
                .currentVersion(1)
                .highestVersion(1)
                .build();
        try {
            childPage = pageRepository.save(childPage);
            log.info("Create child page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create child page in database: " + ex.getMessage());
        }
        pages.add(childPage);
        page.setPages(pages);

        MemberInfo memberInfo = memberInfoRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("created the page")
                .build();
        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        List<Activity> activities = page.getActivities();
        activities.add(activity);
        page.setActivities(activities);
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
        if (!ObjectId.isValid(documentId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document ID invalid");
        }
        if (!ObjectId.isValid(pageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID invalid");
        }
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> pages = document.getPages();
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        if(page.isLocked()){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page is locked");
        }
        if (Objects.nonNull(request.getTitle())) {
            if (pages.stream().anyMatch(m -> m.getTitle().equals(request.getTitle()))) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page name already exist in document");
            }
            page.setTitle(request.getTitle());
        }
        if (Objects.nonNull(request.getContent())) {
            List<_Content> contents = page.getContents();
            _Content content = contents.stream().filter(m -> m.getContent().equals(request.getContent())).findFirst().get();
            if (content == null) {
                page.setCurrentVersion(content.getVersion());
            } else {
                Integer highestVersion = page.getHighestVersion();
                _Content newContent = _Content.builder()
                        .content(request.getContent())
                        .version(++highestVersion)
                        .build();
                try {
                    newContent = contentRepository.save(newContent);
                    log.info("Create content success");
                } catch (Exception ex) {
                    throw new BusinessException("Can't create content in database: " + ex.getMessage());
                }
                contents.add(newContent);
                page.setContents(contents);
                page.setHighestVersion(highestVersion);
            }

        }
        try {
            pageRepository.save(page);
            log.info("Update page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update page in database: " + ex.getMessage());
        }
        MemberInfo memberInfo = memberInfoRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        List<MemberInfo> memberInfos = document.getMembers();
        if (memberInfos.stream().noneMatch(m -> m.getMemberId().equals(request.getMemberId()))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist in document");
        }
        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("Updated page")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
    }

    @Override
    public void updatePageInPage(String pageId, UpdatePageRequest request) {

        if (!ObjectId.isValid(pageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page Id invalid");
        }

        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        if(page.isLocked()){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page is locked");
        }
        MemberInfo memberInfo = memberInfoRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        if (Objects.nonNull(request.getTitle())) {
            page.setTitle(request.getTitle());
        }
        if (Objects.nonNull(request.getContent())) {
            List<_Content> contents = page.getContents();
            Optional<_Content> content = contents.stream().filter(m -> m.getVersion().equals(page.getCurrentVersion())).findFirst();
            if (content.isPresent() && content.get().getContent().equals(request.getContent())) {
                page.setCurrentVersion(content.get().getVersion());
            } else {
                Integer highestVersion = page.getHighestVersion();
                _Content newContent = _Content.builder()
                        .content(request.getContent())
                        .version(++highestVersion)
                        .createdBy(request.getMemberId())
                        .createdDate(LocalDateTime.now())
                        .build();
                try {
                    newContent = contentRepository.save(newContent);
                    log.info("Create content success");
                } catch (Exception ex) {
                    throw new BusinessException("Can't create content in database: " + ex.getMessage());
                }
                contents.add(newContent);
                page.setContents(contents);
                page.setHighestVersion(highestVersion);
                page.setCurrentVersion(highestVersion);
            }
        }


        Activity activity = Activity.builder()
                .changeBy(memberInfo)
                .type(ActivityTypeEnum.HISTORY)
                .changedData("update the page")
                .build();

        try {
            activity = activityRepository.save(activity);
            log.info("Create activity success");
        } catch (Exception ex) {
            throw new BusinessException("Can't save activity in database: " + ex.getMessage());
        }
        List<Activity> activities = page.getActivities();
        activities.add(activity);
        page.setActivities(activities);

        try {
            pageRepository.save(page);
            log.info("Update page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update page in database: " + ex.getMessage());
        }

    }

    @Override
    public void deletePage(String pageId){
        if(!ObjectId.isValid(pageId)){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page Id in delete page invalid: "+ pageId);
        }
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page Id not exist when delete page: "+ pageId));
        try {
            pageRepository.delete(page);
        }catch (Exception ex){
            throw new BusinessException("Can't delete page in database: "+ ex.getMessage());
        }
        deleteSubPage(page.getPages());
        deleteParentPage(pageId);
        deleteInDocument(pageId);
        deleteVisited(pageId);
    }

    private void deleteParentPage(String pageId){
        Query query = new Query();
        query.addCriteria(Criteria.where("pages.$id").in(List.of(new ObjectId(pageId))));
        List<_Page> pages = mongoTemplate.find(query, _Page.class);
        if(!pages.isEmpty()){
            pages.forEach(v -> {
                List<_Page> listSubPage = v.getPages();
                v.setPages(listSubPage.stream().filter(Objects::nonNull).collect(Collectors.toList()));
                try {
                    pageRepository.save(v);
                }catch (Exception ex){
                    throw new BusinessException("Can't delete null pages when delete parent page: "+ ex.getMessage());
                }
            });
        }

    }
    private void deleteVisited(String pageId){
        Query query = new Query();
        query.addCriteria(Criteria.where("page").is(new ObjectId(pageId)));
        List<Visited> visited = mongoTemplate.find(query, Visited.class);
        try {
            visitedRepository.deleteAll(visited);
        }catch (Exception ex){
            throw new BusinessException("Can't delete visited when delete page: "+ ex.getMessage());
        }
    }

    private void deleteSubPage(List<_Page> pages){
        pages.forEach(v -> deletePage(v.getPageId()));
    }

    private void deleteInDocument(String pageId){
        log.info("DELETE PARENT PAGE OF: "+ pageId);
        Query query = new Query();
        query.addCriteria(Criteria.where("pages.$id").in(List.of(new ObjectId(pageId))));
        List<_Document> documents = mongoTemplate.find(query, _Document.class);
        if(!documents.isEmpty()){
            try {
                _Document document = documents.get(0);
                List<_Page> pages = document.getPages().stream().filter(v -> Objects.nonNull(v)).collect(Collectors.toList());
                document.setPages(pages);
                documentRepository.save(document);
            }catch (Exception ex){
                throw new BusinessException("Can't delete parent page in database");
            }
        }
    }

    @Override
    public void deletePageInDocument(String documentId, String pageId) {
        if (!ObjectId.isValid(documentId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document ID invalid");
        }
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> pages = document.getPages();
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        if (pages.stream().noneMatch(m -> m.getPageId().equals(pageId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist in Document");
        }
        try {
            pageRepository.deleteById(pageId);
            log.info("Delete page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete page in database: " + ex.getMessage());
        }
        pages.removeIf(m -> m.getPageId().equals(pageId));
        document.setPages(pages);
        try {
            documentRepository.save(document);
            log.info("Remove page from list in document success");
        } catch (Exception ex) {
            throw new BusinessException("Can't remove page from list in document in database: " + ex.getMessage());
        }
        if (page.getPages().stream().count() > 0) {
            for (_Page p : page.getPages()) {
                deletePageInPage(pageId, p.getPageId());
            }
        }
    }

    @Override
    public void deletePageInPage(String parentPageId, String pageId) {
        if (!ObjectId.isValid(parentPageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "ParentPageId invalid");
        }
        if (!ObjectId.isValid(pageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "PageId invalid");
        }
        _Page parentPage = pageRepository.findById(parentPageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Parent page ID not exist"));
        List<_Page> childPages = parentPage.getPages();
        _Page childPage = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Child Page ID not exist"));
        if (childPages.stream().noneMatch(m -> m.getPageId().equals(pageId))) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Child page ID not exist in parent page");
        }
        try {
            pageRepository.deleteById(pageId);
            log.info("Delete child page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete child page in database: " + ex.getMessage());
        }
        childPages.removeIf(m -> m.getPageId().equals(pageId));
        parentPage.setPages(childPages);
        try {
            pageRepository.save(parentPage);
            log.info("Remove page from list in parent page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't remove page from list in parent page in database: " + ex.getMessage());
        }
        if (childPage.getPages().stream().count() > 0) {
            for (_Page p : childPage.getPages()) {
                deletePageInPage(pageId, p.getPageId());
            }
        }
    }

    @Override
    public GetPageDetailResponse getPageDetail(String pageId, String memberId) {
        if (!ObjectId.isValid(pageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "PageId invalid");
        }
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        MemberInfo memberInfo = memberInfoRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        List<_Content> contents = page.getContents();
        _Content currentContent = contents.stream().filter(m -> m.getVersion().equals(page.getCurrentVersion())).findFirst()
                .orElseThrow(() -> new BusinessException("Current content not exist"));
        GetPageDetailResponse getPageDetailResponse = GetPageDetailResponse.builder()
                .pageId(pageId)
                .title(page.getTitle())
                .version(page.getCurrentVersion())
                .content(currentContent.getContent())
                .locked(page.isLocked())
                .activities(page.getActivities().stream().map(this::convertActivityToActivityResponse).collect(Collectors.toList()))
                .createdBy(convertMemberInfoToUserInfoResponse(memberInfo))
                .createdDate(page.getCreatedDate())
                .lastModifiedBy(convertMemberInfoToUserInfoResponse(memberInfo))
                .lastModifiedDate(page.getLastModifiedDate())
                .build();

        return getPageDetailResponse;
    }

    private UserInfoResponse convertMemberInfoToUserInfoResponse(MemberInfo memberInfo) {
        return UserInfoResponse.builder()
                .memberId(memberInfo.getMemberId())
                .accountId(memberInfo.getAccountId())
                .userInfo(userInfoService.getUserInfo(memberInfo.getAccountId()))
                .build();
    }

    private ActivityResponse convertActivityToActivityResponse(Activity activity) {
        MemberInfo memberInfo = activity.getChangeBy();
        if(Objects.isNull(memberInfo)){
            return null;
        }
        return ActivityResponse.builder()
                .userInfo(UserInfoResponse.builder()
                        .accountId(memberInfo.getAccountId())
                        .memberId(memberInfo.getMemberId())
                        .userInfo(userInfoService.getUserInfo(memberInfo.getAccountId()))
                        .build())
                .edited(activity.getChangedData())
                .createdDate(activity.getChangedDate())
                .type(activity.getType())
                .build();
    }



    @Override
    public void lockPage(String pageId) {
        if (!ObjectId.isValid(pageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page Id invalid");
        }

        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        if (page.isLocked()) {
            page.setLocked(true);
        }else{
            page.setLocked(false);
        }
        try {
            pageRepository.save(page);
            log.info("Close page success");
        } catch (Exception ex) {
            throw new BusinessException("Can't update page in database: " + ex.getMessage());
        }
    }


    @Override
    public PageableResponse<GetPageVersionsResponse> getPageVersions(String pageId) {
        if (!ObjectId.isValid(pageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page Id invalid");
        }
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        List<GetPageVersionsResponse> getPageVersionsResponses = page.getContents().stream().map((this::convertContentToGetPageVersionsResponse)).collect(Collectors.toList());
        return new PageableResponse<> (getPageVersionsResponses);
    }

    private GetPageVersionsResponse convertContentToGetPageVersionsResponse(_Content content) {
        MemberInfo memberInfo = memberInfoRepository.findById(content.getCreatedBy())
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Member info not exist"));
        return GetPageVersionsResponse.builder()
                .contentId(content.getContentId())
                .version(content.getVersion())
                .createdBy(convertMemberInfoToUserInfoResponse(memberInfo))
                .createdDate(content.getCreatedDate())
                .build();
    }

    public void revertVersion(String pageId, String contentId) {
        if (!ObjectId.isValid(pageId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page Id invalid");
        }
        if (!ObjectId.isValid(contentId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Content Id invalid");
        }
        _Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Page ID not exist"));
        _Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Content ID not exist"));
        page.setCurrentVersion(content.getVersion());
    }
}
