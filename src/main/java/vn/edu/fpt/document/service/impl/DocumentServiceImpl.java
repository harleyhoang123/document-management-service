package vn.edu.fpt.document.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
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
import vn.edu.fpt.document.dto.common.PageableResponse;
import vn.edu.fpt.document.dto.common.UserInfoResponse;
import vn.edu.fpt.document.dto.event.GenerateProjectAppEvent;
import vn.edu.fpt.document.dto.response.document.CreateDocumentResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentByAccountIdResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentDetailResponse;
import vn.edu.fpt.document.dto.response.document.GetPageOfDocumentResponse;
import vn.edu.fpt.document.dto.response.page.GetPageResponse;
import vn.edu.fpt.document.entity.Activity;
import vn.edu.fpt.document.entity.MemberInfo;
import vn.edu.fpt.document.entity._Document;
import vn.edu.fpt.document.entity._Page;
import vn.edu.fpt.document.exception.BusinessException;
import vn.edu.fpt.document.repository.DocumentRepository;
import vn.edu.fpt.document.repository.MemberInfoRepository;
import vn.edu.fpt.document.repository.PageRepository;
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

    private final PageRepository pageRepository;

    private final UserInfoService userInfoService;
    private final MongoTemplate mongoTemplate;

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
                .type(ActivityTypeEnum.COMMENT)
                .build();
        _Document document = _Document.builder()
                .documentId(event.getProjectId())
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
        return GetDocumentByAccountIdResponse.builder()
                .documentId(document.getDocumentId())
                .documentName(document.getDocumentName())
                .build();
    }

    @Override
    public GetDocumentDetailResponse getDocumentDetail(String documentId) {
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document ID not exist"));
        List<_Page> pages = document.getPages();
        List<MemberInfo> memberInfos = document.getMembers();
        String accountId = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername).get();
        MemberInfo memberInfo = memberInfos.stream().filter(v -> v.getAccountId().equals(accountId)).findFirst()
                .orElseThrow(() -> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Account not in document"));
        return GetDocumentDetailResponse.builder()
                .memberId(memberInfo.getMemberId())
                .pages(pages.stream().map(this::convertPageToGetPageResponse).collect(Collectors.toList()))
                .members(memberInfos.stream().map(this::convertMemberInfoToUserInfoResponse).collect(Collectors.toList()))
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
}
