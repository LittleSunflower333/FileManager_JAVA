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
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;

    private String filePath;

    private Integer fileSize;

    private Integer uploadedBy;

    private LocalDateTime uploadedAt;

    private String fileType;

    private Integer folderId;


}
