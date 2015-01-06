package edu.nuist.hibean;

import edu.nuist.util.Configuration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

public abstract class HiBean {

    private Properties prop = new Properties();
    private Class<?> classType;//

    public HiBean(String className) throws ClassNotFoundException, FileNotFoundException, IOException, IllegalAccessException, InstantiationException {
        System.out.println("HiBean:className="+className);
        this.classType =  Class.forName(className);
        String simpleName = this.classType.getSimpleName();
    	this.prop.load(new FileInputStream(Configuration.webBeansClassPath + simpleName + Configuration.beanPropFileExt));

    }

    public HiBean() {

    }

    public HiBean getBeanFromJson(String Json) throws JSONException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        JSONObject jsonObject = new JSONObject(Json);
        Constructor constructor = this.classType.getDeclaredConstructor(String.class);
        Object o = constructor.newInstance(this.classType.getName());
//        Object o = this.classType.newInstance();
//        Object o = this;

        Iterator iter = jsonObject.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();//属性名
//            String value = jsonObject.getString(key);//属性值
            String paramType = prop.getProperty(key);//属性值类型

            String setter = "set"+key.substring(0,1).toUpperCase() + key.substring(1);//setter方法
            System.out.println(setter);

            if (paramType.equals("String")) {
                String paramater = jsonObject.getString(key);//属性值
                Method setmethod = classType.getMethod(setter, String.class);
                setmethod.invoke(o,paramater);
            }
            else if(paramType.equals("int")){
                int paramater = jsonObject.getInt(key);
                Method setmethod = classType.getMethod(setter, int.class);
                setmethod.invoke(o,paramater);
            }
            else if(paramType.equals("boolean")){
                Boolean paramater = jsonObject.getBoolean(key);
                Method method = classType.getMethod(setter,Boolean.class);
                method.invoke(o,paramater);
            }
            else if(paramType.equals("double")){
                Double paramater = jsonObject.getDouble(key);
                Method method = classType.getMethod(setter,Double.class);
                method.invoke(o,paramater);
            }
        }
        return (HiBean)o;
    }
    public LinkedList<HiBean> getBeanLinkedListFromJson(String Json) throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JSONArray jsonArray = new JSONArray(Json);
        LinkedList<HiBean> list = new LinkedList<HiBean>();

        for(int i =0;i<jsonArray.length();i++){
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            HiBean bean = getBeanFromJson(jsonObj.toString());
            list.add(bean);
        }
        return list;
    }
    public String toJson() throws NoSuchMethodException, JSONException, InvocationTargetException, IllegalAccessException {
        JSONObject jsonObject = new JSONObject();

        for (Object o1 : this.prop.entrySet()) {
            Map.Entry entry = (Map.Entry) o1;
            String key = (String) entry.getKey();
            String getter = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
            System.out.println(getter);
            Method getmethod = classType.getMethod(getter);
            Object value = getmethod.invoke(this);
            System.out.println(key+"="+value);
            jsonObject.put(key, value);//value为null时，不会被放入
        }

        return jsonObject.toString();
    }


    public Properties getProp() {
    	return prop;
    }
    public void setProp(Properties prop) {
    	this.prop = prop;
    }

}
