package edu.zlj.bbdownload.service;

import edu.zlj.bbdownload.cache.DownloadTaskCache;
import edu.zlj.bbdownload.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-24
 * Time: 12:45
 */
@Service
public class TaskServer {
    @Autowired
    private DownloadTaskCache downloadTaskCache;

    public List<TaskEntity> getTaskList(){
        return downloadTaskCache.getTask();
    }
}
