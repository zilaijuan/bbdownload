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
@Service("moCrawlServer")
public class MOJAVECrawlServerImpl implements CrawlServer {
    private Logger logger = LoggerFactory.getLogger(MOJAVECrawlServerImpl.class);

    @Autowired
    private SourceCache sourceCache;

    private String url = "https://www.physics.purdue.edu/astro/MOJAVE/allsources.html";
    private String baseUrl = "https://www.physics.purdue.edu/astro/MOJAVE/";
    private String detailBaseUrl = "https://www.bu.edu/blazars/VLBA_GLAST/";

    @Override
    public List<SourceName> getSourceName() {
        List<SourceName> sourceNames = sourceCache.getSourceNamesFromCache(Constant.SOURCENAME_MOJAVE);
        if (sourceNames == null) {
            sourceNames = new ArrayList<>();
            Map<String, SourceName> sourceNamesMap = new LinkedHashMap<>();
            try {
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();
                Elements centers = doc.getElementsByTag("center");
                for (Element center : centers) {
                    Elements as = center.getElementsByTag("a");
                    for (Element a : as) {


                        SourceName sourceName = new SourceName();
                        sourceName.setName(a.text());
                        sourceName.setUrl(baseUrl + a.attr("href"));
                        sourceName.setId(DigestUtil.md5Hex(Constant.SOURCENAME_MOJAVE + a.text()));
                        sourceName.setSource(Constant.SOURCENAME_MOJAVE);
                        sourceNames.add(sourceName);
                        sourceNamesMap.put(sourceName.getId(), sourceName);
                    }
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
            Elements tables = doc.select("table");
            Element table = tables.get(tables.size() - 2);
            Elements trs = table.getElementsByTag("tr");
            for (int i = 1; i < trs.size(); i++) {
                Elements tds = trs.get(i).getElementsByTag("td");
                SourceDetail sourceDetail = new SourceDetail();
                sourceDetail.setId(UUID.randomUUID().toString());
                sourceDetail.setEpoch(tds.get(0).text());
                try {
                    sourceDetail.setD_viewImage(tds.get(6).child(0).attr("href"));
                    sourceDetail.setD_image_natweight(tds.get(6).child(1).attr("href"));
                } catch (Exception e) {
                    logger.info("no I Image (Nat. Weight);");
                }
                try {
                    sourceDetail.setD_image_tapered(tds.get(7).child(1).attr("href"));
                } catch (Exception e) {
                    logger.info("no Tapered I Image;");
                }
                try {
                    sourceDetail.setD_image_widefield(tds.get(8).child(0).attr("href"));
                } catch (Exception e) {
                    logger.info("no Tapered I Image (Widefield);");
                }
                try {
                    sourceDetail.setD_UVdata(tds.get(9).child(0).attr("href"));
                } catch (Exception e) {
                    logger.info("no Visibility Data;");
                }
                try {
                    sourceDetail.setD_image_stokes(tds.get(10).child(0).attr("href"));
                } catch (Exception e) {
                    logger.info("no Stokes I Radplot;");
                }
                try {
                    sourceDetail.setD_image_poi(tds.get(11).child(0).attr("href"));
                } catch (Exception e) {
                    logger.info("no Poi Image;");
                }


                sourceDetails.add(sourceDetail);
                sourceDetailsMap.put(sourceDetail.getId(), sourceDetail);
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
