package vn.edu.fpt.document.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.document.entity.Activity;
import vn.edu.fpt.document.entity._Page;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetPageResponse implements Serializable {

    private static final long serialVersionUID = -2553833365666515519L;
    private String pageId;
    private String title;
}
