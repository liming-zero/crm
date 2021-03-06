package com.bjpowernode.crm.service;

import com.bjpowernode.crm.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAll();
}
