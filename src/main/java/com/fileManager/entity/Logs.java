package com.fileManager.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangmy
 * @since 2024-12-10
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class Logs  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer fileId;

    private String action;

    private LocalDateTime actionTime;


}
