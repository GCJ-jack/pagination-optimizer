package com.pig.app.driver.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig.app.driver.entity.Driver;
import com.pig.app.driver.mapper.DriverMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController

public class DriverController {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private DriverMapper driverMapper;

    private static final int count = 150 * 10000;

    @GetMapping("/init")
    public void initData() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            DriverMapper driverMapper = sqlSession.getMapper(DriverMapper.class);

            LocalDateTime now = LocalDateTime.now();
            for (int i = 1; i <= count; i++) {
                Driver driver = Driver.builder()
                        .id((long) i)
                        .userId((long) i)
                        .carModel(IdUtil.fastSimpleUUID())
                        .issuingAuthority(IdUtil.fastSimpleUUID())
                        .fileUmber(IdUtil.fastSimpleUUID())
                        .validStartTime(now)
                        .validEndTime(now)
                        .build();

                driverMapper.insert(driver);
                if (i % 10000 == 0) {
                    sqlSession.commit();
                    sqlSession.clearCache();
                    System.out.println("======================" + i + "=======================");
                }
            }
        }
    }

    @GetMapping("/{userId}")
    public Driver getDriver(@PathVariable Long userId) {
        LambdaQueryWrapper<Driver> wrapper = Wrappers.<Driver>lambdaQuery()
                .eq(Driver::getUserId, userId);
        return driverMapper.selectOne(wrapper);
    }

    @GetMapping("/by-user-ids")
    public Map<Long, Driver> getDriver(@RequestParam List<Long> userIds){
        LambdaQueryWrapper<Driver> wrapper = Wrappers.<Driver>lambdaQuery()
                .in(Driver::getUserId,userIds);

        List<Driver> drivers = driverMapper.selectList(wrapper);

        return drivers.stream()
                .collect(Collectors.toMap(
                        Driver::getUserId,
                        Function.identity(),
                        (v1, v2) -> v2
                ));
    }
}
