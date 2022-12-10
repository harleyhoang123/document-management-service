package vn.edu.fpt.document.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
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
 * @created : 09/12/2022 - 01:09
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Document(collection = "pages")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class _Page extends Auditor {

    private static final long serialVersionUID = 6127066703246170527L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String pageId;
    @Field(name = "title")
    private String title;
    @Field(name = "content")
    @DBRef(lazy = true)
    private List<_Content> contents = new ArrayList<>();
    @Field(name = "current_version")
    private Integer currentVersion;
    @Field(name = "highest_version")
    private Integer highestVersion;
    @Field(name = "pages")
    @Builder.Default
    @DBRef(lazy = true)
    private List<_Page> pages = new ArrayList<>();
    @Field(name = "activities")
    @Builder.Default
    @DBRef(lazy = true)
    private List<Activity> activities = new ArrayList<>();

}
