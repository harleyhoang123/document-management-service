package vn.edu.fpt.document.service;

import vn.edu.fpt.document.dto.event.CreateDocumentEvent;
import vn.edu.fpt.document.dto.request.document.UpdateDocumentRequest;
import vn.edu.fpt.document.dto.response.document.CreateDocumentResponse;
import vn.edu.fpt.document.dto.response.document.GetDocumentDetailResponse;

public interface DocumentService {
    CreateDocumentResponse createDocument(CreateDocumentEvent event);

    void deleteDocument(String documentId);

    GetDocumentDetailResponse getDocumentDetail(String documentId);
}
