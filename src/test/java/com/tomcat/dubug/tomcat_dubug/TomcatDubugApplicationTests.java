package com.tomcat.dubug.tomcat_dubug;


import org.apache.tomcat.util.threads.LimitLatch;
import org.junit.Test;


public class TomcatDubugApplicationTests {

    private B tail;

	@Test
	public void contextLoads() throws InterruptedException {

        LimitLatch limitLatch = new LimitLatch(0);
        limitLatch.countUpOrAwait();
        System.out.println("hello");

	}

	@Test
    public void ObjectTest(){
        System.out.println();


    }

    public static void main(String[] args) {
        TomcatDubugApplicationTests tests = new TomcatDubugApplicationTests();
        tests.aaa();
    }

    public void aaa(){
        tail = new B("a1");

        A a = new A();
        a.setB(tail);
        a.getB().print();

        tail = new B("a2");

        a.getB().print();
        System.out.println();
    }


    class A{
	    B b;

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }
    }

    class B{
	    String arg;

        public B(String arg) {
            this.arg = arg;
        }

        public void print(){
            System.out.println(this.arg);
        }
    }



}
