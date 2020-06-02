package br.com.spendingcontrol.usecases;

public abstract class UseCaseAbstract implements UseCase {
    private Executor executor;

    public UseCaseAbstract(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        executor.execute(this);
    }
}
