package edu.nuist;

import edu.nuist.hibean.HiBean;

import java.io.IOException;

/**
 * project_name:java_demo
 * package_name:edu.nuist
 * user: youzipi
 * date: 2015/1/9 19:37
 */
public class Department extends HiBean{

    private String id;
    private String name;

    public Department(String className) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        super(className);
    }

    public Department() throws IOException, ClassNotFoundException {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
