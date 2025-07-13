package com.pig.app.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("test_user")
public class User {
    private Long id;
    private String name;
    private Integer gender;
    private String idCard;
    private String phone;
    private Long creatorId;
    private Long updaterId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
