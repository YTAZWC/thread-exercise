package top.ytazwc.thread.exercise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 花木凋零成兰
 * @title ThreadsAccumulate
 * @date 2024-08-04 11:52
 * @package top.ytazwc.thread.exercise
 * @description 100个线程各累加100次，保证线程安全
 */
public class ThreadsAccumulate {

    // 计数器 原子类
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        // 创建线程数为100的线程池
        ExecutorService executor = Executors.newFixedThreadPool(100);
        // 提交一百个任务 每个任务执行100次
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    // 使用原子操作递增 线程安全
                    COUNTER.incrementAndGet();
                }
            });
        }
        // 关闭线程池 不接受新的任务 但会继续执行等待队列中的任务
        executor.shutdown();
        // 等待所有任务完成
        executor.awaitTermination(1, TimeUnit.HOURS);
        // 输出最终计数值
        System.out.println(COUNTER.get());
    }

}
