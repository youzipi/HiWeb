package edu.nuist.servlet;

import edu.nuist.hibean.HiBean;
import edu.nuist.util.Configuration;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
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


//public abstract class Hilet extends HttpServlet {
public class Hilet extends HttpServlet {
    protected HttpServletRequest request;
    protected HttpSession session;
    protected ServletContext application;


    @Override
    public void init(ServletConfig config) throws ServletException {
//        String prefix = config.getServletContext().getRealPath("/"); //获取当前路径
//        this.application = config.getServletContext();

//        String servletProp = config.getInitParameter("servletProp");//从web.xml中获取参数值,找到log4j这个文件
//        Configuration.setWebBeansDefinePath(prefix+servletProp);


    }


    //这个函数分别从request，session, application中取 key 指定的对象
    //如果request中没有，就到session中去找，再没有就到application中去找，再找不到，返回空
    //调用方法应该是 <User>get("user"),这样，直接调用
    @SuppressWarnings("unchecked")
    public final  <T> T get(String c) throws ClassCastException {
        if (request.getAttribute(c) != null) {
            return (T) request.getAttribute(c);
        }
        else if (session.getAttribute(c) != null) {
            return (T) session.getAttribute(c);
        }
        else if (application.getAttribute(c) != null) {
            return (T) application.getAttribute(c);
        }
        return null;
    }

    //这个函数主要是把对象o，使用key,存放到request, session, application中
    public final void put(String key, Object o, String scope) {
        if (scope.equals("request")) {
            request.setAttribute(key, o);
        }
        else if (scope.equals("session")) {
            session.setAttribute(key, o);
        }
        else if (scope.equals("application")) {
            application.setAttribute(key, o);
        }
    }

    //这个函数主要是把对象o，使用key,存放到request中
    public final void put(String key, Object o) {
        put(key, o, "request");
    }


    public void doing() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, JSONException, InvocationTargetException {
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.request = req;
        this.session = this.request.getSession();
        this.application = this.session.getServletContext();
//        System.out.println("doPost.str"+request.getParameter("str"));
        try {
            doing();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            forward(req,resp);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    protected final <T> void forward(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Properties prop = new Properties();

        String letName = this.getClass().getSimpleName();
        System.out.println(letName);
//        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");
        System.out.println(Configuration.webBeansClassPath + letName + Configuration.outputPropFileExt);
        prop.load(new FileInputStream(Configuration.webBeansClassPath + letName + Configuration.outputPropFileExt));
        String param = prop.getProperty("json");
        String next = prop.getProperty("next");
        System.out.println("param="+param);
        System.out.println("next="+next);
        System.out.println(param != null);
        if(param != null){
            System.out.println("do param");
            T message = get(param);
            PrintWriter out = resp.getWriter();
            out.println(message);
        }
        else if(next != null){
            System.out.println("do next");
            Class classType = Class.forName(next);
            System.out.println(next);
            Constructor constructor = classType.getDeclaredConstructor();
            Object o = constructor.newInstance();
            Method postMethod = classType.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
            postMethod.invoke(o,request,resp);
//            Method[] methods = classType.getMethods();
//            for(Method method:methods){
//                System.out.print(method.getName() + ":");
//                System.out.println(method.getParameterTypes());
//
//            }
        }
        else{
            System.out.println("out.properties is empty");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }


    public  final HiBean toJson(String json) throws InvocationTargetException, NoSuchMethodException, InstantiationException, JSONException, IllegalAccessException, ClassNotFoundException, IOException {
        Properties prop = new Properties();

        System.out.println("json="+json);
        JSONObject jsonObject = new JSONObject(json);
        System.out.println(jsonObject);
        String beanName = jsonObject.getString("class").toLowerCase();
        String jsonObjectString = jsonObject.getString("param");
//        System.out.println(beanName);
        System.out.println(Configuration.webBeansClassPath + beanName + Configuration.servletPropFileExt);
        prop.load(new FileInputStream(Configuration.webBeansClassPath + beanName + Configuration.servletPropFileExt));
        System.out.println(prop);
        String className = prop.getProperty(beanName);//属性值类型
        Class<?> classType = Class.forName(className);
        Constructor<?> constructor = classType.getDeclaredConstructor(String.class);
        Object o = constructor.newInstance(classType.getName());
        System.out.println(className);

        Method getBeanFromJson = classType.getMethod("getBeanFromJson", String.class);
        HiBean hibean = (HiBean) getBeanFromJson.invoke(o, jsonObjectString);
        return hibean;
    }


    public static void main(String[] args) throws JSONException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, ClassNotFoundException, ServletException {

        String json = "{class:'user',param:{'id':0,'name':'test'}}";
//        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");
        (new Hilet()).init();
//        User user = (User) Hilet.toJson(json);
//        System.out.println(user);

        Properties prop = new Properties();

        String letName = (new edu.nuist.Testlet()).getClass().getSimpleName().toLowerCase();
        System.out.println(letName);
        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");
        System.out.println(Configuration.webBeansClassPath + letName + Configuration.outputPropFileExt);
        prop.load(new FileInputStream(Configuration.webBeansClassPath + letName + Configuration.outputPropFileExt));



    }
}
