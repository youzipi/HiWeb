package edu.nuist.servlet;

import edu.nuist.util.Configuration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * project_name:java_demo
 * package_name:edu.nuist.servlet
 * user: youzipi
 * date: 2015/1/8 21:12
 */
public class Initlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        String prefix = config.getServletContext().getRealPath("/"); //获取当前路径

        String servletProp = config.getInitParameter("servletProp");//从web.xml中获取参数值
        String beanProp = config.getInitParameter("beanProp");//从web.xml中获取参数值
        Configuration.setWebBeansDefinePath(prefix + servletProp);
        System.out.println(Configuration.webBeansClassPath);

//        String count = config.getInitParameter("count");//从web.xml中获取参数值
//        System.out.println(config.getServletContext().setInitParameter("count", "2"));
//
//        String count2 = config.getInitParameter("count");//从web.xml中获取参数值
//        System.out.println(count2);
    }
}
