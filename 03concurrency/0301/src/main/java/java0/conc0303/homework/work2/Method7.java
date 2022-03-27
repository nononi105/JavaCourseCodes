package java0.conc0303.homework.work2;

import java.util.concurrent.CountDownLatch;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class Method7 {
    
    public static void main(String[] args) {
        
        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        CountDownLatch countDownLatch = new CountDownLatch(1);
        FiboRunnable fiboRunnable = new FiboRunnable(countDownLatch);
        Thread thread = new Thread(fiboRunnable);
        thread.start();

        try {
            countDownLatch.await();
            System.out.println(fiboRunnable.getResult());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        
        // 然后退出main线程
    }
    
    private static int sum() {
        return fibo(36);
    }
    
    private static int fibo(int a) {
        if ( a < 2) 
            return 1;
        return fibo(a-1) + fibo(a-2);
    }

    public static class FiboRunnable implements Runnable{
        private int result;
        private CountDownLatch countDownLatch;

        public FiboRunnable(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            result = sum();
            countDownLatch.countDown();
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }
}
