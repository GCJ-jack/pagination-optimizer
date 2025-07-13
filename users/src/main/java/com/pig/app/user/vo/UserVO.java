package com.pig.app.user.vo;

import com.pig.app.user.entity.Driver;
import com.pig.app.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private User user;
    private Driver driver;
}
