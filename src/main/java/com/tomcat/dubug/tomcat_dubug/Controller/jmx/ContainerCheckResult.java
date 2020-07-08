package com.tomcat.dubug.tomcat_dubug.Controller.jmx;

/**
 * @author linsong.chen
 * @date 2020-07-07 19:37
 */
public class ContainerCheckResult {

    /**
     * 容器类型：tomcat,netty,jetty等
     * */
    private String CONTAINERTYPE;

    /**
     * 服务名称
     * */
    private String SERVICENAME;

    /**
     * 主机
     * */
    private String SERVICEHOST;

    /**
     * 端口
     * */
    private String SERVICEPORT;

    public String getCONTAINERTYPE() {
        return CONTAINERTYPE;
    }

    public void setCONTAINERTYPE(String CONTAINERTYPE) {
        this.CONTAINERTYPE = CONTAINERTYPE;
    }

    public String getSERVICENAME() {
        return SERVICENAME;
    }

    public void setSERVICENAME(String SERVICENAME) {
        this.SERVICENAME = SERVICENAME;
    }

    public String getSERVICEHOST() {
        return SERVICEHOST;
    }

    public void setSERVICEHOST(String SERVICEHOST) {
        this.SERVICEHOST = SERVICEHOST;
    }

    public String getSERVICEPORT() {
        return SERVICEPORT;
    }

    public void setSERVICEPORT(String SERVICEPORT) {
        this.SERVICEPORT = SERVICEPORT;
    }
}
