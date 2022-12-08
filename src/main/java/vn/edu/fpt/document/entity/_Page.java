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
    private String pageId;
    private String title;
    private String content;
    private List<_Page> pages;
    private List<Activity> activities;
}
