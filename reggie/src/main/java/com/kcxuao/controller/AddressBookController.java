package com.kcxuao.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kcxuao.common.CustomException;
import com.kcxuao.common.R;
import com.kcxuao.Utils.RedisUtils;
import com.kcxuao.domain.AddressBook;
import com.kcxuao.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿
 */
@Slf4j
@RestController
@RequestMapping("/address")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 保存or更新
     * @param addressBook 地址簿
     * @return ok
     */
    @PostMapping
    public R<String> saveOrUpdate(@RequestBody AddressBook addressBook) {
        Long userId = Long.valueOf((String) RedisUtils.get("id"));
        addressBook.setUserId(userId);

        try {
            addressBookService.saveOrUpdate(addressBook);
        } catch (Exception e) {
            throw new CustomException("参数不合法");
        }
        log.info("保存地址 ==> {}", addressBook);
        return R.success("保存成功");
    }

    /**
     * 设置默认地址
     * @param id 地址簿ID
     * @return ok
     */
    @GetMapping("/{id}")
    public R<String> setDefault(@PathVariable long id) {
        log.info("设置默认地址 ==> {}", id);

        // 取消其余地址默认属性
        Long userId = Long.valueOf((String) RedisUtils.get("id"));
        LambdaUpdateWrapper<AddressBook> luw = new LambdaUpdateWrapper<>();
        luw.eq(AddressBook::getUserId, userId).set(AddressBook::getIsDefault, 0);
        addressBookService.update(luw);

        // 设置默认
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("设置成功");
    }

    /**
     * 获取地址列表
     * @param flag all：获取全部地址簿  default：获取默认地址簿
     * @return 地址簿列表
     */
    @GetMapping("/list/{flag}")
    public R<List<AddressBook>> list(@PathVariable String flag) {
        log.info("flag ==> {}", flag);
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();

        if ("default".equals(flag)) {
            lqw.eq(AddressBook::getIsDefault, 1);
        }

        Long userId = Long.valueOf((String) RedisUtils.get("id"));
        lqw.eq(AddressBook::getUserId, userId);
        List<AddressBook> list = addressBookService.list(lqw);

        // 地址簿只存在一位，自动设置为默认地址
        if (list.size() == 1 && list.get(0).getIsDefault() == 0) {
            setDefault(list.get(0).getId());
            list.get(0).setIsDefault(1);
        }
        return R.success(list);
    }

    /**
     * 删除地址
     * @param addressBook 地址簿信息
     * @return ok
     */
    @DeleteMapping
    public R<String> remove(@RequestBody AddressBook addressBook) {
        log.info("删除地址 ==> {}", addressBook);

        if (addressBook.getIsDefault() == 1) {
            throw new CustomException("默认地址无法删除");
        }

        addressBookService.removeById(addressBook);
        return R.success("删除成功");
    }
}
