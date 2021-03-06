package com.bjpowernode.crm.dao;

import com.bjpowernode.crm.domain.Activity;
import com.bjpowernode.crm.domain.Clue;

import java.util.List;

public interface ClueDao {

    int save(Clue clue);

    Clue detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    Clue getById(String clueId);

    int delete(String clueId);
}
