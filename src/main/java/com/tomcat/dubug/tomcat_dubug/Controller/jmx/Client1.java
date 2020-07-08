package com.tomcat.dubug.tomcat_dubug.Controller.jmx;


/*
 * Client.java - JMX client that interacts with the JMX agent. It gets
 * attributes and performs operations on the Hello MBean and the QueueSampler
 * MXBean example. It also listens for Hello MBean notifications.
 */
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * @author linsong.chen
 * @date 2020-06-30 22:10
 */

public class Client1 {

    /**
     * Inner class that will handle the notifications.
     */
    public static class ClientListener implements NotificationListener {
        public void handleNotification(Notification notification,
                                       Object handback) {
            echo("\nReceived notification:");
            echo("\tClassName: " + notification.getClass().getName());
            echo("\tSource: " + notification.getSource());
            echo("\tType: " + notification.getType());
            echo("\tMessage: " + notification.getMessage());
            if (notification instanceof AttributeChangeNotification) {
                AttributeChangeNotification acn =
                        (AttributeChangeNotification) notification;
                echo("\tAttributeName: " + acn.getAttributeName());
                echo("\tAttributeType: " + acn.getAttributeType());
                echo("\tNewValue: " + acn.getNewValue());
                echo("\tOldValue: " + acn.getOldValue());
            }
        }
    }

    /* For simplicity, we declare "throws Exception".
       Real programs will usually want finer-grained exception handling. */
    public static void main(String[] args) throws Exception {
        // Create an RMI connector client and
        // connect it to the RMI connector server
        //
        long starttime = System.currentTimeMillis();
        echo("\nCreate an RMI connector client and " +
                "connect it to the RMI connector server");
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
        long urlend = System.currentTimeMillis();
        System.out.println("get url:"+(urlend - starttime));

        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        long jmxend = System.currentTimeMillis();
        System.out.println("get connector:"+(jmxend - urlend));
        // Create listener
        //
        ClientListener listener = new ClientListener();

        // Get an MBeanServerConnection
        //

        echo("\nGet an MBeanServerConnection");
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
//        waitForEnterPressed();

        long mbscend = System.currentTimeMillis();
        System.out.println("get connection:"+(mbscend - jmxend));

        // Get domains from MBeanServer
        //
        echo("\nDomains:");
        String domains[] = mbsc.getDomains();
        Arrays.sort(domains);
        for (String domain : domains) {
            echo("\tDomain = " + domain);
        }
//        waitForEnterPressed();

        // Get MBeanServer's default domain
        //
        echo("\nMBeanServer default domain = " + mbsc.getDefaultDomain());

        // Get MBean count
        //
        echo("\nMBean count = " + mbsc.getMBeanCount());

        // Query MBean names
        //
        echo("\nQuery MBeanServer MBeans:");
        Set<ObjectName> names =
                new TreeSet<>(mbsc.queryNames(null, null));
        for (ObjectName name : names) {
            echo("\tObjectName = " + name);
        }

        System.out.println("----");

        printAttr(mbsc);

        int i = 0;
        while (i < 10){
            i++;
            waitForEnterPressed();

            printAttr(mbsc);
        }


        jmxc.close();
        echo("\nBye! Bye!");
    }


    private static void printAttr(MBeanServerConnection mbsc){
        try {
            ObjectName mbeanName = new ObjectName("com.example:type=Hello");
            Object o = ( mbsc.getAttribute(mbeanName,"CacheSize"));
            System.out.println(o);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        }

//        try {
//            ObjectName mbeanName = new ObjectName("java.lang:type=Memory,name=HeapMemoryUsage");
//            Object o = mbsc.queryNames(mbeanName, null);
//            System.out.println(o);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (MalformedObjectNameException e) {
//            e.printStackTrace();
//        }
    }


    private static void echo(String msg) {
        System.out.println(msg);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void waitForEnterPressed() {
        try {
            echo("\nPress <Enter> to continue...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

