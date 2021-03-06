package com.bjpowernode.crm.service.impl;

import com.bjpowernode.crm.dao.DicTypeDao;
import com.bjpowernode.crm.dao.DicValueDao;
import com.bjpowernode.crm.domain.DicType;
import com.bjpowernode.crm.domain.DicValue;
import com.bjpowernode.crm.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Resource
    private DicTypeDao dicTypeDao;

    @Resource
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<>();

        //将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        //将字典类型列表遍历
        for (DicType dicType : dtList){
            //取得每一种类型的字典类型编码
            String code = dicType.getCode();

            //根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code+"List",dvList);
        }

        return map;
    }
}
