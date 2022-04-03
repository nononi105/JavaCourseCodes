package io.nononi.starter;

import java.util.ArrayList;
import java.util.List;

public class Klass {
    private int id;
    List<Student> students;

    public Klass(int id) {
        this.id = id;
        this.students = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public String toString() {
        return "Klass{" +
                "id=" + id +
                ", students=" + students +
                '}';
    }
}
