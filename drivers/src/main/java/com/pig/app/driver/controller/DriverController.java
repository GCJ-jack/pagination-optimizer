package com.pig.app.driver.controller;

import com.pig.app.driver.mapper.DriverMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController

public class DriverController {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private DriverMapper driverMapper;

    private static final int count = 150 * 10000;
}
