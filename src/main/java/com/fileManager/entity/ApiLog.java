package com.fileManager.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("api_logs")
public class ApiLog {
//    @TableId(type = IdType.AUTO)
    private Long id;
    private String method;         // 方法名
    private String url;            // 请求URL
    private String ip;             // 客户端IP
    private String params;         // 请求参数
    private String result;         // 响应结果
    private Long executionTime;    // 执行时间（ms）
    private Integer status;        // 状态：1成功，0失败
    private String error;          // 异常信息
    private String username;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt; // 创建时间
}
