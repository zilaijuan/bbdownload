package edu.zlj.bbdownload.controller;

import edu.zlj.bbdownload.config.Constant;
import edu.zlj.bbdownload.entity.SourceDetail;
import edu.zlj.bbdownload.entity.SourceName;
import edu.zlj.bbdownload.entity.TaskEntity;
import edu.zlj.bbdownload.service.CrawlServer;
import edu.zlj.bbdownload.service.DownloadServer;
import edu.zlj.bbdownload.service.TaskServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-22
 * Time: 10:33
 */
@Controller("/")
//@ResponseBody
public class IndexController {
    @Autowired
    private CrawlServer bu7CrawlServer;
    @Autowired
    private CrawlServer moCrawlServer;
    @Autowired
    private DownloadServer downloadServer;
    @Autowired
    private TaskServer taskServer;

    @Value("${store.path}")
    private String path;

    @RequestMapping(value = {"/", "index"}, method = RequestMethod.GET)
    public String index(Model model) {
        // 加缓存，这样就不用每次都请求了。
        List<SourceName> sourceNames = bu7CrawlServer.getSourceName();
        List<SourceName> moSourceNames = moCrawlServer.getSourceName();

        model.addAttribute("sourceNames", sourceNames);
        model.addAttribute("moSourceNames", moSourceNames);
        return "index";
    }

    @RequestMapping(value = {"details"}, method = RequestMethod.GET)
    public String detail(Model model, String id, String source) {
        // 加缓存，这样就不用每次都请求了。
        List<SourceDetail> sourceDetails = null;
        if (Constant.SOURCENAME_BU3.equals(source)) {
            
        } else if (Constant.SOURCENAME_BU7.equals(source)) {
             sourceDetails = bu7CrawlServer.getSourceDetail(id);
            
        } else if (Constant.SOURCENAME_MOJAVE.equals(source)) {
            sourceDetails = moCrawlServer.getSourceDetail(id);
        }

        model.addAttribute("sourceDetails", sourceDetails);
        model.addAttribute("source", source);
        model.addAttribute("path", path);
        model.addAttribute("sourceNameID", id);
        return "details";
    }

    @RequestMapping(value = {"download"}, method = RequestMethod.POST)
    @ResponseBody
    public String download(String[] checkID, String source, String path, String sourceNameID) {
        downloadServer.preDownload(checkID, source, path, sourceNameID);
        return "success";
    }

    @GetMapping("tasklist")
    public String tasklist(Model model) {
        List<TaskEntity> taskList = taskServer.getTaskList();
        model.addAttribute("taskList", taskList);

        return "tasklist";
    }

}
