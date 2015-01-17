package edu.nuist.servlet;

import edu.nuist.User;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * project_name:java_demo
 * package_name:edu.nuist.servlet
 * user: youzipi
 * date: 2015/1/17 19:25
 */
public class Midlet extends Hilet {
    public Midlet() {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void doing() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, JSONException, InvocationTargetException {
        //获取P
        User p=get("user");
        p.setDepartment("Midlet");
        put("user",p);
        System.out.println(p);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
