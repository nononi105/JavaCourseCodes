package io.nononi.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(SchoolProperties.class)
@ConditionalOnProperty(prefix = "school", value = "enabled", havingValue = "true")
@PropertySource("classpath:application.properties")
public class SchoolAutoConfiguration {

    @Autowired
    private SchoolProperties schoolProperties;

    @Bean
    public School school() {
        List<Integer> studentIds = schoolProperties.getStudentIds();
        List<String> studentNames = schoolProperties.getStudentNames();
        List<Integer> klassIds = schoolProperties.getKlassIds();
        List<Map<String, Integer>> studentKlassRelation = schoolProperties.getStudentKlassRelation();

        List<Student> students = new ArrayList<>(studentIds.size());
        for (int i = 0; i < studentIds.size(); i++) {
            students.add(new Student(studentIds.get(i), studentNames.get(i)));
        }

        List<Klass> klasses = new ArrayList<>();
        for (int i = 0; i < klassIds.size(); i++) {
            klasses.add(new Klass(klassIds.get(i)));
        }

        for (Map relation : studentKlassRelation) {
            klasses.get((Integer) relation.get("klassId")).addStudent(students.get((Integer) relation.get("studentId")));
        }

        System.out.println(studentIds.toString());
        System.out.println(studentNames.toString());
        System.out.println(klassIds.toString());
        System.out.println(studentKlassRelation.toString());

        return new School(klasses);
    }
}
