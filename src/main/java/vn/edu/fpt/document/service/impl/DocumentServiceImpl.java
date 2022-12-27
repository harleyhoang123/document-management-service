package vn.edu.fpt.document.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import vn.edu.fpt.document.constant.ActivityTypeEnum;
import vn.edu.fpt.document.constant.DocumentRoleEnum;
import vn.edu.fpt.document.constant.ResponseStatusEnum;
import vn.edu.fpt.document.dto.cache.UserInfo;
import vn.edu.fpt.document.dto.common.ActivityResponse;
import vn.edu.fpt.document.dto.common.PageableResponse;
import vn.edu.fpt.document.dto.common.UserInfoResponse;
import vn.edu.fpt.document.dto.event.GenerateProjectAppEvent;
import vn.edu.fpt.document.dto.event.ModifyMembersToWorkspaceEvent;
import vn.edu.fpt.document.dto.request.document.GetMemberIdResponse;
import vn.edu.fpt.document.dto.response.document.*;
import vn.edu.fpt.document.dto.response.page.GetPageDetailResponse;
import vn.edu.fpt.document.dto.response.page.GetPageResponse;
import vn.edu.fpt.document.entity.*;
import vn.edu.fpt.document.exception.BusinessException;
import vn.edu.fpt.document.repository.*;
import vn.edu.fpt.document.service.DocumentService;
import vn.edu.fpt.document.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final MemberInfoRepository memberInfoRepository;
    private final DocumentRepository documentRepository;
    private final ContentRepository contentRepository;
    private final PageRepository pageRepository;
    private final ActivityRepository activityRepository;
    private final UserInfoService userInfoService;
    private final MongoTemplate mongoTemplate;

    private static final String WELCOME_CONTENT = "<div style=\"background-color:rgb(227, 252, 239);\"><h3 style=\"text-align:start\"><strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">Welcome to your new space</span></span></strong></h3><p style=\"text-align:start\"><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">Use it to create something wonderful.</span></span></p><p style=\"text-align:start\"><strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">To start, you might want to:</span></span></strong></p><ul><li><p><strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">Customise this overview</span></span></strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">&nbsp;using the&nbsp;</span></span><strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">edit icon</span></span></strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">&nbsp;at the top right of this page.</span></span></p></li><li><p><strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">Create a new page</span></span></strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">&nbsp;by clicking the&nbsp;</span></span><strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">+</span></span></strong><span style=\"color:rgb(23, 43, 77);\"><span style=\"background-color:rgb(227, 252, 239);\">&nbsp;in the space sidebar, then go ahead and fill it with plans, ideas, or anything else your heart desires.</span></span></p></li></ul></div>";


    @Override
    public CreateDocumentResponse createDocument(GenerateProjectAppEvent event) {
        MemberInfo memberInfo = MemberInfo.builder()
                .accountId(event.getAccountId())
                .role(DocumentRoleEnum.OWNER.getRole())
                .build();
        try {
            memberInfo = memberInfoRepository.save(memberInfo);
            log.info("Create memberInfo success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create memberInfo in database: " + ex.getMessage());
        }
        List<MemberInfo> memberInfoList = new ArrayList<>();
        memberInfoList.add(memberInfo);
        Activity activity = Activity.builder()
                .type(ActivityTypeEnum.HISTORY)
                .build();
        try {
            activity = activityRepository.save(activity);
        }catch (Exception ex){
            throw new BusinessException("Can't save activity to database: "+ ex.getMessage());
        }
        _Content content = _Content.builder()
                .content(WELCOME_CONTENT)
                .version(1)
                .build();
        try {
            content = contentRepository.save(content);
        }catch (Exception ex){
            throw new BusinessException("Can't save content to database: "+ ex.getMessage());
        }
        _Page overview = _Page.builder()
                .title(event.getProjectName())
                .contents(List.of(content))
                .currentVersion(1)
                .highestVersion(1)
                .activities(List.of(activity))
                .build();
        try {
            overview = pageRepository.save(overview);
        }catch (Exception ex){
            throw new BusinessException("Can't save page repository in database: "+ ex.getMessage());
        }
        _Document document = _Document.builder()
                .documentId(event.getProjectId())
                .overview(overview)
                .members(memberInfoList)
                .documentName(event.getProjectName())
                .build();
        try {
            document = documentRepository.save(document);
            log.info("Create document success");
        } catch (Exception ex) {
            throw new BusinessException("Can't create document in database: " + ex.getMessage());
        }
        return CreateDocumentResponse.builder()
                .documentId(document.getDocumentId())
                .build();
    }

    @Override
    public void deleteDocument(String documentId) {
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document ID not exist"));

        List<_Page> pages = document.getPages();
        List<MemberInfo> memberInfos = document.getMembers();

        try {
            documentRepository.deleteById(documentId);
            log.info("Delete document success");
        } catch (Exception ex) {
            throw new BusinessException("Can't delete document in database: " + ex.getMessage());
        }

        for (_Page p : pages) {
            try {
                pageRepository.deleteById(p.getPageId());
                log.info("Delete page success");
            } catch (Exception ex) {
                throw new BusinessException("Can't delete page in database: " + ex.getMessage());
            }
        }
        for (MemberInfo m : memberInfos) {
            try {
                memberInfoRepository.deleteById(m.getMemberId());
                log.info("Delete memberInfo success");
            } catch (Exception ex) {
                throw new BusinessException("Can't delete memberInfo in database: " + ex.getMessage());
            }
        }
    }

    @Override
    public PageableResponse<GetDocumentByAccountIdResponse> getDocumentByAccountId(String accountId) {
        if (!ObjectId.isValid(accountId)) {
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account Id invalid");
        }
        List<MemberInfo> memberInfos = memberInfoRepository.findAllByAccountId(accountId);
        List<ObjectId> memberIds = memberInfos.stream().map(MemberInfo::getMemberId).map(ObjectId::new).collect(Collectors.toList());

        Query query = new Query();
        query.addCriteria(Criteria.where("members.$id").in(memberIds));
        List<_Document> documents = mongoTemplate.find(query, _Document.class);
        List<GetDocumentByAccountIdResponse> getDocumentByAccountIdResponses = documents.stream().map(this::convertDocumentToGetDocumentByAccountIdResponse).collect(Collectors.toList());

        return new PageableResponse<>(getDocumentByAccountIdResponses);
    }

    @Override
    public GetPageDetailResponse getPageOverview(String documentId) {
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new BusinessException(""));
        _Page overview = document.getOverview();
        _Content currentContent = overview.getContents().stream().filter(m -> m.getVersion().equals(overview.getCurrentVersion())).findFirst()
                .orElseThrow(() -> new BusinessException(""));
        return GetPageDetailResponse.builder()
                .pageId(overview.getPageId())
                .title(overview.getTitle())
                .version(overview.getCurrentVersion())
                .content(currentContent.getContent())
                .activities(overview.getActivities().stream().map(this::convertActivityToActivityResponse).collect(Collectors.toList()))
                .createdDate(overview.getCreatedDate())
                .lastModifiedDate(overview.getLastModifiedDate())
                .build();
    }

    private ActivityResponse convertActivityToActivityResponse(Activity activity) {
        MemberInfo memberInfo = activity.getChangeBy();
        return ActivityResponse.builder()
                .userInfo(memberInfo != null ? UserInfoResponse.builder()
                        .accountId(memberInfo.getAccountId())
                        .memberId(memberInfo.getMemberId())
                        .userInfo(userInfoService.getUserInfo(memberInfo.getAccountId()))
                        .build(): null)
                .edited(activity.getChangedData())
                .createdDate(activity.getChangedDate())
                .type(activity.getType())
                .build();
    }

    @Override
    public List<GetPageOfDocumentResponse> getPageOfDocument(String documentId) {
        if(!ObjectId.isValid(documentId)){
            throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document Id invalid");
        }
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document Id not exist"));
        return document.getPages().stream().map(this::convertPageToGetPageOfDocumentResponse).collect(Collectors.toList());
    }

    private GetPageOfDocumentResponse convertPageToGetPageOfDocumentResponse(_Page page){
        UserInfo createdBy = userInfoService.getUserInfo(page.getCreatedBy());
        return GetPageOfDocumentResponse.builder()
                .pageId(page.getPageId())
                .title(page.getTitle())
                .pages(page.getPages().stream().map(this::convertPageToGetPageOfDocumentResponse).collect(Collectors.toList()))
                .createdBy(createdBy == null ? null : createdBy.getUsername())
                .lastModifiedDate(page.getLastModifiedDate())
                .build();
    }

    private GetDocumentByAccountIdResponse convertDocumentToGetDocumentByAccountIdResponse(_Document document) {
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername)
                .orElseThrow(() -> new BusinessException("Can't get account id from token"));
        MemberInfo memberInfo = document.getMembers().stream().filter(v -> v.getAccountId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Account Id not in document"));
        return GetDocumentByAccountIdResponse.builder()
                .documentId(document.getDocumentId())
                .documentName(document.getDocumentName())
                .memberId(memberInfo.getMemberId())
                .build();
    }

    @Override
    public GetDocumentDetailResponse getDocumentDetail(String documentId) {
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document ID not exist"));
        List<MemberInfo> memberInfos = document.getMembers();
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).get();
        MemberInfo memberInfo = memberInfos.stream().filter(v -> v.getAccountId().equals(accountId)).findFirst()
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account not in document"));
        List<Visited> visited = memberInfo.getVisited();

        return GetDocumentDetailResponse.builder()
                .documentId(document.getDocumentId())
                .documentName(document.getDocumentName())
                .memberInfo(UserInfoResponse.builder()
                        .accountId(accountId)
                        .memberId(memberInfo.getMemberId())
                        .userInfo(userInfoService.getUserInfo(accountId))
                        .build())
                .leftOff(visited.stream().map(this::convertVisitedToDocumentDetailResponse).collect(Collectors.toList()))
                .discover(new ArrayList<>())
                .build();
    }


    @Override
    public GetMemberIdResponse getMemberId(String documentId) {
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).orElseThrow();

        _Document document = documentRepository.findById(documentId).orElseThrow();
        MemberInfo memberInfo = document.getMembers().stream().filter(v -> v.getAccountId().equals(accountId)).findAny().orElseThrow();
        return GetMemberIdResponse.builder()
                .memberId(memberInfo.getMemberId())
                .build();
    }

    private LeftOffPageResponse convertVisitedToDocumentDetailResponse(Visited visited) {
        _Page page = visited.getPage();
        return LeftOffPageResponse.builder()
                .pageId(page.getPageId())
                .title(page.getTitle())
                .visitedDate(visited.getVisitedTime())
                .build();
    }

    private UserInfoResponse convertMemberInfoToUserInfoResponse(MemberInfo memberInfo) {
        if (memberInfo == null) {
            return null;
        } else {
            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .accountId(memberInfo.getAccountId())
                    .memberId(memberInfo.getMemberId())
                    .userInfo(userInfoService.getUserInfo(memberInfo.getAccountId()))
                    .build();
            return userInfoResponse;
        }
    }

    private GetPageResponse convertPageToGetPageResponse(_Page page) {
        return GetPageResponse.builder()
                .pageId(page.getPageId())
                .title(page.getTitle())
                .build();
    }

    @Override
    public void modifyMembersToWorkspace(ModifyMembersToWorkspaceEvent event) {
        _Document document = documentRepository.findById(event.getWorkspaceId())
                .orElseThrow();
        List<MemberInfo> memberInfos = document.getMembers();
        if (event.getType().equals("ADD")) {
            if (memberInfos.stream().anyMatch(m -> m.getAccountId().equals(event.getAccountId()))) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID is already exist in Workspace");
            }
            MemberInfo memberInfo = MemberInfo.builder()
                    .accountId(event.getAccountId())
                    .role(DocumentRoleEnum.MEMBER.getRole())
                    .build();
            try {
                memberInfo = memberInfoRepository.save(memberInfo);
                log.info("Create memberInfo success: {}", memberInfo);
            } catch (Exception ex) {
                throw new BusinessException("Can't create memberInfo to database: " + ex.getMessage());
            }

            memberInfos.add(memberInfo);
            document.setMembers(memberInfos);
            try {
                documentRepository.save(document);
                log.info("Add member to workspace success: {}", memberInfo);
            } catch (Exception ex) {
                throw new BusinessException("Can't add member to workspace in database: " + ex.getMessage());
            }
        } else {
            MemberInfo memberInfo = memberInfos.stream().filter(m -> m.getAccountId().equals(event.getAccountId())).findFirst().get();
            if (memberInfo == null) {
                throw new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account ID is not exist in Workspace");
            }
            memberInfos.removeIf(m -> m.getAccountId().equals(event.getAccountId()));
            document.setMembers(memberInfos);
            try {
                documentRepository.save(document);
                log.info("Delete member from workspace success: {}", memberInfo);
            } catch (Exception ex) {
                throw new BusinessException("Can't delete member from workspace: " + ex.getMessage());
            }

            try {
                memberInfoRepository.deleteById(memberInfo.getAccountId());
                log.info("Delete member in database success: {}", memberInfo);
            } catch (Exception ex) {
                throw new BusinessException("Can't delete member in database: " + ex.getMessage());
            }
        }
    }
}
