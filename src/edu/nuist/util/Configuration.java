package edu.nuist.util;

public class Configuration {
    public static final String beanPropFileExt = ".class.properties";
    public static final String servletPropFileExt = ".servlet.properties";
    public static String webBeansClassPath = "";



    //WILL be set in the web service starts by a string ends with "/"
    public static void setWebBeansDefinePath(String classpath){
        webBeansClassPath = classpath;
    }
}
