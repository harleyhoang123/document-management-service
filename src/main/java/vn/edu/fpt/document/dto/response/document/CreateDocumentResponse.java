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
public class CreateDocumentResponse implements Serializable {

    private static final long serialVersionUID = -3701663316271951939L;
    private String documentId;
}
