package io.nononi.starter.startertest;

import io.nononi.starter.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
public class TestController {
    @Autowired
    private School school;

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return school.toString();
    }
}
