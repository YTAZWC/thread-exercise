package top.ytazwc.thread.exercise;

/**
 * @author 花木凋零成兰
 * @title TwoThreadsCrossPrintLetters
 * @date 2024-08-07 10:07
 * @package top.ytazwc.thread.exercise
 * @description 两线程交替打印字母大小写
 * 方法一：使用synchronized
 */
public class TwoThreadsCrossPrintLetters {
    // 锁
    private static final Object LOCK = new Object();

    // 标记当前打印大写还是打印小写
    // true大写  false小写
    private static boolean flag = true;

    // 标记当前打印字母
    private static char letter = 'A';

    private static void printLetters(boolean isUpperCase) {
        while (letter <= 'Z') {
            synchronized (LOCK) {
                while (flag != isUpperCase) {
                    // 若不论到当前线程打印 则令线程等待
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (letter <= 'Z') {
                    try {
                        if (isUpperCase) {
                            System.out.print(letter);
                        } else {
                            System.out.print((char) (letter + 'a' - 'A'));
                        }
                        // 令线程休眠一秒 便于观察
                        Thread.sleep(1000);
                        ++ letter;
                        // 当前线程打印后 轮到另外一个线程打印
                        flag = !flag;
                        LOCK.notifyAll();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> printLetters(true), "t1");
        Thread t2 = new Thread(() -> printLetters(false), "t2");
        t2.start();
        t1.start();
    }
}
