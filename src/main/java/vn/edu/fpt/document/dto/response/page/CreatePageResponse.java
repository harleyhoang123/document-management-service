package vn.edu.fpt.document.dto.response.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreatePageResponse implements Serializable {

    private static final long serialVersionUID = 190288760617988367L;
    private String pageId;
}
