package com.tomcat.dubug.tomcat_dubug;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author linsong.chen
 * @date 2020-05-07 12:46
 */
public class UnsafeTest {

//    private static final Unsafe unsafe = Unsafe.getUnsafe();
//
    private Object head;
//
//    private String tail;
//
//    private static final long a;
//    private static final long b;
//
//    static {
//        try {
//            a = unsafe.objectFieldOffset
//                    (UnsafeTest.class.getDeclaredField("head"));
//            b = unsafe.objectFieldOffset
//                    (UnsafeTest.class.getDeclaredField("tail"));
//
//        } catch (Exception ex) { throw new Error(ex); }
//    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        System.out.println("hello");
        UnsafeTest unsafeTest = new UnsafeTest();
        Unsafe unsafe = getUnsafe();

        long a = unsafe.objectFieldOffset
                (UnsafeTest.class.getDeclaredField("head"));

        unsafe.compareAndSwapObject(unsafeTest, a, null, new UnsafeTestVo());


        UnsafeTestVo unsafeTestVo = new UnsafeTestVo();
        unsafeTestVo.setB("aaaa");
        unsafe.putObject(unsafeTest, a, unsafeTestVo);
        Object o = unsafe.getObjectVolatile(unsafeTest, a);


        System.out.println();

    }

    public static Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field f =Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
       return  (Unsafe) f.get(null);
    }
}
