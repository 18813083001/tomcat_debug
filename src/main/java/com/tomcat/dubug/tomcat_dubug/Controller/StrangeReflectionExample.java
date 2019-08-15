package com.tomcat.dubug.tomcat_dubug.Controller;

import java.io.File;
import java.lang.reflect.Field;

public class StrangeReflectionExample {

    public Character aCharacter;
    public static void main(String... args) throws Exception {
        StrangeReflectionExample instance = new StrangeReflectionExample();
////        Field field = StrangeReflectionExample.class.getField("aCharacter");
////        Field type = Field.class.getDeclaredField("type");
////        type.setAccessible(true);
////        type.set(field, String.class);
////        field.set(instance, 'A');
////        System.out.println(instance.aCharacter);
//        System.setSecurityManager( new FascistSecurityManager( ) );
//        SecurityManager securityManager = System.getSecurityManager();
//        File file = new File("/Users/chenlinsongs/apache-tomcat-8.5.42.zip");
//        securityManager.checkRead("/Users/chenlinsongs/apache-tomcat-8.5.42.zip");
//        System.out.println(file.getAbsoluteFile());

        instance.aCharacter = '1';
        instance.testRun();

    }

    public void testRun(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        this.run();
                    }
                }
        ).start();
    }

    public void run(){
        System.out.println("run: "+aCharacter);
    }


}


class FascistSecurityManager extends SecurityManager { }
