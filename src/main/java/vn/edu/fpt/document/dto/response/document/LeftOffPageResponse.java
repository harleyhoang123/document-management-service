package vn.edu.fpt.document.dto.response.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 11/12/2022 - 16:05
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LeftOffPageResponse implements Serializable {

    private static final long serialVersionUID = -2404098277929705624L;
    private String pageId;
    private String title;
    private LocalDateTime visitedDate;
}
