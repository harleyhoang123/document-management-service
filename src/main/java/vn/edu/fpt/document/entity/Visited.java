package vn.edu.fpt.document.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
public class Visited implements Serializable{

    private static final long serialVersionUID = -1675617805660487289L;
    private String pageId;
    private LocalDateTime visitedTime;
}
