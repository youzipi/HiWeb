package edu.nuist.hibase;

import edu.nuist.Department;
import edu.nuist.User;
import edu.nuist.util.Configuration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * project_name:java_demo
 * package_name:edu.nuist.hibase
 * user: youzipi
 * date: 2015/1/9 15:42
 */
public class HiBase {

    protected static Connection conn = null;
    protected static Statement stmt = null;
    protected static ResultSet rs = null;

    /**
     * 静态加载配置项，实例化driver
     */
    static {
        try {
            Configuration.getBundle();
            Class.forName(Configuration.getDriver());
        } catch (Exception e) {
            System.err.println("=== Error Creating hibasePropFileExt ===");
            e.printStackTrace();
        }
    }


    public HiBase() {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(Configuration.getUrl(), Configuration.getUsername(), Configuration.getPassword());
            }
        } catch (SQLException e) {
            System.err.println("=== Error Connection conn ===");
            e.printStackTrace();
        }
    }


    protected HashMap<String, Object> list2map(LinkedList<Object> list) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        for (Object o : list) {
            String className = o.getClass().getSimpleName().toLowerCase();
            map.put(className, o);
        }
        return map;
    }

    protected String getSql(String sql_path, Object o) throws IOException {
        Class classType = o.getClass();
        Properties prop = new Properties();
        String simpleName = classType.getSimpleName().toLowerCase();
        prop.load(new FileInputStream(Configuration.webBeansClassPath + simpleName + Configuration.sqlPropFileExt));
        String sql = prop.getProperty(sql_path);
        System.out.println(sql);
        return sql;
    }

    protected String getSql(String sql_path) throws IOException {
        String[] strings = sql_path.split("\\.");
        String simpleName = strings[0];
        String sqlpath = strings[1];

        Properties prop = new Properties();
        prop.load(new FileInputStream(Configuration.webBeansClassPath + simpleName + Configuration.sqlPropFileExt));
        String sql = prop.getProperty(sqlpath);
        System.out.println(sql);
        return sql;
    }

    protected Matcher getMatcher(String sql) {
        String rule = "\\{(?<class>[\\w]+)\\.(?<param>[\\w]+)\\}";
        Pattern r = Pattern.compile(rule);
        return r.matcher(sql);
    }

    protected JSONObject getJsonObject(Object o) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> classType = o.getClass();
        Method toJsonObject = classType.getMethod("toJsonObject");
        return (JSONObject) toJsonObject.invoke(o);
    }

    public void exec(String sql_path, Object o) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, IOException {

        String sql = getSql(sql_path, o);
        Matcher m = getMatcher(sql);
        StringBuffer sql2 = new StringBuffer();

        JSONObject jsonObject = getJsonObject(o);

        while (m.find()) {
            String str = (m.group(0));
            String classname = m.group("class").toLowerCase();
            String param = m.group("param");
            String value;
//            String value = "null"; //appendReplacement没有进行替换
            if (!classname.equals(o.getClass().getSimpleName().toLowerCase())) {
                System.out.println("exec error..");
                return;
            }
            try {
                value = jsonObject.getString(param);
            } catch (JSONException e) {
                System.out.println(e.toString());
                e.printStackTrace();
                value = "null";
            }
            m.appendReplacement(sql2, "'" + value + "'");
        }
        m.appendTail(sql2);
        System.out.println(sql2.toString());

        this.update(sql2.toString());

    }

    public void exec(String sql_path, LinkedList list) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, IOException {


        String sql = getSql(sql_path);
        Matcher m = getMatcher(sql);
        StringBuffer sql2 = new StringBuffer();

        HashMap<String, Object> map = list2map(list);

        while (m.find()) {
            String str = (m.group(0));
//            String classname = m.group("class").substring(0, 1).toUpperCase() + m.group("class").substring(1);
            String classname = m.group("class").toLowerCase();
            String param = m.group("param");

            Object o = map.get(classname);
            JSONObject jsonObject = getJsonObject(o);
            String value;
            try {
                value = jsonObject.getString(param);
            } catch (JSONException e) {
                System.out.println(e.toString());
                value = "null";
            }
            m.appendReplacement(sql2, "'" + value + "'");
        }
        m.appendTail(sql2);
        System.out.println(sql2.toString());
        this.update(sql2.toString());
    }

    /**
     * query
     * @param sql_path
     * @return ResultSet
     * @throws IOException
     */
    public ResultSet query(String sql_path) throws IOException {
        String sql = getSql(sql_path);
        rs = getRs(sql);
        return rs;
    }

    /**
     * insert,update,delete
     * @param sql
     * @return ResultSet
     */
    public int update(String sql) {
        int num;
        if (sql == null) sql = "";
        try {
            stmt = getStm();
            num = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            num = 0;
        }
        return num;
    }

    public Statement getStm() {
        try {
            stmt = conn.createStatement();
            stmt.execute("set names utf8;");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return stmt;
    }

    public ResultSet getRs(String sql) {
        if (sql == null) sql = "";
        try {
            stmt = getStm();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


    public void close() {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, JSONException, InvocationTargetException {
        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");

        User u = new User();
        u.setId(1);
        u.setName("youzipi");

        Department department = new Department();
        department.setId("departmenttt");
        LinkedList<Object> list = new LinkedList<Object>();
        list.add(u);
        list.add(department);

        (new HiBase()).exec("user_insert", u);
//        (new HiBase()).exec("user.xx_insert", list);
    }
}
