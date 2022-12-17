package vn.edu.fpt.document.dto.response.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetDocumentResponse implements Serializable {

    private static final long serialVersionUID = 3112167789601807296L;
    private String documentId;
    private String documentName;
}
