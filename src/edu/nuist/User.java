package edu.nuist;

import edu.nuist.hibean.HiBean;
import edu.nuist.util.Configuration;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * project_name:java_demo
 * package_name:edu.nuist
 * user: youzipi
 * date: 2015/1/5 15:27
 */
public class User extends HiBean {
    public User(String className) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        super(className);
    }

    public User() throws ClassNotFoundException, FileNotFoundException, IOException, IllegalAccessException, InstantiationException {
        super();

    }

    private int id;
    private String name;
    private String department;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, JSONException, IllegalAccessException, IOException {
        String Jsons = "[{\"id\":0,\"name\":\"s0\"},{\"id\":1,\"name\":\"s1\"},{\"id\":2,\"name\":\"s2\"}]";
        String Json = "{\"id\":0,\"name\":\"s0\"}";
        Configuration.setWebBeansDefinePath("D:\\Desktop\\HiWeb\\HiWeb\\src\\");
        User bean = new User(User.class.getName());
//        bean.init(User.class.getName());
        bean = (User)bean.getBeanFromJson(Json);
//        User bean = (User)(new HiBean("User").getBeanFromJson(Json));


        System.out.println(bean);

//        LinkedList<HiBean> list = bean.getBeanLinkedListFromJson(Jsons);
        LinkedList<HiBean> list = bean.getBeanLinkedListFromJson(Jsons);
        for(HiBean hibean:list){
            System.out.println((User)hibean);
        }

        System.out.println(bean);
        bean.setName("1111");
        System.out.println(bean.toJson());
    }
}
