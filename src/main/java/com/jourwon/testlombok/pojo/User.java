package com.jourwon.testlombok.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:用户
 *
 * @author JourWon
 * @date Created on 2019/1/3
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -8054600833969507380L;

    private Integer id;

    private String username;

    private Integer age;

}
