package com.tomcat.dubug.tomcat_dubug.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="tomcat")
public class DeubugController {

    @RequestMapping(value="dubug")
    public ResponseEntity dubug(){
        return ResponseEntity.ok("tomcat dubug");
    }
}
