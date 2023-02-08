package com.kcxuao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcxuao.domain.AddressBook;
import com.kcxuao.mapper.AddressBookMapper;
import com.kcxuao.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
