package com.jackssybin.demo03.mapper;

import com.jackssybin.demo.entity.DemoUser;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoUserMapper {

    DemoUser queryUser(Integer id);
}
