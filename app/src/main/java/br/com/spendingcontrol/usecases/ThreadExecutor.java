package br.com.spendingcontrol.usecases;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutor implements Executor {
    private static ThreadExecutor threadExecutor;

    private ExecutorService executorService;

    private ThreadExecutor() {
        executorService = Executors.newFixedThreadPool(3);
    }

    public static Executor getInstance() {
        if (threadExecutor == null) {
            threadExecutor = new ThreadExecutor();
        }
        return threadExecutor;
    }

    @Override
    public void execute(final UseCase useCase) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
    }
}
