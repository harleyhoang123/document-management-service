package vn.edu.fpt.document.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.edu.fpt.document.entity.common.Auditor;

import java.util.List;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 09/12/2022 - 00:41
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "documents")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class _Document extends Auditor {

    private static final long serialVersionUID = 5565765293444651695L;
    private String documentId;
    private List<_Page> pages;
    private List<MemberInfo> members;
}
