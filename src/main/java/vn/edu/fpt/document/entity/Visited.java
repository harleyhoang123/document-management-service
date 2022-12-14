package vn.edu.fpt.document.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.persistence.EntityListeners;
import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "visited")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Visited implements Serializable{

    private static final long serialVersionUID = -1675617805660487289L;
    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    private String visitedId;
    @Field(name = "page")
    @DBRef(lazy = true)
    private _Page page;
    @Field(name = "visited_time")
    @CreatedDate
    private LocalDateTime visitedTime;
}
