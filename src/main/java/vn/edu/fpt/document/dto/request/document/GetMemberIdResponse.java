package vn.edu.fpt.document.dto.request.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 25/12/2022 - 04:36
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetMemberIdResponse implements Serializable {

    private static final long serialVersionUID = 2229911818861954931L;
    private String memberId;
}
