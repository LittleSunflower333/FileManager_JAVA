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
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String status;


}
