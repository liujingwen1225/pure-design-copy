package com.recommend.controller.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.recommend.entity.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
    @ApiModelProperty("用户类型：【1=新用户，2=老用户】")
    private Integer userType;
}
