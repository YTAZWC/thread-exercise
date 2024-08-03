package top.ytazwc.thread.exercise;

/**
 * @author 花木凋零成兰
 * @title ThreeThreadsPrint123
 * @date 2024-08-03 11:24
 * @package top.ytazwc.thread.exercise
 * @description 三线程分别打印1、2、3，按顺序执行10次
 * 方法二：使用 Synchronized
 */
public class ThreeThreadsPrint123_2 {

    // 计数器：计算当前打印次数
    private int count = 0;
    // 同步锁对象
    private final Object lock = new Object();

    // 打印方法：0打印A，1打印B，2打印C
    public void printNum(int flag) {
        for (int i = 0; i < 10; ++ i) {
            synchronized (lock) {
                while (count % 3 != flag && count < 30) {
                    try {
                        // 若没有轮到当前线程打印 则让当前线程等待
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (count < 30) {
                    System.out.println("线程: " + Thread.currentThread().getName() + " 打印: " + (1 + flag));
                    // 次数增加
                    ++ count;
                    // 唤醒所有线程
                    lock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        ThreeThreadsPrint123_2 obj = new ThreeThreadsPrint123_2();
        Thread t1 = new Thread(() -> obj.printNum(0), "A");
        Thread t2 = new Thread(() -> obj.printNum(1), "B");
        Thread t3 = new Thread(() -> obj.printNum(2), "B");

        t2.start();
        t1.start();
        t3.start();
    }

}
