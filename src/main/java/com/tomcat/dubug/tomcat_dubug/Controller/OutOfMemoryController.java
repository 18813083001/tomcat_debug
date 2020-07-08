package com.tomcat.dubug.tomcat_dubug.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author linsong.chen
 * @date 2020-06-29 10:17
 */
@Controller
@RequestMapping(value = "memory")
public class OutOfMemoryController {

    Map bytesMemory = new ConcurrentHashMap();

    AtomicInteger key = new AtomicInteger(0);

    /**
     * @param memorySeize 需要增加的内存大小，1代表单位1K
     * */
    @RequestMapping(value = "increase")
    public ResponseEntity increaseMemory(@RequestParam(defaultValue = "100") int memorySeize){

        for (int i=0; i < memorySeize;i ++){

            //1 M
            byte[] bytes = new byte[1024];
            for (int b = 0; b < bytes.length;b++){
                bytes[b] = 1;
            }
            bytesMemory.put(key.getAndIncrement(),bytes);
        }

        java.lang.management.MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");

        Map memoryMap = new HashMap();
        memoryMap.put(ft.format(dNow) + " -> Init", mu.getInit());
        memoryMap.put("Used",mu.getUsed());
        memoryMap.put("Committed", mu.getCommitted());
        memoryMap.put("Max", mu.getMax());
        return ResponseEntity.ok(memoryMap);
    }


    @RequestMapping(value = "decrease")
    public ResponseEntity decreaseMemory(@RequestParam(defaultValue = "100") int memorySeize){

        for (int i= 0;i < memorySeize;i++){
            bytesMemory.remove(key.getAndDecrement());
        }

        java.lang.management.MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
        Map memoryMap = new HashMap();
        memoryMap.put(ft.format(dNow) + " -> Init", mu.getInit());
        memoryMap.put("Used",mu.getUsed());
        memoryMap.put("Committed", mu.getCommitted());
        memoryMap.put("Max", mu.getMax());
        return ResponseEntity.ok(memoryMap);

    }
}
