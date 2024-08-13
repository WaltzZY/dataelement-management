package com.inspur.dsp.direct.conf;

import com.inspur.dsp.direct.util.Threads;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 功能描述: 线程池配置
 *
 * @author linkai
 * @date 2022年01月07日16:58:35
 */
@Configuration
@EnableAsync
@RequiredArgsConstructor
public class TaskExecutePoolConfig {

    // 核心线程池大小
    private int corePoolSize = 10;

    // 最大可创建的线程数
    private int maxPoolSize = 100;

    // 队列最大长度
    private int queueCapacity = 200;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 60;

    @Bean("asyncServiceExecutor")
    @Primary
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(corePoolSize);
        // 最大线程数
        taskExecutor.setMaxPoolSize(maxPoolSize);
        // 队列数
        taskExecutor.setQueueCapacity(queueCapacity);
        // 存活时间
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程前缀名
        taskExecutor.setThreadNamePrefix("dsp-enterprise-service-");
        // 线程抛弃策略
        // AbortPolicy：直接抛出异常，阻止系统正常运行。可以根据业务逻辑选择重试或者放弃提交等策略。
        // CallerRunsPolicy ：只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。不会造成任务丢失，同时减缓提交任务的速度，给执行任务缓冲时间。
        // DiscardOldestPolicy ：丢弃最老的一个请求，也就是即将被执行的任务，并尝试再次提交当前任务。
        // DiscardPolicy ：该策略默默地丢弃无法处理的任务，不予任何处理。如果允许任务丢失，这是最好的一种方案。
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 设置是否在关闭时等待计划任务完成，而不中断正在运行的任务和执行队列中的所有任务。
        taskExecutor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        taskExecutor.setAwaitTerminationSeconds(7200);
        return taskExecutor;
    }
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService()
    {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy())
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }
}
