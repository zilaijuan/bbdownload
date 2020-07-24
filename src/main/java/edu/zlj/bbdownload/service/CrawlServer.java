package edu.zlj.bbdownload.service;

import edu.zlj.bbdownload.entity.SourceDetail;
import edu.zlj.bbdownload.entity.SourceName;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-22
 * Time: 10:45
 */

public interface CrawlServer {
    public List<SourceName> getSourceName();
    public List<SourceDetail> getSourceDetail(String url);
}
