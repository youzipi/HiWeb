package edu.nuist.hibean;

import edu.nuist.util.Configuration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.Class;
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

    public HiBean(String className) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        System.out.println("HiBean:className="+className);
        this.classType =  Class.forName(className);
        String simpleName = this.classType.getSimpleName().toLowerCase();
        this.prop.load(new FileInputStream(Configuration.webBeansClassPath + simpleName + Configuration.beanPropFileExt));

    }

    public HiBean() throws IOException, ClassNotFoundException {
        String className = this.getClass().getName();
        this.classType =  Class.forName(className);
        String simpleName = this.classType.getSimpleName().toLowerCase();
//        System.out.println(Configuration.webBeansClassPath + simpleName + Configuration.beanPropFileExt);
        this.prop.load(new FileInputStream(Configuration.webBeansClassPath + simpleName + Configuration.beanPropFileExt));

    }

    /**
     *
     * @param Json
     * @return HiBean
     * @throws JSONException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
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
        return getListfromjsonArr(jsonArray);
    }

    public LinkedList<HiBean> getListfromjsonArr(JSONArray jsonArray) throws JSONException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        LinkedList<HiBean> list = new LinkedList<HiBean>();

        for(int i =0;i<jsonArray.length();i++){
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            HiBean bean = getBeanFromJson(jsonObj.toString());
            list.add(bean);
        }
        return list;
    }


    /**
     *
     * @return
     * @throws NoSuchMethodException
     * @throws JSONException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String toJson() throws NoSuchMethodException, JSONException, InvocationTargetException, IllegalAccessException {
//        System.out.println("toooJson");
        return toJsonObject().toString();

    }

    /**
     *
     * @return
     * @throws NoSuchMethodException
     * @throws JSONException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public JSONObject toJsonObject() throws NoSuchMethodException, JSONException, InvocationTargetException, IllegalAccessException {
        JSONObject jsonObject = new JSONObject();

        for (Object o1 : this.prop.entrySet()) {
            Map.Entry entry = (Map.Entry) o1;
            String key = (String) entry.getKey();
            String getter = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
//            System.out.println(getter);
            Method getmethod = classType.getMethod(getter);
            Object value = getmethod.invoke(this);
//            System.out.println(key+"="+value);
            Object value2 = (value == null)?"null":value;
            jsonObject.put(key, value2);
//            jsonObject.put(key, value);//value为null时，不会被放入
        }
//        System.out.println(jsonObject);
        return jsonObject;
    }


    public Properties getProp() {
    	return prop;
    }
    public void setProp(Properties prop) {
    	this.prop = prop;
    }

}
