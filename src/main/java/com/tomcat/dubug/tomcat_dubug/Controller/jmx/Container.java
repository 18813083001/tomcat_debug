package com.tomcat.dubug.tomcat_dubug.Controller.jmx;


/**
 * @author linsong.chen
 * @date 2020-07-07 18:38
 */
public class Container {

    /**
     * 主机
     * */
    private String serviceHost;

    /**
     * 服务名称
     * */
    private String serviceName;

    /**
     * 端口
     * */
    private String servicePort;

    /**
     * 容器类型：tomcat,netty,jetty等
     * */
    private String containerType;

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePort() {
        return servicePort;
    }

    public void setServicePort(String servicePort) {
        this.servicePort = servicePort;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }
}
