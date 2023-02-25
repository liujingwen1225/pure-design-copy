package com.qingge.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingge.springboot.entity.Menu;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
