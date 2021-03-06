package com.bjpowernode.crm.service;


import com.bjpowernode.crm.domain.Activity;
import com.bjpowernode.crm.domain.Clue;
import com.bjpowernode.crm.domain.Tran;

import java.util.List;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran tran, String createBy);
}
