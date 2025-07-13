package com.pig.app.user.controller;

import com.pig.app.user.entity.Driver;
import com.pig.app.user.entity.User;
import com.pig.app.user.mapper.UserMapper;
import com.pig.app.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private  UserMapper userMapper;

    private static final RestTemplate restTemplate = new RestTemplate();


    private static final String url = "http://localhost:8888/driver/";


    @GetMapping("/page")
    public List<UserVO> getUserWithDriver(long pagenum, long pageSize){
        long start = (pagenum - 1) * pageSize;

        List<User> users = userMapper.selectByLimit(start,pageSize);

        return  users.stream()
                .map(user -> {
                    Driver driver = restTemplate.getForObject(url + user.getId(),Driver.class);
                    return new UserVO(user,driver);
                }).collect(Collectors.toList());
    }
}
