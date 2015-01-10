package edu.nuist.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    public static final String beanPropFileExt = ".class.properties";
    public static final String servletPropFileExt = ".servlet.properties";
    public static final String sqlPropFileExt = ".sql.properties";
    public static final String hibasePropFileExt="hibase.cfg.properties";

    public static String webBeansClassPath = "";

//    private static PropertyResourceBundle bundle; // 配置资源文件
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    public static String getDriver() {
        return driver;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    //WILL be set in the web service starts by a string ends with "/"
    public static void setWebBeansDefinePath(String classpath){
        webBeansClassPath = classpath;
    }

    public static void getBundle() throws IOException {
        // 读取配置文件
        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");
        System.out.println(Configuration.webBeansClassPath + Configuration.hibasePropFileExt);
//        bundle = new PropertyResourceBundle(Configuration.class.getResourceAsStream(Configuration.webBeansClassPath + Configuration.hibasePropFileExt));
        Properties prop = new Properties();
        prop.load(new FileInputStream(webBeansClassPath + hibasePropFileExt));
        driver = prop.getProperty("driver");
        url = prop.getProperty("connection.url");
        username = prop.getProperty("connection.username");
        password = prop.getProperty("connection.userpassword");

    }


}
