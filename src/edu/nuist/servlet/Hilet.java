package edu.nuist.servlet;

import edu.nuist.User;
import edu.nuist.hibean.HiBean;
import edu.nuist.util.Configuration;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * project_name:java_demo
 * package_name:edu.nuist.servlet
 * user: youzipi
 * date: 2015/1/6 16:08
 */


class Bean extends HiBean{

    Bean(String className) throws ClassNotFoundException, FileNotFoundException, IOException, IllegalAccessException, InstantiationException {
        super(className);
    }

    Bean() {
    }
}


public class Hilet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }


    public static HiBean toJson(String json) throws InvocationTargetException, NoSuchMethodException, InstantiationException, JSONException, IllegalAccessException, ClassNotFoundException, IOException {
        Properties prop = new Properties();

        JSONObject jsonObject = new JSONObject(json);
        System.out.println(jsonObject);
        String beanName = jsonObject.getString("class");
        String jsonObjectString = jsonObject.getString("param");
//        System.out.println(beanName);
        prop.load(new FileInputStream(Configuration.webBeansClassPath + beanName + Configuration.servletPropFileExt));
        System.out.println(prop);
        String className = prop.getProperty(beanName);//属性值类型

        System.out.println(className);
        Bean bean = new Bean(className);
        HiBean hibean = bean.getBeanFromJson(jsonObjectString);
        return hibean;
    }





    public static void main(String[] args) throws JSONException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, ClassNotFoundException {

        String json = "{class:'user',param:{'id':0,'name':'test'}}";
        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");
        User user = (User)Hilet.toJson(json);
        System.out.println(user);


    }
}
