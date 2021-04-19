package com.console;

import java.util.concurrent.CountDownLatch;

public class MyThread extends Thread
{

    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    private TestSuite ts;

       
    public MyThread(CountDownLatch startSignal, CountDownLatch doneSignal, TestSuite ts) {
      this.startSignal = startSignal;
      this.doneSignal = doneSignal;
      this.ts = ts;
      this.start();
    }
    
    @Override
    public void run() {
      try {
        startSignal.await();
        	doWork();
        doneSignal.countDown();
      } catch (InterruptedException ex) {} // return;
    }

    void doWork() { RunConsole.console(ts); }
  }
    
    
