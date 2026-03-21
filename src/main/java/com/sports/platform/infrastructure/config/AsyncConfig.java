package com.sports.platform.infrastructure.config;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    @Bean
    public ContextSnapshotFactory contextSnapshotFactory() {
        return ContextSnapshotFactory.builder().build();
    }

    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor(MeterRegistry registry, ContextSnapshotFactory contextSnapshotFactory) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("EmailTask-");
        executor.setTaskDecorator(new ContextPropagatingTaskDecorator(contextSnapshotFactory));
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();

        return ExecutorServiceMetrics.monitor(
                registry,
                executor.getThreadPoolExecutor(),
                "emailTaskExecutor");
    }

    static class ContextPropagatingTaskDecorator implements TaskDecorator {
        private final ContextSnapshotFactory snapshotFactory;

        public ContextPropagatingTaskDecorator(ContextSnapshotFactory snapshotFactory) {
            this.snapshotFactory = snapshotFactory;
        }

        @Override
        @NonNull
        public Runnable decorate(@NonNull Runnable runnable) {
            ContextSnapshot snapshot = this.snapshotFactory.captureAll();
            return () -> {
                try (ContextSnapshot.Scope scope = snapshot.setThreadLocals()) {
                    runnable.run();
                }
            };
        }
    }
}
