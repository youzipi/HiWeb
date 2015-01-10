package edu.nuist.util;

public class Configuration {
    public static final String beanPropFileExt = ".class.properties";
    public static final String servletPropFileExt = ".servlet.properties";
    public static final String sqlPropFileExt = ".sql.properties";
    public static final String hibasePropFileExt="hibase.cfg.properties";

    public static String webBeansClassPath = "";

    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    private static String classname;
    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Configuration.password = password;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        Configuration.user = user;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Configuration.url = url;
    }

    public static String getDriver() {
        return driver;
    }

    public static void setDriver(String driver) {
        Configuration.driver = driver;
    }

    //WILL be set in the web service starts by a string ends with "/"
    public static void setWebBeansDefinePath(String classpath){
        webBeansClassPath = classpath;
    }
}
