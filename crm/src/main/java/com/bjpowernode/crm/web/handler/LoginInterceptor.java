package com.bjpowernode.crm.web.handler;

import com.bjpowernode.crm.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
            System.out.println("拦截器执行了");

            String path = request.getServletPath();
            if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
                return true;
            }else{
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");
                //如果User不为空，说明登录过
                if (user != null){
                    return true;
                }else{
                    //获取当前请求的路径
                    String basePath = request.getScheme() + "://" + request.getServerName() + ":"  + request.getServerPort()+request.getContextPath();
                    //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理 否则直接重定向就可以了
                    if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                        //告诉ajax我是重定向
                        response.setHeader("SESSIONSTATUS", "TIMEOUT");
                        //告诉ajax我重定向的路径
                        response.setHeader("CONTEXTPATH", basePath+"/login.jsp");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return false;
                    }else{
                        response.sendRedirect(request.getContextPath() + "/login.jsp");
                        return false;
                    }

                }
                    //重定向到登录页
                    /**
                     * 转发和重定向路径的写法:
                     *      1.转发:使用的是一种特殊的绝对路径的使用方式，这种绝对路径前面不加/项目名，这种路径也称之为内部路径( login.jsp )
                     *      2.重定向:使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径( /crm/login.jsp )
                     * 为什么使用重定向？
                     *      转发之后，路径会停留在老路径上，而不是跳转之后最新资源的路径，我们应该在为用户跳到登录页的同时，
                     *      将浏览器的地址栏应该自动设置为当前的登录页的路径
                     *
                     */
        }
    }
}
