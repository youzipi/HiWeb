package edu.nuist.hibean;

import java.io.IOException;

/**
 * project_name:java_demo
 * package_name:edu.nuist.hibean
 * user: youzipi
 * date: 2015/1/11 19:44
 */
public class Course extends HiBean {
    private int id;
    private String name;
    private String teacher_id;
    private String detail;



    public Course(String className) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        super(className);
    }

    public Course() throws IOException, ClassNotFoundException {
    }
}
