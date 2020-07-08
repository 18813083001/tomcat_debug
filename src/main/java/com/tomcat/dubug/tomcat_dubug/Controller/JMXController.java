package com.tomcat.dubug.tomcat_dubug.Controller;

import com.tomcat.dubug.tomcat_dubug.Controller.jmx.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * @author linsong.chen
 * @date 2020-06-30 22:08
 */
@RestController
@RequestMapping("jmx")
public class JMXController {

    org.slf4j.Logger logger = LoggerFactory.getLogger(JMXController.class);

    @Autowired
    private CacheClient cacheClient;

    /**
     * 参数的格式和cmdline-jmxclient的参数格式一样
     * @param userpass 用户名密码
     * @param hostport 主机名:端口，如 10.19.14.195:20011
     * @param beanname 需要查询的bean名称，如 java.lang:type=Memory
     * @param command 操作命令，如 HeapMemoryUsage，command可能包含多个命令，用空格隔开
     * @return 返回jmx的调用结果，返回结果的格式和cmdline-jmxclient一样
     * */
    @RequestMapping(value = "info")
    public String getInfo(String userpass,@RequestParam String hostport,String beanname,String command) throws Exception {

        int argsLength = 3;
        //将userpass、hostport、beanname、command拼到一个数组中，command可能包含多个命令，用空格隔开
        String[] args;
        if (command != null){
            String[] commands = command.split(" ");
            argsLength += commands.length;
            args = new String[argsLength];
            args[0] = userpass;
            args[1] = hostport;
            args[2] = beanname;
            for (int i = 0;i < commands.length;i++){
                args[3+i] = commands[i];
            }
        }else {
            args = new String[argsLength];
            args[0] = userpass;
            args[1] = hostport;
            args[2] = beanname;
        }

        // Set the logger to use our all-on-one-line formatter.
        Logger l = Logger.getLogger("");
        Handler[] hs = l.getHandlers();
        for (int i = 0; i < hs.length; i++) {
            Handler h = hs[0];
            if (h instanceof ConsoleHandler) {
                h.setFormatter(cacheClient.new OneLineSimpleLogger());
            }
        }
        String result = cacheClient.execute(args);

        return result;
    }

    /**
     * 判断java进程的容器类型，如Tomcat,netty,jetty等
     * */
    @PostMapping(value = "container/type")
    public Map<String,List<Map>> checkContainerType(@Valid @RequestBody ContainerCheck containerCheck){
        List<Map> resultList = new ArrayList<>();
        for (Container container : containerCheck.getCheckContainerList()){
            String host = container.getServiceHost();
            String jmxPort = container.getServicePort();
            if (StringUtils.isEmpty(host) || StringUtils.isEmpty(jmxPort)){
                logger.info("host or jmxport is empty");
                continue;
            }
            if (ContainnerEnum.tomcat.name().equals(container.getContainerType())){
                String beanName = "Tomcat:type=Server";
                try {
                    getInfo(null,host+":"+jmxPort,beanName,null);
                    //不抛异常说明是tomcat
                    Map map = new HashMap(4);
                    map.put("{#CONTAINERTYPE}",ContainnerEnum.tomcat.name());
                    map.put("{#SERVICEHOST}",host);
                    map.put("{#SERVICEPORT}",jmxPort);
                    map.put("{#SERVICENAME}",container.getServiceName());
                    resultList.add(map);
                } catch (Exception e) {
                    //不是tomcat,忽略
                    logger.info("{}:{},{}服务不是Tomcat服务,reason:{}",host,jmxPort,container.getServiceName(),e.getMessage());
                }
            }
        }
        Map result = new HashMap(1);
        result.put("data",resultList);
        return result;
    }

    /**
     * 查询jmx连接数
     * */
    @RequestMapping(value = "connector/size")
    public Map<String,Integer> getJmxConnectorSize(){
        Map sizeMap = new HashMap();
        sizeMap.put("connectorSize",cacheClient.getConnectorSize());
        return sizeMap;
    }

    /**
     * 查询jmx连接的key集合
     * */
    @RequestMapping(value = "connector/keys")
    public Set<String> getConnectorKeys(){
        return cacheClient.getConnectorKeys();
    }

}
