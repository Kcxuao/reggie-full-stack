package com.kcxuao.domain;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //姓名
    private String name;


    //邮箱
    private String email;


    //性别 0 女 1 男
    private String sex;


    //身份证号
    private String idNumber;


    //头像
    private String avatar;

    @TableField(exist = false)
    private String token;

    // 验证码
    @TableField(exist = false)
    private String code;


    //状态 0:禁用，1:正常
    private Integer status;
}
