package com.tomcat.dubug.tomcat_dubug.Controller.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.management.remote.JMXConnector;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linsong.chen
 * @date 2020-07-01 12:07
 *
 * 该类的目的缓存jmx连接
 */
@Component
public class CacheClient extends Client implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(CacheClient.class);

    /**
     * 因为创建一个连接的创建时间和cpu消耗的成本是最大的，所以缓存jmx的连接，
     * JMXConnector代表客户端到服务端的连接，是一个tcp类型的连接，
     * 当连接创建后，JMXConnector会和服务器保持心跳，默认是60秒一次心跳
     * */
    private Map<String,JMXConnector> cacheConnector = new ConcurrentHashMap<>();

    /**
     * 心跳线程，检查cacheConnector中的JMXConnector连接是否有效，如果无效就关闭连接，然后删除
     * */
    private Thread heartbeatThread;

    // 状态
    public final static int RUNNING = 0;
    public final static int STOP  = 1;

    private int state = RUNNING;

    //所有连接的心跳时间，毫秒
    @Value("${gofun.jmx.client.all.period:30}")
    private long period;

    //单个连接的心跳时间，毫秒
    @Value("${gofun.jmx.client.single.period:0}")
    private long connectionPeriod;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (period > 0) {
            Checker checker = new Checker();

            Thread t = new Thread(checker, "JMX cache client heartbeat");
            t.setDaemon(true);
            t.start();
        }
    }

//    /**
//     * @param period 心跳时间,毫秒
//     * */
//    public static CacheClient getCacheClient(long period){
//        if (cacheClient == null){
//            synchronized (CacheClient.class){
//                if (cacheClient == null){
//                    cacheClient = new CacheClient(period);
//                }
//            }
//        }
//        return cacheClient;
//    }

    @Override
    protected JMXConnector getJMXConnector(String hostport, String login, String password) throws IOException {
        String connectorKey = generateConnectorKey(hostport,login,password);
        JMXConnector jmxConnector = cacheConnector.get(connectorKey);
        if (jmxConnector == null){
            synchronized (connectorKey){
                if (jmxConnector == null){
                    jmxConnector = super.getJMXConnector(hostport, login, password);
                    cacheConnector.put(connectorKey,jmxConnector);
                }
            }
        }
        return jmxConnector;
    }



    /**
     * 根据hostport+login+password拼成缓存key
     * */
    protected String generateConnectorKey(String hostPort, String login, String password){
        StringBuilder builder = new StringBuilder();
        builder.append(hostPort);
        if (login != null){
            builder.append(login);
        }
        if (password != null){
            builder.append(password);
        }
        return builder.toString();
    }

    @Override
    public void closeJmxConnection(String hostPort, String login, String password) {
        String key = generateConnectorKey(hostPort,login,password);
        cacheConnector.remove(key);
    }

    @Override
    public long getConnectionHeartbeatPeriod() {
        return connectionPeriod;
    }

    public int getConnectorSize(){
        return this.cacheConnector.size();
    }

    public Set<String> getConnectorKeys(){
        return this.cacheConnector.keySet();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private class Checker implements Runnable{

        @Override
        public void run() {
            heartbeatThread = Thread.currentThread();
            while (state == RUNNING && !heartbeatThread.isInterrupted()){
                try {
                    Thread.sleep(period);
                } catch (InterruptedException ire) {
                    // OK.
                    // 下一步检查状态
                }

                if (state == STOP || heartbeatThread.isInterrupted()) {
                    break;
                }

                Iterator<Map.Entry<String,JMXConnector>> iterator = cacheConnector.entrySet().iterator();

                Map.Entry<String,JMXConnector> entry;
                while (iterator.hasNext()){
                    entry = iterator.next();
                    try {
                        if (logger.isDebugEnabled())
                            logger.debug("CacheClient-checkConnection,ConnectionId is:{}"
                                    ,entry.getKey());
                        checkConnection(entry.getValue());
                    }catch (Exception e){
                        //发生异常则认为该连接已经无效，然后删除该连接
                        iterator.remove();
                    }
                }
            }
        }
    }

    protected void checkConnection(JMXConnector jmxConnector) throws IOException {
        if (logger.isDebugEnabled())
            logger.debug("CacheClient-checkConnection,Calling method:{}"
                    ,"getDefaultDomain.");

        jmxConnector.getMBeanServerConnection().getDefaultDomain();
    }
}
