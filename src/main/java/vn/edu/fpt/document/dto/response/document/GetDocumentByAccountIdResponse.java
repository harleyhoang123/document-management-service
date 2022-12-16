package vn.edu.fpt.document.dto.response.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 11/12/2022 - 10:37
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetDocumentByAccountIdResponse implements Serializable {

    private static final long serialVersionUID = -4970399399493502453L;
    private String documentId;
    private String documentName;
    private String memberId;
}
