package vn.edu.fpt.document.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.document.dto.common.ActivityResponse;
import vn.edu.fpt.document.dto.common.AuditableResponse;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class GetPageDetailResponse extends AuditableResponse implements Serializable {

    private static final long serialVersionUID = -3091734851025243103L;
    private String pageId;
    private String title;
    private String content;
    private List<GetPageResponse> subPages;
    private List<ActivityResponse> activities;
}
