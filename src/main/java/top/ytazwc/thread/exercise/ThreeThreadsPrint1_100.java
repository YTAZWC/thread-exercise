package top.ytazwc.thread.exercise;

/**
 * @author 花木凋零成兰
 * @title ThreeThreadsPrint1_100
 * @date 2024-08-02 16:45
 * @package top.ytazwc.thread.exercise
 * @description 三个线程轮流打印 1-100
 */
public class ThreeThreadsPrint1_100 {

    // 当前打印数
    private int num = 1;
    // 锁
    private final Object lock = new Object();

    // flag 标记应该打印的余数
    // 线程1 打印 num%3 = 1 的数
    // 线程2 打印 num%3 = 2 的数
    // 线程3 打印 num%3 = 0 的数
    public void printNum(int flag) {
        while (num <= 100) {
            synchronized (lock) {
                while (num % 3 != flag && num <= 100) {
                    try {
                        // 当前数 不由当前线程打印 让当前线程休眠
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (num <= 100) {
                    // 打印
                    System.out.println("当前线程: " + Thread.currentThread().getName() + "打印: " + num++);
                    // 唤醒其他线程
                    lock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 实例化对象
        ThreeThreadsPrint1_100 obj = new ThreeThreadsPrint1_100();
        Thread t1 = new Thread(() -> obj.printNum(1), "t1");
        Thread t2 = new Thread(() -> obj.printNum(2), "t2");
        Thread t3 = new Thread(() -> obj.printNum(0), "t3");

        // 不按顺序启动 验证是否成功实现
        t2.start();
        t1.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }

}
