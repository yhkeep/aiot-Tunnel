/*package com.example.demo.service.guavaTest;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitorDemo {
    //生产者、消费者共同维护的缓冲区
    private LinkedList<Integer> buffer = new LinkedList<>();
    //设置缓冲区的大小为10
    private static final int MAX_BUFFER_SIZE = 10;
    //生产或消费数据的编号
    private static AtomicInteger count = new AtomicInteger(0);

    private Monitor monitor = new Monitor();

    //生产者向缓冲区中生产数据
    public void produce(int value) {
        try {
            //enterWhen相当于synchronized的加锁操作；当参数enterWhen()的参数为true时，monitor就会给共享资源加锁并阻塞其他线程
            monitor.enterWhen(monitor.newGuard(() -> buffer.size() < MAX_BUFFER_SIZE));
            buffer.addLast(value);
        } catch (InterruptedException e) {
            System.out.println("生产：" + value+",缓冲区剩余大小："+buffer.size());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //monitor.leave()相当于结束synchronized的加锁操作（即释放锁）
            monitor.leave();
        }
    }

    //消费者从缓冲区中消费数据
    public int consume() {
        try {
            monitor.enterWhen(monitor.newGuard(() -> !buffer.isEmpty()));
            int value = buffer.removeFirst();
            System.out.println("消费：" + value+",缓冲区剩余大小："+buffer.size());
            return value;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            monitor.leave();
        }
    }

    //测试程序
    public static void main(String[] args) {
        MonitorDemo demo = new MonitorDemo();
        //创建有6个线程的线程池
        ExecutorService pool = Executors.newFixedThreadPool(6);//定义线程数
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(pool);
        //向线程池中提交3个生产数据的线程任务
        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                while (true) {
                    int result = count.getAndIncrement();
                    demo.produce(result);
                }
            });
        }
        //向线程池中提交3个消费数据的线程任务
        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {

                while (true) {
                    Integer result = demo.consume();
                }
            });
        }
        try {
            Thread.sleep(2000);//模拟其他业务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }

}*/