package vn.edu.fpt.document.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import vn.edu.fpt.document.constant.ActivityTypeEnum;

import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:24
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Activity {

    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String activityId;
    @Field(name = "type", targetType = FieldType.STRING)
    private ActivityTypeEnum type;
    @Field(name = "change_by")
    @DBRef(lazy = true)
    private MemberInfo changeBy;
    @Field(name = "changed_data")
    private String changedData;
    @Field(name = "changed_date")
    @CreatedDate
    private LocalDateTime changedDate;

}
