package edu.nuist;

import edu.nuist.servlet.Hilet;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * project_name:java_demo
 * package_name:edu.nuist.servlet
 * user: youzipi
 * date: 2015/1/7 14:38
 */

public class TestLet extends Hilet {

    /**
     * Constructor of the object.
     */
    public TestLet() {

        super();
    }

    public void doing() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, JSONException, InvocationTargetException {

        //doget(request);    //传递request对象
        String jsonStr = request.getParameter("str");
        User A=(User)toJson(jsonStr);
        //存储P
        put("user", A, "request");
        //获取P
        User p=get("user");
        System.out.println(p);

    }


}