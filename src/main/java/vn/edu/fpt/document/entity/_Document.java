package vn.edu.fpt.document.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.document.entity.common.Auditor;

import java.util.ArrayList;
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
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String documentId;
    @Field(name = "document_name")
    private String documentName;
    @Field(name = "overview")
    @DBRef(lazy = true)
    private _Page overview;
    @Field(name = "pages")
    @Builder.Default
    @DBRef(lazy = true)
    private List<_Page> pages = new ArrayList<>();
    @Field(name = "members")
    @Builder.Default
    @DBRef(lazy = true)
    private List<MemberInfo> members = new ArrayList<>();
}
