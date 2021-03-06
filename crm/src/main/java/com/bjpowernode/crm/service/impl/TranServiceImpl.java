package com.bjpowernode.crm.service.impl;

import com.bjpowernode.crm.dao.CustomerDao;
import com.bjpowernode.crm.dao.TranDao;
import com.bjpowernode.crm.dao.TranHistoryDao;
import com.bjpowernode.crm.domain.Customer;
import com.bjpowernode.crm.domain.Tran;
import com.bjpowernode.crm.domain.TranHistory;
import com.bjpowernode.crm.service.TranService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;

    @Override
    public boolean save(Tran tran, String customerName) {

        /*
            交易添加业务:
                在做添加之前，tran里面缺少customerId
                先处理客户相关的需求
                    1.判断customerName, 根据客户名称在客户表进行精确查询
                      如果有这个客户，则取出这个客户的id，封装到tran对象中。
                      如果没有这个客户，则在客户表新建一条客户信息，然后将新建的客户的id取出，封装到tran对象中
                    2.经过以上操作后，tran对象的信息就全了，需要执行添加交易的操作
                    3.添加交易完毕后，需要创建一条交易历史
         */

        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);

        //customer为空，需要创建客户
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());

            //添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }
        }

        //通过以上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户是已经有了，客户的id就有了
        //将客户的id封装到tran对象中
        tran.setCustomerId(customer.getId());

        //添加交易
        int count2 = tranDao.save(tran);
        if (count2 != 1){
            flag = false;
        }

        //添加交易历史记录
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        int count3 = tranHistoryDao.save(tranHistory);
        if (count3 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran tran = tranDao.detail(id);
        return tran;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> tranHistoryList = tranDao.getHistoryListByTranId(tranId);
        return tranHistoryList;
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        //改变交易阶段
        int count1 = tranDao.changeStage(tran);
        if (count1 != 1){
            flag = false;
        }

        //交易阶段改变后，生成一条交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(tran.getEditTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setPossibility(tran.getPossibility());
        //添加交易历史
        int count2 = tranHistoryDao.save(tranHistory);
        if (count2 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        //取得total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //将total和dataList保存到map集合中
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }
}
