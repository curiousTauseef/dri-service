package vphshare.driservice.task;

public interface Executable extends Runnable {

    void execute(TaskContext context);
}
