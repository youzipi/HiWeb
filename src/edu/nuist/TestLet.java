package edu.nuist;

import edu.nuist.servlet.Hilet;
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
 * date: 2015/1/7 14:38
 */

public class Testlet extends Hilet {
    public Testlet() {
    }

    @Override
    public void doing() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, JSONException, InvocationTargetException {

        String jsonStr = request.getParameter("str");
//        System.out.println(request.getParameter("str"));
        System.out.println("str="+jsonStr);
        User A=(User)toJson(jsonStr);
        //存储P
        put("user", A, "request");
        //获取P
        User p=get("user");
        System.out.println(p);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

    }


}