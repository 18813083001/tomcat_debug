package com.tomcat.dubug.tomcat_dubug.Controller;

import org.apache.catalina.connector.ResponseFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="tomcat")
public class DeubugController {

    Logger logger = LoggerFactory.getLogger("test");

    @RequestMapping(value="debug")
    public ResponseEntity dubug(HttpServletRequest request, HttpServletResponse response){
        try {
//            response.getWriter().println("hello");
//            response.getWriter().flush();
//            ((ResponseFacade)request).getOutputStream().write("helloOutputStream".getBytes());
            if (ResponseFacade.class.isAssignableFrom(response.getClass())){
                response.getOutputStream().write("helloOutputStream".getBytes());
            }
//            try {
//                Thread.sleep(10000000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            URL url = null;
            try {
                url = new URL("http://10.19.9.94:18081/tomcat/debug");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(3000);
                con.setConnectTimeout(3000);
                con.setRequestMethod("GET");
                InputStream inputStream = con.getInputStream();
                byte[] b = new byte[40];
                inputStream.read(b);
//                System.out.println("线程"+j+" "+new java.lang.String(b));
            } catch (IOException e) {
                //e.printStackTrace();
            }

//            System.out.println(Thread.currentThread().getName());
//            request.getParts();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("test");
        }
        return ResponseEntity.ok("tomcat dubug3486598364583648658436584689");
    }


    @RequestMapping(value = "/cars", method = RequestMethod.GET)
    public String init(@ModelAttribute("model") ModelMap model) {
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("make","make");
        map.put("model","model");
        model.addAttribute("carList", list);
        return "index";
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
