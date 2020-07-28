package edu.zlj.bbdownload.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpException;
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
import java.net.URLEncoder;
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

                if (Strings.isNullOrEmpty(url)) {
                    continue;
                }
                String fileName = url.substring(url.lastIndexOf("/"));
                int count = 0;
                while (true) {
                    try {
                        HttpUtil.downloadFile(url, new File(path, fileName));
//                        FileUtils.download(url, path, fileName);
                        break;
                    } catch (Exception e) {
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            if (httpException.getMessage().contains("code: [301]")) {
                                url = url.replace("http", "https");
                            } else if(httpException.getMessage().contains("code: [404]")) {
                                url = url.replaceAll("\\+","%2b");
                            }
                        }

                        count++;
                        if (count >= 5) {
                            e.printStackTrace();
                            flag = false;
                            logger.info("[error] {}", url);
                            break;

                        }
                    }
                }
            }
        }
        if (flag) {
            taskEntity.countDown();
        } else {
            taskEntity.setStatus(DownloadStatus.FAILED);
            logger.info("download failed......[{}]", downloadEntity);
        }
        return flag;
    }

    public static void main(String[] args) {
        String url = "https://www.cv.nrao.edu/2cmVLBA/data/0003+380/2013_08_12/0003+380.u.2013_08_12.irad.png";
//        url="https://www.cv.nrao.edu/2cmVLBA/data/0502+049/2018_08_19/0502+049.u.2018_08_19.ict_color.png";
        /*try {
            FileUtils.download(url,"C:\\Users\\Lenovo\\temp\\0219+428(3C66A)\\bu7mm","a.png");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
//        HttpUtil.downloadFile(url,"C:\\Users\\Lenovo\\temp\\0219+428(3C66A)\\bu7mm\\b.png");
        String str = HttpUtil.get("https://www.cv.nrao.edu/2cmVLBA/data/0003%2b380/");

        System.out.println("str = " + str);
    }
}
