package com.jourwon.testlombok.controller;

import com.jourwon.testlombok.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:用户controller
 *
 * @author JourWon
 * @date Created on 2019/1/3
 */
@Slf4j
@RestController
@RequestMapping(("/user"))
public class UserController {

    @GetMapping("/getUserById/{id}")
    public User getUserById(@PathVariable Integer id) {
        User user = new User();
        user.setUsername("风清扬");
        user.setAge(21);
        user.setId(id);

        if (log.isInfoEnabled()) {
            log.info("用户 {}", user);
        }

        return user;
    }

}
