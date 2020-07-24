package edu.zlj.bbdownload.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import edu.zlj.bbdownload.config.DownloadStatus;
import edu.zlj.bbdownload.entity.DownloadEntity;
import edu.zlj.bbdownload.entity.TaskEntity;
import edu.zlj.bbdownload.utils.FileUtils;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-23
 * Time: 15:20
 */
public class DownloadTask implements Callable<Boolean> {
    private TaskEntity taskEntity;
    private DownloadEntity downloadEntity;

    public DownloadTask(TaskEntity taskEntity, DownloadEntity downloadEntity) {
        this.downloadEntity = downloadEntity;
        this.taskEntity = taskEntity;
    }

    @Override
    public Boolean call() throws Exception {
        int count = 0;
        while (count < 5) {
            try {

                HttpUtil.downloadFile(downloadEntity.getUrl(), new File(downloadEntity.getPath(), downloadEntity.getFilename()));
//                FileUtils.download(downloadEntity.getUrl(), downloadEntity.getPath(),downloadEntity.getFilename());
                taskEntity.countDown();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                count++;
            }
        }
        taskEntity.setStatus(DownloadStatus.FAILED);

        return false;
    }
}
