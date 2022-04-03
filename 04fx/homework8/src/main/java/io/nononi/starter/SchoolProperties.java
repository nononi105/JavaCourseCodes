package io.nononi.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "school")
public class SchoolProperties {
    private List<Integer> studentIds;
    private List<String> studentNames;
    private List<Integer> klassIds;
    private List<Map<String, Integer>> studentKlassRelation;

    public List<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Integer> studentIds) {
        this.studentIds = studentIds;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }

    public List<Integer> getKlassIds() {
        return klassIds;
    }

    public void setKlassIds(List<Integer> klassIds) {
        this.klassIds = klassIds;
    }

    public List<Map<String, Integer>> getStudentKlassRelation() {
        return studentKlassRelation;
    }

    public void setStudentKlassRelation(List<Map<String, Integer>> studentKlassRelation) {
        this.studentKlassRelation = studentKlassRelation;
    }
}
