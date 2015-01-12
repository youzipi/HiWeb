package edu.nuist.hibean;

import java.io.IOException;

/**
 * project_name:java_demo
 * package_name:edu.nuist.hibean
 * user: youzipi
 * date: 2015/1/11 19:43
 */
public class Student extends HiBean {

    private int id;
    private String name;
    private String detail;
    private int class_id;

    public Student(String className) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        super(className);
    }

    public Student() throws IOException, ClassNotFoundException {
    }

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", class_id=" + class_id +
                '}';
    }
}
