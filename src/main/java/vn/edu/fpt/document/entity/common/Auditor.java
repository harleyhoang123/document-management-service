package vn.edu.fpt.document.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Authentication Service
 * @created : 30/08/2022 - 19:36
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditor implements Serializable {

    private static final long serialVersionUID = -1270165536077836250L;
    @Field(name = "created_by")
    @CreatedBy
    private String createdBy;
    @Field(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;
    @Field(name = "last_modified_by")
    @LastModifiedBy
    private String lastModifiedBy;
    @Field(name = "last_modified_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
