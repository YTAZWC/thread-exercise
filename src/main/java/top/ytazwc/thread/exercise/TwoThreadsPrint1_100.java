package top.ytazwc.thread.exercise;

/**
 * @author 花木凋零成兰
 * @title TwoThreadsPrint1_100
 * @date 2024/8/1 11:40
 * @package top.ytazwc.thread.exercise
 * @description 两线程轮流打印1-100
 */
public class TwoThreadsPrint1_100 {

    // 待打印的数
    private int num = 1;
    // 用于同步代码块的锁
    private final Object lock = new Object();

    // 同步打印方法
    public void printNum(boolean isEven) {
        while (num <= 100) {
            synchronized (lock) {
                // 判断当前线程是否应该打印
                // 若当前线程t1 打印偶数但num为奇数时 让t1休眠
                // 若当前线程t2 打印奇数但num为偶数时 让t2休眠
                while ((isEven && num % 2 != 0) || (!isEven && num % 2 == 0)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (num <= 100) {
                    System.out.println("当前线程: " + Thread.currentThread().getName() + "打印: " + num++);
                    lock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 实例化当前对象
        TwoThreadsPrint1_100 obj = new TwoThreadsPrint1_100();
        // 线程1 负责打印奇数
        Thread t1 = new Thread(() -> obj.printNum(false), "t1");
        // 线程2 负责打印偶数
        Thread t2 = new Thread(() -> obj.printNum(true), "t2");
        // 启动线程
        t2.start();
        t1.start();
        t1.join();
        t2.join();
        System.out.println("执行结束!");
    }
}
