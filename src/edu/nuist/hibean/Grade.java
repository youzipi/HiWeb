package edu.nuist.hibean;

import java.io.IOException;

/**
 * project_name:java_demo
 * package_name:edu.nuist.hibean
 * user: youzipi
 * date: 2015/1/11 19:48
 */
public class Grade extends HiBean{

    private int id;
    private int course_id;
    private int stu_id;
    private int score;

    public Grade(String className) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        super(className);
    }

    public Grade() throws IOException, ClassNotFoundException {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int couser_id) {
        this.course_id = couser_id;
    }

    public int getStu_id() {
        return stu_id;
    }

    public void setStu_id(int stu_id) {
        this.stu_id = stu_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", course_id=" + course_id +
                ", stu_id=" + stu_id +
                ", score=" + score +
                '}';
    }
}
