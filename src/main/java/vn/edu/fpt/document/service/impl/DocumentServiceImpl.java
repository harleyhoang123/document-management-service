package vn.edu.fpt.document.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.nist.KMACwithSHAKE128_params;
import org.springframework.stereotype.Service;
import vn.edu.fpt.document.constant.DocumentRoleEnum;
import vn.edu.fpt.document.constant.ResponseStatusEnum;
import vn.edu.fpt.document.dto.common.ActivityResponse;
import vn.edu.fpt.document.dto.common.UserInfoResponse;
import vn.edu.fpt.document.dto.event.CreateDocumentEvent;
import vn.edu.fpt.document.dto.request.document.UpdateDocumentRequest;
import vn.edu.fpt.document.dto.response.document.CreateDocumentResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentDetailResponse;
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
import vn.edu.fpt.document.service.PageService;
import vn.edu.fpt.document.service.UserInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final MemberInfoRepository memberInfoRepository;
    private final DocumentRepository documentRepository;

    private final PageRepository pageRepository;

    private final UserInfoService userInfoService;

    @Override
    public CreateDocumentResponse createDocument(CreateDocumentEvent event) {
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
        _Document document = _Document.builder()
                .documentId(event.getProjectId())
                .members(memberInfoList)
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
                .orElseThrow(()->new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document ID not exist"));

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
    public GetDocumentDetailResponse getDocumentDetail(String documentId) {
        _Document document = documentRepository.findById(documentId)
                .orElseThrow(()-> new BusinessException(ResponseStatusEnum.BAD_REQUEST, "Document ID not exist"));
        List<_Page> pages = document.getPages();
        List<MemberInfo> memberInfos = document.getMembers();

        return GetDocumentDetailResponse.builder()
                .documentId(documentId)
                .pages(pages.stream().map(this :: convertPageToGetPageResponse).collect(Collectors.toList()))
                .members(memberInfos.stream().map(this :: convertMemberInfoToUserInfoResponse).collect(Collectors.toList()))
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
