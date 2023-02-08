package com.kcxuao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcxuao.common.R;
import com.kcxuao.domain.Category;
import com.kcxuao.domain.PageInfo;
import com.kcxuao.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 菜品分类
 */
@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页
     * @param pageInfo
     * @return
     */
    @PostMapping("/page")
    public R<Page<Category>> page(@RequestBody PageInfo pageInfo) {
        log.info("获取分类分页信息 ==> {}", pageInfo);

        Page<Category> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Category::getSort);

        categoryService.page(page, lqw);
        return R.success(page);
    }

    /**
     * 保存 or 修改
     * @param category
     * @param flag 1：修改分类 0：保存分类
     * @return
     */
    @PostMapping("/{flag}")
    public R<String> saveOrUpdate(@RequestBody Category category, @PathVariable int flag) {
        log.info("新增 or 修改分类 ==> {} flag ==> {}", category, flag);
        categoryService.saveOrUpdate(category);

        if (flag == 1) {
            return R.success("分类修改成功");
        }

        return R.success("分类添加成功");
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable Long id) {
        log.info("删除分类 ==> {}", id);

        categoryService.delete(id);
        return R.success("删除成功");
    }

    /**
     * 请求分类列表
     * @param type
     * @return
     */
    @GetMapping("/{type}")
    public R<List<Category>> list(@PathVariable int type) {
        log.info("请求分类 ==> {}", type);

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getType, type);
        List<Category> list = categoryService.list(lqw);

        if (type == 0) {
            LambdaQueryWrapper<Category> lqw1 = new LambdaQueryWrapper<>();
            lqw1.orderByAsc(Category::getType);
            list = categoryService.list(lqw1);
        }

        return R.success(list);
    }
}
