package com.bjpowernode.crm.service.impl;

import com.bjpowernode.crm.dao.CustomerDao;
import com.bjpowernode.crm.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Override
    public List<String> getCustomerName(String name) {
        List<String> stringList = customerDao.getCustomerName(name);
        return stringList;
    }
}
