package com.bjpowernode.crm.dao;

import com.bjpowernode.crm.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    //说白了就是根据id删单条
    int unbund(String id);

    int bund(ClueActivityRelation car);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int delete(ClueActivityRelation clueActivityRelation);
}
