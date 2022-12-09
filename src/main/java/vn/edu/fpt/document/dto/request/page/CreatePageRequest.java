package vn.edu.fpt.document.dto.request.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreatePageRequest implements Serializable {

    private static final long serialVersionUID = -1137974213058545840L;
    private String title;
    private String content;
}
