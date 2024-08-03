package top.ytazwc.thread.exercise;

import java.util.concurrent.Semaphore;

/**
 * @author 花木凋零成兰
 * @title ThreeThreadsPrint123
 * @date 2024-08-03 11:24
 * @package top.ytazwc.thread.exercise
 * @description 三线程分别打印1、2、3，按顺序执行10次
 * 方法一：使用 Semaphore
 */
public class ThreeThreadsPrint123 {

    public static void main(String[] args) {
        // 信号量 控制访问资源的线程数量 设置线程数为0
        Semaphore first = new Semaphore(0);
        Semaphore second = new Semaphore(0);
        Semaphore third = new Semaphore(0);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; ++ i) {
                try {
                    // 线程 A 打印 1
                    System.out.println("线程: " + Thread.currentThread().getName() + "打印: 1");
                    // 休眠一秒 便于观察结果
                    Thread.sleep(1000);
                    // release 会释放信号量 并唤醒等待线程
                    second.release();
                    // 尝试访问资源 因为允许0个线程访问 所以会阻塞线程 A
                    first.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "A");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; ++ i) {
                try {
                    // 先阻塞线程B 保证线程A先打印结束后 再唤醒线程B
                    second.acquire();
                    // 线程 B 打印 2
                    System.out.println("线程: " + Thread.currentThread().getName() + "打印: 2");
                    // 休眠一秒 便于观察结果
                    Thread.sleep(1000);
                    // release 会释放信号量 并唤醒等待线程
                    third.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "B");
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10; ++ i) {
                try {
                    // 先阻塞线程C 保证线程B先打印结束后 再唤醒线程C
                    third.acquire();
                    // 线程 C 打印 3
                    System.out.println("线程: " + Thread.currentThread().getName() + "打印: 3");
                    // 休眠一秒 便于观察结果
                    Thread.sleep(1000);
                    // release 会释放信号量 并唤醒等待线程
                    first.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "C");

        t1.start();
        t2.start();
        t3.start();

    }

}
