package com.bjpowernode.crm.dao;

import com.bjpowernode.crm.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
