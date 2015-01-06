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
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * project_name:java_demo
 * package_name:edu.nuist.servlet
 * user: youzipi
 * date: 2015/1/6 16:08
 */


//class Bean extends HiBean{
//
//    Bean(String className) throws ClassNotFoundException, FileNotFoundException, IOException, IllegalAccessException, InstantiationException {
//        super(className);
//    }
//
//    Bean() {
//    }
//}


public class Hilet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jsonStr = req.getParameter("str");
        System.out.println(req.getParameter("str"));
        System.out.println(jsonStr);
        try {
            HiBean hibean = toJson(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Class<?> classType = Class.forName(className);
        Constructor<?> constructor = classType.getDeclaredConstructor(String.class);
        Object o = constructor.newInstance(classType.getName());
        System.out.println(className);

        Method getBeanFromJson = classType.getMethod("getBeanFromJson",String.class);
        HiBean hibean = (HiBean)getBeanFromJson.invoke(o,jsonObjectString);
        return hibean;
    }





    public static void main(String[] args) throws JSONException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, ClassNotFoundException {

        String json = "{class:'user',param:{'id':0,'name':'test'}}";
        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");
        User user = (User)Hilet.toJson(json);
        System.out.println(user);


    }
}
