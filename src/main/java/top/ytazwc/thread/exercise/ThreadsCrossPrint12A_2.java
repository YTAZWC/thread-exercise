package top.ytazwc.thread.exercise;

import java.util.concurrent.Semaphore;

/**
 * @author 花木凋零成兰
 * @title ThreadsCrossPrint12A
 * @date 2024-08-06 21:31
 * @package top.ytazwc.thread.exercise
 * @description 线程交叉打印12A34B56C等
 * 方法二：使用 semaphore
 */
public class ThreadsCrossPrint12A_2 {

    public static void main(String[] args) {
        Semaphore first = new Semaphore(0);
        Semaphore second = new Semaphore(0);
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 52; i += 2) {
                try {
                    // 先打印数字
                    System.out.print(i);
                    System.out.print(i+1);
                    // 打印后休眠一秒 突出效果
                    Thread.sleep(1000);
                    // 唤醒 second 附近线程
                    second.release();
                    // 再阻塞当前打印数字线程
                    first.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 26; ++ i) {
                try {
                    // 先阻塞当前线程 保证打印数字的线程先打印
                    second.acquire();
                    // 打印字母
                    System.out.print((char)(i + 'A'));
                    // 打印后休眠一秒 突出效果
                    Thread.sleep(1000);
                    // 唤醒打印数字的线程
                    first.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t2");
        t2.start();
        t1.start();
    }
}
