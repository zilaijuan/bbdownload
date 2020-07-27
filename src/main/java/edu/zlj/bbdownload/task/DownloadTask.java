package edu.zlj.bbdownload.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.base.Strings;
import edu.zlj.bbdownload.config.DownloadStatus;
import edu.zlj.bbdownload.entity.DownloadEntity;
import edu.zlj.bbdownload.entity.SourceDetail;
import edu.zlj.bbdownload.entity.TaskEntity;
import edu.zlj.bbdownload.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-23
 * Time: 15:20
 */
public class DownloadTask implements Callable<Boolean> {
    private Logger logger = LoggerFactory.getLogger(DownloadTask.class);
    private TaskEntity taskEntity;
    private DownloadEntity downloadEntity;

    public DownloadTask(TaskEntity taskEntity, DownloadEntity downloadEntity) {
        this.downloadEntity = downloadEntity;
        this.taskEntity = taskEntity;
    }

    @Override
    public Boolean call() throws Exception {
        SourceDetail sourceDetail = downloadEntity.getSourceDetail();
        Class<? extends SourceDetail> aClass = sourceDetail.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        String path = downloadEntity.getPath();
        boolean flag = true;
        for (Field declaredField : declaredFields) {
            String fieldName = declaredField.getName();
            if (fieldName.startsWith("d_")) {
                String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = aClass.getMethod("get" + name);
                String url = (String) method.invoke(sourceDetail);
                String fileName = url.substring(url.lastIndexOf("/"));
                if(Strings.isNullOrEmpty(url)){
                    continue;
                }
                int count = 0;
                while (count < 5) {
                    try {
                        HttpUtil.downloadFile(url, new File(path, fileName));
                        flag = true;
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        count++;
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            taskEntity.countDown();
        } else {
            taskEntity.setStatus(DownloadStatus.FAILED);
            logger.info("download failed......[{}]",downloadEntity);
        }
        return flag;
    }
}
