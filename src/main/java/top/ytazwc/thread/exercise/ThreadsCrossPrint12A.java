package top.ytazwc.thread.exercise;

/**
 * @author 花木凋零成兰
 * @title ThreadsCrossPrint12A
 * @date 2024-08-06 21:31
 * @package top.ytazwc.thread.exercise
 * @description 线程交叉打印12A34B56C等
 * 方法一：使用 synchronized
 */
public class ThreadsCrossPrint12A {

    // 打印标记 true打印数字 false打印字母
    private static boolean flag = true;

    // 锁
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 52; i += 2) {
                synchronized (LOCK) {
                    while (!flag) {
                        // 当前轮到打印字母时 让打印数字的线程等待
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    // 执行打印
                    System.out.print(i);
                    System.out.print(i+1);
                    // 休眠1秒 效果更明显
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // 打印数字结束后 轮到打印字母
                    flag = false;
                    LOCK.notifyAll();
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int c = 0; c < 26; ++ c) {
                synchronized (LOCK) {
                    while (flag) {
                        // 当前轮到打印数字时 让打印字母的线程等待
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    // 执行打印
                    System.out.print((char)  (c + 'A'));
                    // 休眠1秒 效果更明显
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // 打印字母结束后 轮到打印数字
                    flag = true;
                    LOCK.notifyAll();
                }
            }
        }, "t2");
        t2.start();
        t1.start();
    }

}
