package edu.nuist.hibean;

import java.io.IOException;

/**
 * project_name:java_demo
 * package_name:edu.nuist.hibean
 * user: youzipi
 * date: 2015/1/11 19:53
 */
public class RP extends HiBean {

    private int stu_id;
    private String reason;
    private String type;
    private String detail;

    public RP(String className) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        super(className);
    }

    public RP() throws IOException, ClassNotFoundException {
    }

    public int getStu_id() {
        return stu_id;
    }

    public void setStu_id(int stu_id) {
        this.stu_id = stu_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
