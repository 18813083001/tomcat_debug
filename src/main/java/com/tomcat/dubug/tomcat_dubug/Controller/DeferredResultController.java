package com.tomcat.dubug.tomcat_dubug.Controller;

import freemarker.template.utility.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ForkJoinPool;

@Controller
@RequestMapping(value = "async")
public class DeferredResultController {

    Logger LOG = LoggerFactory.getLogger(DeferredResultController.class);


    @GetMapping("/async-deferredresult")
    public DeferredResult<ResponseEntity<?>> handleReqDefResult(Model model) {
        LOG.info("Received async-deferredresult request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();
        output.onTimeout(new Runnable() {
            @Override
            public void run() {
                System.out.printf("time out callable");
            }
        });

        ForkJoinPool.commonPool().submit(() -> {
            LOG.info("Processing in separate thread");
            try {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
            }
            output.setResult(ResponseEntity.ok("ok"));
        });

//        output.setResult(ResponseEntity.ok("ok"));

//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        LOG.info("servlet thread freed");
        return output;
    }

    @RequestMapping("close")
    public void closeConnection(HttpServletRequest request, HttpServletResponse response){
        try {
            response.getWriter().write("hello"+ new Date());

//            response.getWriter().write("hello2"+ new Date());
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
