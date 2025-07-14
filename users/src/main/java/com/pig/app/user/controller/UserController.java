package com.pig.app.user.controller;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.pig.app.user.entity.Driver;
import com.pig.app.user.entity.User;
import com.pig.app.user.mapper.UserMapper;
import com.pig.app.user.vo.UserVO;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private  UserMapper userMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private static final RestTemplate restTemplate = new RestTemplate();


    private static final String url = "http://localhost:8888/driver/";

    private static final int count = 150 * 10000;


    @GetMapping("/init")
     public void initData() {
         try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
             UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

             LocalDateTime now = LocalDateTime.now();
             for (int i = 1; i <= count; i++) {
                 User driver = User.builder()
                     .id((long) i)
                     .name(IdUtil.fastSimpleUUID())
                     .gender(1)
                     .phone(IdUtil.fastSimpleUUID())
                     .idCard(IdUtil.fastSimpleUUID())
                     .build();

                 userMapper.insert(driver);
                 if (i % 10000 == 0) {
                     sqlSession.commit();
                     sqlSession.clearCache();
                     System.out.println("======================" + i + "=======================");
                 }
             }
         }
     }


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

    @GetMapping("/page/optimize")
    public List<UserVO> getUserWithDriverOptimized(long pagenum, long pageSize) {
        long start = (pagenum - 1) * pageSize;

        List<User> users = userMapper.selectByLimit(start,pageSize);

        String idParam = users.stream()
                .map(User::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String json = restTemplate.getForObject(url  + "by-user-ids?userIds=" + idParam, String.class);

        Map<Long,Driver> map = JSONUtil.toBean(json, new TypeReference<Map<Long, Driver>>() {
        },true);

        return users.stream()
                .map(user ->{
                    Driver driver = map.get(user.getId());
                    return new UserVO(user,driver);
                        })
                .collect(Collectors.toList());
    }
}
