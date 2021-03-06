package com.bjpowernode.crm.service.impl;

import com.bjpowernode.crm.dao.ActivityDao;
import com.bjpowernode.crm.dao.ActivityRemarkDao;
import com.bjpowernode.crm.dao.UserDao;
import com.bjpowernode.crm.domain.Activity;
import com.bjpowernode.crm.domain.ActivityRemark;
import com.bjpowernode.crm.domain.User;
import com.bjpowernode.crm.service.ActivityService;
import com.bjpowernode.crm.vo.PaginationVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao;

    @Resource
    private ActivityRemarkDao activityRemarkDao;

    @Resource
    private UserDao userDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false,
            rollbackFor = {RuntimeException.class} )
    public boolean save(Activity activity) {
        boolean flag = true;
        int count = activityDao.save(activity);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList =  activityDao.getActivityListByCondition(map);

        //创建一个vo将total和dataList封装到vo中
        PaginationVO vo = new PaginationVO();
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回收到影响的条数(实际删除的数量)
        int count2 = activityRemarkDao.deleteByAids(ids);
        if (count1 != count2){
            flag = false;
        }

        //删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        //取uList，取activity，并将这两项打包到map中，返回map
        List<User> uList = userDao.getUserList();
        Activity activity = activityDao.getById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("activity",activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int count = activityDao.update(activity);
        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> activityRemarkList = activityRemarkDao.getRemarkListByAid(activityId);
        return activityRemarkList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(ar);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        List<Activity> activityList = activityDao.getActivityListByNameAndNotByClueId(map);
        return activityList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> activityList = activityDao.getActivityListByName(aname);
        return activityList;
    }
}
