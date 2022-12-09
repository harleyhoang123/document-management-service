package vn.edu.fpt.document.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.document.dto.common.ActivityResponse;
import vn.edu.fpt.document.entity.Activity;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetPageDetailResponse implements Serializable {

    private static final long serialVersionUID = -3091734851025243103L;
    private String pageId;
    private String title;
    private String content;
    private List<GetPageResponse> pages;
    private List<ActivityResponse> activities;
}
