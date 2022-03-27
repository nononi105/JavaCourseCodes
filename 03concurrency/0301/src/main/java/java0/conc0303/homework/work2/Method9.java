package java0.conc0303.homework.work2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class Method9 {
    final static ReentrantLock lock = new ReentrantLock();
    final static Condition condition = lock.newCondition();
    static int result;
    
    public static void main(String[] args) {
        
        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    result = sum();
                    condition.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }

            }
        });
        thread.start();


        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        System.out.println(result);

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


}
