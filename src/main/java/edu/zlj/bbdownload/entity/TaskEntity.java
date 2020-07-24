package edu.zlj.bbdownload.entity;

import edu.zlj.bbdownload.config.DownloadStatus;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-23
 * Time: 15:05
 */
public class TaskEntity {
    private String taskName;
    private BlockingQueue<DownloadEntity> blockingQueue;
    private DownloadStatus status;
    private int total;
    private int remain;
    public TaskEntity(){
        blockingQueue = new LinkedBlockingQueue();
        taskName = String.valueOf(System.currentTimeMillis());
        status=DownloadStatus.READY;
    }

    public boolean add(DownloadEntity downloadEntity){
        return blockingQueue.add(downloadEntity);
    }

    public void setTotal() {
        total = blockingQueue.size();
        remain=total;
    }

    synchronized public void countDown() {
        remain--;
        if(remain==0){
            this.status=DownloadStatus.SUCCESS;
        }
    }

    public BlockingQueue getTasks() {
        return blockingQueue;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }



    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }
}
