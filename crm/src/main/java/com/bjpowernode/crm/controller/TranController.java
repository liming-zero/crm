package com.bjpowernode.crm.controller;

import com.bjpowernode.crm.domain.Tran;
import com.bjpowernode.crm.domain.TranHistory;
import com.bjpowernode.crm.domain.User;
import com.bjpowernode.crm.service.CustomerService;
import com.bjpowernode.crm.service.TranService;
import com.bjpowernode.crm.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//交易控制器
@RequestMapping("/workbench/transaction")
@Controller
public class TranController {

    @Autowired
    private TranService tranService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ServletContext application;

    //跳转到交易添加页的操作
    @RequestMapping("/add.do")
    private ModelAndView add(){
        ModelAndView mv = new ModelAndView();
        List<User> userList = userService.getUserList();
        mv.addObject("userList",userList);
        mv.setViewName("transaction/save");
        return mv;
    }

    //取得客户名称列表(按照客户名称进行模糊查询)
    @ResponseBody
    @RequestMapping("/getCustomerName.do")
    private List<String> getCustomerName(String name){
        List<String> stringList = customerService.getCustomerName(name);
        return stringList;
    }

    //执行添加交易的操作
    @RequestMapping("/save.do")
    private ModelAndView save(Tran tran, HttpServletRequest request){
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(((User) request.getSession().getAttribute("user")).getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        String customerName = request.getParameter("customerName");
        boolean flag = tranService.save(tran,customerName);
        ModelAndView mv = new ModelAndView();
        if (flag){
            //如果添加交易成功，跳转到列表页
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return mv;
    }

    //跳转到详细信息页
    @RequestMapping("/detail.do")
    private ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Tran tran = tranService.detail(id);

        /*
            处理可能性,因为只有在控制器里面才能获取上下文application对象
                阶段:tran.getStage()
                阶段和可能性之间的对应关系 pMap
         */
        String stage = tran.getStage();
        Map<String,String> pMap = (Map<String,String>)application.getAttribute("pMap");

        //在监听器的Map中根据key取value
        String possibility = pMap.get(stage);

        tran.setPossibility(possibility);

        mv.addObject("tran",tran);
        mv.addObject("possibility",possibility);
        mv.setViewName("transaction/detail");
        return mv;
    }

    //根据交易id取得响应的列表
    @ResponseBody
    @RequestMapping("/getHistoryListByTranId.do")
    private List<TranHistory> getHistoryListByTranId(String tranId){
        List<TranHistory> tranHistoryList = tranService.getHistoryListByTranId(tranId);

        //拿到交易历史列表处理可能性,因为只有在控制器里面才能获取上下文application对象

        //阶段和可能性之间的对应关系
        Map<String,String> pMap = (Map<String,String>)application.getAttribute("pMap");

        //将交易历史列表遍历
        for (TranHistory tranHistory : tranHistoryList){
            //根据每一条交易历史，取出每一个阶段
            String stage = tranHistory.getStage();
            String possibility = pMap.get(stage);
            tranHistory.setPossibility(possibility);
        }

        return tranHistoryList;
    }

    //执行改变阶段的操作
    @ResponseBody
    @RequestMapping("/changeStage.do")
    private Map<String,Object> changeStage(Tran tran, HttpServletRequest request){
        tran.setEditBy(((User) request.getSession().getAttribute("user")).getName());
        tran.setEditTime(DateTimeUtil.getSysTime());
        boolean flag = tranService.changeStage(tran);

        //阶段和可能性之间的对应关系
        Map<String,String> pMap = (Map<String,String>)application.getAttribute("pMap");
        tran.setPossibility(pMap.get(tran.getStage()));

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tran",tran);
        return map;
    }

    //取得交易阶段数量统计图表的数据
    @ResponseBody
    @RequestMapping("/getCharts.do")
    private Map<String,Object> getCharts(){

        //业务层返回: total  dataList
        //通过map打包以上两项进行返回
        Map<String,Object> map = tranService.getCharts();

        return map;
    }


}
