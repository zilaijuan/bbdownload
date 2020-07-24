package edu.zlj.bbdownload.service;

import cn.hutool.crypto.digest.DigestUtil;
import edu.zlj.bbdownload.cache.SourceCache;
import edu.zlj.bbdownload.config.Constant;
import edu.zlj.bbdownload.entity.SourceDetail;
import edu.zlj.bbdownload.entity.SourceName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-22
 * Time: 16:12
 */
@Service
public class MOJAVECrawlServerImpl implements CrawlServer {
    private Logger logger = LoggerFactory.getLogger(MOJAVECrawlServerImpl.class);

    @Autowired
    private SourceCache sourceCache;

    private String url = "http://www.physics.purdue.edu/astro/MOJAVE/allsources.html";
    private String baseUrl = "https://www.bu.edu/blazars/";
    private String detailBaseUrl = "https://www.bu.edu/blazars/VLBA_GLAST/";

    @Override
    public List<SourceName> getSourceName() {
        List<SourceName> sourceNames = null;
//        List<SourceName> sourceNames = sourceCache.getSourceNamesFromCache(Constant.SOURCENAME_MOJAVE);
        if (sourceNames == null) {
            sourceNames = new ArrayList<>();
            Map<String, SourceName> sourceNamesMap = new LinkedHashMap<>();
            try {
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();
                Elements centers = doc.getElementsByTag("center");
                for (Element center : centers) {
                    
                }
                Elements sources = table.getElementsByTag("a");
                for (Element source : sources) {
                    SourceName sourceName = new SourceName();
                    sourceName.setName(source.text());
                    sourceName.setUrl(baseUrl + source.attr("href"));
                    sourceName.setId(DigestUtil.md5Hex(Constant.SOURCENAME_MOJAVE+ source.text()));
                    sourceName.setSource(Constant.SOURCENAME_MOJAVE);
                    sourceNames.add(sourceName);
                    sourceNamesMap.put(sourceName.getId(), sourceName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sourceCache.getSourceNameCache().put(Constant.SOURCENAME_MOJAVE, sourceNamesMap);
        }

        return sourceNames;
    }

    @Override
    public List<SourceDetail> getSourceDetail(String sourceNameID) {
        SourceName sourceName = sourceCache.getSourceNameFromCache(Constant.SOURCENAME_MOJAVE, sourceNameID);
        if (sourceName == null) {
            return null;
        }

        // 缓存中查找
        List<SourceDetail> sourceDetails = sourceCache.getSourceDetailsFromCache(sourceNameID);
        if (sourceDetails != null) {
            return sourceDetails;
        }
        sourceDetails = new ArrayList<>();
        String detailUrl = sourceName.getUrl();
        Map<String, SourceDetail> sourceDetailsMap = new LinkedHashMap<>();
        try {
            Document doc = Jsoup.connect(detailUrl).get();
            String title = doc.title();
            Element table = doc.selectFirst("table");
            Elements trs = table.getElementsByTag("tr");
            for (Element tr : trs) {
                Elements tds = tr.getElementsByTag("td");
                if (tds.size() < 10) {
                    continue;
                }

                SourceDetail sourceDetail = new SourceDetail();
                sourceDetail.setId(UUID.randomUUID().toString());
                sourceDetail.setEpoch(tds.get(0).text());
                sourceDetail.setiPeak(tds.get(1).text());
                sourceDetail.setpPeak(tds.get(2).text());
                sourceDetail.setEVPA(tds.get(3).text());
                sourceDetail.setViewImage(detailBaseUrl + tds.get(4).child(0).attr("href"));
                sourceDetail.setPsFile(detailBaseUrl + tds.get(5).child(0).attr("href"));
                sourceDetail.setFitsFile(detailBaseUrl + tds.get(6).child(0).attr("href"));
                try {
                    sourceDetail.setpMap(detailBaseUrl + tds.get(7).child(0).attr("href"));
                } catch (Exception e) {
//                    e.printStackTrace();
                    logger.info("no ps file;");
                }
                sourceDetail.setUVdata(detailBaseUrl + tds.get(8).child(0).attr("href"));
                sourceDetail.setCLEANModel(detailBaseUrl + tds.get(9).child(0).attr("href"));


                sourceDetails.add(sourceDetail);
                sourceDetailsMap.put(sourceDetail.getId(),sourceDetail);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sourceCache.getSourceDetailCache().put(sourceNameID, sourceDetailsMap);
        return sourceDetails;
    }

    public static void main(String[] args) {
        MOJAVECrawlServerImpl b = new MOJAVECrawlServerImpl();
        b.getSourceName();
//        b.getSourceDetail("https://www.bu.edu/blazars/VLBA_GLAST/0316.html");
    }
}
