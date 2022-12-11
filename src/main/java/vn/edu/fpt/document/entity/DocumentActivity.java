package vn.edu.fpt.document.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.document.entity.common.Auditor;

import javax.persistence.EntityListeners;
import javax.persistence.Id;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 11/12/2022 - 11:33
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "document_activities")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DocumentActivity  {

    private static final long serialVersionUID = 2444621166817071512L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String activityId;
    @Field(name = "document_id", targetType = FieldType.OBJECT_ID)
    private String documentId;
    @Field(name = "page_id", targetType = FieldType.OBJECT_ID)
    private String pageId;
    @Field(name = "changed_data")
    private String changedData;
    @Field(name = "created_by")
    @CreatedBy
    private String createdBy;
    @Field(name = "created_date")
    @CreatedDate
    private String createdDate;

}
