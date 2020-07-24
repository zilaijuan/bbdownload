package edu.zlj.bbdownload.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import edu.zlj.bbdownload.cache.DownloadTaskCache;
import edu.zlj.bbdownload.cache.SourceCache;
import edu.zlj.bbdownload.entity.DownloadEntity;
import edu.zlj.bbdownload.entity.SourceDetail;
import edu.zlj.bbdownload.entity.SourceName;
import edu.zlj.bbdownload.entity.TaskEntity;
import edu.zlj.bbdownload.task.DownloadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-23
 * Time: 14:53
 */
@Component
public class DownloadServer {
    private Logger logger = LoggerFactory.getLogger(DownloadServer.class);
    @Autowired
    private SourceCache sourceCache;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private DownloadTaskCache downloadTaskCache;

    public boolean preDownload(String[] ids, String source, String path, String sourceNameID) {
        if (ids.length == 0) {
            return false;
        }
        SourceName sourceName = sourceCache.getSourceNameFromCache(source, sourceNameID);
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTaskName(source + "_" + sourceName.getName() + "_" + DateUtil.now());
        File dir = new File(path, source);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                logger.info("can not create dir:[{}]", dir.toString());
                return false;
            }
        }
        for (String id : ids) {
            SourceDetail sourceDetail = sourceCache.getSourceDetailFromCache(sourceNameID, id);
            DownloadEntity downloadEntity = new DownloadEntity();
            downloadEntity.setPath(dir.toString());
            downloadEntity.setSourceDetail(sourceDetail);
            downloadEntity.setUrl(sourceDetail.getCLEANModel());
            taskEntity.add(downloadEntity);
        }

        download(taskEntity);
        return true;
    }

    private void download(TaskEntity taskEntity) {
        taskEntity.setTotal();
        BlockingQueue<DownloadEntity> tasks = taskEntity.getTasks();
        List<Future<Boolean>> results = new ArrayList<>();
        for (DownloadEntity task : tasks) {
            DownloadTask downloadTask = new DownloadTask(taskEntity, task);
            Future<Boolean> result = executorService.submit(downloadTask);
            results.add(result);
        }
        downloadTaskCache.add(taskEntity);
        for (Future<Boolean> result : results) {
            /*try {
                result.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/
        }

    }
}
