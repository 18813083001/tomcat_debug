package com.tomcat.dubug.tomcat_dubug.Controller;

import org.apache.catalina.connector.ResponseFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="tomcat")
public class DebugController {

    @RequestMapping(value="debug")
    public ResponseEntity dubug(HttpServletRequest request, HttpServletResponse response){
        try {
//            response.getWriter().println("hello");
//            response.getWriter().flush();
//            ((ResponseFacade)request).getOutputStream().write("helloOutputStream".getBytes());
            if (ResponseFacade.class.isAssignableFrom(response.getClass())){
                response.getOutputStream().write("helloOutputStream".getBytes());
            }
//            request.getParts();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("tomcat dubug3486598364583648658436584689");
    }


    /**
     * 测试阿里的arthas
     * */
    @RequestMapping("arthas")
    public ResponseEntity arthas(@RequestParam(value = "list") List<String> list,String param){
        System.out.println(list.toArray());
        System.out.println(param);
        HashMap map = new HashMap();
        map.put("list",list);
        map.put("param",param);
        return ResponseEntity.ok(map);
    }



    class Car{
        private String make;
        private String model;

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }

}
