package edu.nuist.hibase;

import edu.nuist.hibean.Grade;
import edu.nuist.hibean.Student;
import edu.nuist.util.Configuration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
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

    protected Connection conn = null;
    protected Statement stmt = null;
    protected ResultSet rs = null;

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

/*     protected String getSql(String sql_path, Object o) throws IOException {
        Class classType = o.getClass();
        Properties prop = new Properties();
        String simpleName = classType.getSimpleName().toLowerCase();
        prop.load(new FileInputStream(Configuration.webBeansClassPath + simpleName + Configuration.sqlPropFileExt));
        String sql = prop.getProperty(sql_path);
        System.out.println(sql);
        return sql;
        
    /**
     * 
     * @param sql_path ex: user.user_insert
     * @return 根据sql_path从配置文件中获取到的sql语句
     * @throws IOException
     */
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

    protected JSONObject getJsonObject(Object o) throws NoSuchMethodException, IllegalAccessException {
        Class<?> classType = o.getClass();
        System.out.println(classType.getName());
        Method toJsonObject = classType.getMethod("toJsonObject");
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) toJsonObject.invoke(o);

        } catch (InvocationTargetException e) {
            System.out.println(e.getCause());
            e.printStackTrace();

        }
        return jsonObject;
    }

    public void exec(String sql_path, Student o) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, IOException {

        //String sql = getSql(sql_path, o);
        String sql = getSql(sql_path);
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

    public void exec(String sql_path, LinkedList<Object> list) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, IOException {


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
    public LinkedList query(String sql_path) throws IOException, NoSuchMethodException, IllegalAccessException, JSONException, SQLException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        String sql = getSql(sql_path);
        String className = sql_path.split("\\.")[0];
        rs = getRs(sql);
        LinkedList list = getListfromRs(rs, className);
        return list;
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

    /**
     *
     * @param rs ResultSet
     * @param simpleName 类名，由sql_path获得
     * @return LinkedList<Bean>
     */

    public  LinkedList getListfromRs(ResultSet rs,String simpleName) throws IOException, SQLException, JSONException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(Configuration.webBeansClassPath + simpleName + Configuration.beanPropFileExt));
//        String className = prop.getProperty("className");

        String className = Configuration.beansPath+simpleName.substring(0,1).toUpperCase()+simpleName.substring(1);
        Class<?> classType =  Class.forName(className);
        Constructor<?> constructor = classType.getDeclaredConstructor(String.class);
        Object o = constructor.newInstance(classType.getName());

        JSONArray jsonArray = getjsonArrfromRs(rs);
        Method toList = classType.getMethod("getListfromjsonArr",jsonArray.getClass());

        return (LinkedList) toList.invoke(o,jsonArray);
    }

    /**
     *
     * @param rs ResultSet
     * @return jsonArray 遍历rs填入jsonArray中
     * @throws IOException
     * @throws SQLException
     * @throws JSONException
     */
    public JSONArray getjsonArrfromRs(ResultSet rs) throws IOException, SQLException, JSONException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        JSONArray array = new JSONArray();
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();

            for (int i = 1; i <= columnCount; i++) {
                String columnName =metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            array.put(jsonObj);
        }
        System.out.println(array);
        return array;
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

    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, JSONException, InvocationTargetException, SQLException {
        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");

        Student u = new Student();
        u.setId(15);
        u.setName("youzipi");

        LinkedList<Object> list = new LinkedList<Object>();
//        list.add(u);

        HiBase hb = new HiBase();
//        hb.exec("student.insert", u);
        u.setId(2);
        hb.exec("student.update",u);
        u.setId(5);
        hb.exec("student.delete",u);
        LinkedList list3 = hb.query("student.select");
        System.out.println(list3);

        Grade grade = new Grade();
        grade.setId(4);
        grade.setCourse_id(1);
        grade.setScore(99);
        grade.setStu_id(1);
        list.add(u);
        list.add(grade);
//        hb.exec("grade.insert",list);
//        LinkedList list2 = hb.query("grade.select");
//
//        for(Object o:list2){
//            System.out.println(o);
//        }
    }
}
