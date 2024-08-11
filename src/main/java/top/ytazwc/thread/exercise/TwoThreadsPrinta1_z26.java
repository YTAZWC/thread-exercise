package top.ytazwc.thread.exercise;

/**
 * @author 花木凋零成兰
 * @title TwoThreadsPrint a1_z26
 * @date 2024-08-11 15:03
 * @package top.ytazwc.thread.exercise
 * @description 两个线程交替打印a1b2...z26
 */
public class TwoThreadsPrinta1_z26 {
    // 标记当前线程打印字母还是数字
    private static boolean isLetter = true;

    // 同步锁
    private static final Object LOCK = new Object();

    // 打印方法
    private static void printLetterAndNumber(boolean flag) {
        for (int i = 0; i < 26; ++ i) {
            synchronized (LOCK) {
                // 没有轮到当前线程打印时 令线程休眠
                while (flag != isLetter) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (isLetter) {
                    System.out.printf("%c", 'a' + i);
                } else {
                    System.out.printf("%d", i+1);
                }
                // 当前线程打印结束后 切换另外线程打印
                isLetter = !isLetter;
                // 当前线程打印结束后 唤醒其他线程
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
