package com.kcxuao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kcxuao.domain.Category;

public interface CategoryService extends IService<Category> {

    void delete(Long id);
}
