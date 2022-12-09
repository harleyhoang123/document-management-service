package vn.edu.fpt.document.dto.request.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor

@Data
@Builder
public class UpdateDocumentRequest implements Serializable {

    private static final long serialVersionUID = -441842972788086817L;
}
