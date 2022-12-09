package vn.edu.fpt.document.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.document.constant.ActivityTypeEnum;
import vn.edu.fpt.document.dto.cache.UserInfo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 15:01
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ActivityResponse implements Serializable {

    private static final long serialVersionUID = 3521089807347974621L;
    private UserInfo userInfo;
    private String edited;
    private ActivityTypeEnum activityType;
    private LocalDateTime createdDate;
}
