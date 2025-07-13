package com.pig.app.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("driver_license")
public class Driver {
    private Long id;
    private Long userId;
    private String carModel;
    private String issuingAuthority;
    private String fileUmber;
    private LocalDateTime validStartTime;
    private LocalDateTime validEndTime;
    private Long creatorId;
    private Long updaterId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
