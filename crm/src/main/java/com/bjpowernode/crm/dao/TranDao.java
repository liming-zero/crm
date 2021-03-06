package com.bjpowernode.crm.dao;

import com.bjpowernode.crm.domain.Tran;
import com.bjpowernode.crm.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    int changeStage(Tran tran);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
