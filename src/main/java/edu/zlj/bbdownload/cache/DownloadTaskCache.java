package edu.zlj.bbdownload.cache;

import edu.zlj.bbdownload.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-24
 * Time: 11:00
 */
@Component
public class DownloadTaskCache {
    private LinkedList<TaskEntity> taskCache;
    public DownloadTaskCache(){
        taskCache = new LinkedList<>();
    }
    public boolean add(TaskEntity taskEntity){
        taskCache.addFirst(taskEntity);
        return true;
    }

    public List<TaskEntity> getTask(){
        return taskCache;
    }
}
