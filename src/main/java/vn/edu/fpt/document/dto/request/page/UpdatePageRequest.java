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
public class UpdatePageRequest implements Serializable {

    private static final long serialVersionUID = 3786148247557706557L;
    private String memberId;
    private String title;
    private String content;
}
