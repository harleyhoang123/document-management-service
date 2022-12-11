package vn.edu.fpt.document.dto.response.page;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.fpt.document.dto.common.ActivityResponse;
import vn.edu.fpt.document.dto.common.AuditableResponse;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class GetPageDetailResponse extends AuditableResponse implements Serializable {

    private static final long serialVersionUID = -3091734851025243103L;
    private String pageId;
    private String title;
    private String content;
    private Integer version;
    private List<ActivityResponse> activities;
}
