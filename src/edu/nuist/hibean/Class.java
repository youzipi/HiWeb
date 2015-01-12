package edu.nuist.hibean;

import java.io.IOException;

/**
 * project_name:java_demo
 * package_name:edu.nuist.hibean
 * user: youzipi
 * date: 2015/1/11 19:51
 */
public class Class extends HiBean {

    private int id;
    private String name;

    public Class(String className) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        super(className);
    }

    public Class() throws IOException, ClassNotFoundException {
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
}
