package com.evans.common;

import java.util.concurrent.*;

import com.evans.view.MainClient;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.log4j.Logger;

public class Constants {

    // 创建线程池
    // public static final BlockedThreadPool executorService = BlockedThreadPool.createBlockedThreadPool(THREAD_NUM, "Evans");

    // 创建日志
    public static final Logger LOGGER = Logger.getLogger(MainClient.class);


    //获取系统处理器个数，作为线程池数量
    private static final int nThreads = Runtime.getRuntime().availableProcessors();
    //设置名称
    private static final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("EVANS-thread-pool-%d---").build();
    /**
     参数1:  corePoolSize：

     线程池的基本大小，即在没有任务需要执行的时候线程池的大小，并且只有在工作队列满了的情况下才会创建超出这个数量的线程。
     这里需要注意的是：
     在刚刚创建ThreadPoolExecutor的时候，线程并不会立即启动，而是要等到有任务提交时才会启动
     除非调用了prestartCoreThread/prestartAllCoreThreads事先启动核心线程。
     再考虑到keepAliveTime和allowCoreThreadTimeOut超时参数的影响，所以没有任务需要执行的时候，线程池的大小不一定是corePoolSize。
     参数2:  maximumPoolSize：
     线程池中允许的最大线程数，线程池中的当前线程数目不会超过该值。如果队列中任务已满，并且当前线程个数小于maximumPoolSize，那么会创建新的线程来执行任务。
     这里值得一提的是largestPoolSize，该变量记录了线程池在整个生命周期中曾经出现的最大线程个数。为什么说是曾经呢？因为线程池创建之后，
     可以调用setMaximumPoolSize()改变运行的最大线程的数目。
     参数3:  keepAliveTime：
     如果一个线程处在空闲状态的时间超过了该属性值，就会因为超时而退出。举个例子，如果线程池的核心大小corePoolSize=5，而当前大小poolSize =8，
     那么超出核心大小的线程，会按照keepAliveTime的值判断是否会超时退出。如果线程池的核心大小corePoolSize=5，而当前大小poolSize =5，
     那么线程池中所有线程都是核心线程，这个时候线程是否会退出，取决于allowCoreThreadTimeOut。
     参数4:  TimeUnit unit, 上一个参数（keepAliveTime）的单位
     参数5:  BlockingQueue  阻塞队列  通过BlockingQueue暂存还没有来得及执行的任务。  构造传入大小
     参数6:  ThreadFactory 线程工厂，传入Guava创建的ThreadFactoryBuilder 这里可以用其他的
     参数7： RejectedExecutionHandler 拒绝执行策略 多种
     */
    public static final ExecutorService executorService = new ThreadPoolExecutor(1, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(16), namedThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());


    // 进度条参数
    public static int TIMER_COUNT = 0;
    public static int CLASS_COUNT = 0;
    public static int JAVA_COUNT = 0;

}
