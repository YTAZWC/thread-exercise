package top.ytazwc.thread.exercise;

/**
 * @author 花木凋零成兰
 * @title TwoThreadsPrinta1_d4TenRounds
 * @date 2024-08-12 10:22
 * @package top.ytazwc.thread.exercise
 * @description 两线程交替打印a1b2c3d4十轮
 */
public class TwoThreadsPrinta1_d4TenRounds {

    // 锁
    private static final Object LOCK = new Object();

    // 标记当前应该打印字母还是数字 true-字母 false-数字
    private static boolean letterOrNumber = true;

    // 打印方法
    private static void printLetterAndNumber(boolean flag) {
        for (int i = 0; i < 40; ++ i) {
            synchronized (LOCK) {
                while (flag != letterOrNumber) {
                    // 不由当前线程打印 令其等待
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 判断打印字母还是数字
                if (letterOrNumber) {
                    System.out.printf("%c", i%4 + 'a');
                } else {
                    System.out.printf("%d", i%4 + 1);
                }
                // 标记轮到另外线程打印
                letterOrNumber = !letterOrNumber;
                // 唤醒其他线程
                LOCK.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> printLetterAndNumber(true), "t1");
        Thread t2 = new Thread(() -> printLetterAndNumber(false), "t2");
        t2.start();
        t1.start();
    }
}
