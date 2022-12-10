package vn.edu.fpt.document.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "pages")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class _Content {
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String contentId;
    @Field(name = "content")
    private String content;
    @Field(name = "version")
    private Integer version;
}
