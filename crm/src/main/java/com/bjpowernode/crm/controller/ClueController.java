package com.bjpowernode.crm.controller;

import com.bjpowernode.crm.domain.Activity;
import com.bjpowernode.crm.domain.Clue;
import com.bjpowernode.crm.domain.Tran;
import com.bjpowernode.crm.domain.User;
import com.bjpowernode.crm.service.ActivityService;
import com.bjpowernode.crm.service.ClueService;
import com.bjpowernode.crm.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//进入到线索控制器
@Controller
@RequestMapping("/workbench/clue")
public class ClueController {

    @Autowired
    private UserService userService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;

    //取得用户信息列表
    @ResponseBody
    @RequestMapping("/getUserList.do")
    private List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return userList;
    }

    //执行线索添加操作
    @RequestMapping(value = "/save.do",method = RequestMethod.POST)
    private void save(Clue clue, HttpServletRequest request, HttpServletResponse response){
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        boolean flag = clueService.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    @RequestMapping("/detail.do")
    private ModelAndView detail(String id){
        Clue clueObj = clueService.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("clue",clueObj);
        mv.setViewName("clue/detail");
        return mv;
    }

    //根据线索id查询关联的市场活动列表
    @ResponseBody
    @RequestMapping(value = "/getActivityListByClueId.do")
    private List<Activity> getActivityListByClueId(String clueId){
        List<Activity> activityList = clueService.getActivityListByClueId(clueId);
        return activityList;
    }

    //解除关联
    @RequestMapping(value = "/unbund.do",method = RequestMethod.POST)
    private void unbund(String id,HttpServletResponse response){
        boolean flag = clueService.unbund(id);
        PrintJson.printJsonFlag(response,flag);
    }

    //查询市场活动列表( 根据名称模糊查询+排除掉已经关联指定线索的列表 )
    @ResponseBody
    @RequestMapping("/getActivityListByNameAndNotByClueId.do")
    private List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId){
        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        List<Activity> activityList = activityService.getActivityListByNameAndNotByClueId(map);
        return activityList;
    }

    //执行关联市场活动的操作
    @RequestMapping("/bund.do")
    private void bund(String cid, HttpServletRequest request, HttpServletResponse response){
        String[] aids = request.getParameterValues("aid");
        boolean flag = clueService.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag);
    }

    //为搜索模态窗口的 搜索框 绑定事件,搜索市场活动，执行搜索并展现市场活动列表的操作
    @ResponseBody
    @RequestMapping("/getActivityListByName.do")
    private List<Activity> getActivityListByName(String aname){
        List<Activity> activityList = activityService.getActivityListByName(aname);
        return activityList;
    }

    //执行线索转换的操作，发出传统请求，请求结束后，最终响应回线索列表页
    @RequestMapping("/convert.do")
    private ModelAndView convert(String flag, String clueId,HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        Tran tran = null;
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        //接收是否需要创建的标记
        if ("a".equals(flag)){
            tran = new Tran();
            //接收交易表单中的参数
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setMoney(request.getParameter("money"));
            tran.setName(request.getParameter("name"));
            tran.setExpectedDate(request.getParameter("expectedDate"));
            tran.setStage(request.getParameter("stage"));
            tran.setActivityId(request.getParameter("activityId"));
            tran.setCreateBy(createBy);
        }
        boolean returnFlag = clueService.convert(clueId,tran,createBy);
        if (returnFlag){
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }
        return mv;
    }
}
