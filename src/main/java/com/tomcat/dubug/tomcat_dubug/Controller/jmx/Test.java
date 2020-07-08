package com.tomcat.dubug.tomcat_dubug.Controller.jmx;

import java.util.logging.Logger;

/**
 * @author linsong.chen
 * @date 2020-07-01 10:36
 */
public class Test {

    private static final Logger logger =
            Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        System.out.println("start");
        Client client = new Client();
        String[] command = {"HeapMemoryUsage"};
        try {
            Object [] result = client.execute("104.224.135.105:9999",
                    null,
                    null, "java.lang:type=Memory",
                    command);

            // Print out results on stdout. Only log if a result.
            if (result != null) {
                for (int i = 0; i < result.length; i++) {
                    if (result[i] != null && result[i].toString().length() > 0) {
                        if (command != null) {
                            logger.info(command[i] + ": " + result[i]);
                        } else {
                            logger.info("\n" + result[i].toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
