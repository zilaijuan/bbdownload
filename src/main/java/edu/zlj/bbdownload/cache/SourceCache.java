package edu.zlj.bbdownload.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.zlj.bbdownload.entity.SourceDetail;
import edu.zlj.bbdownload.entity.SourceName;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-23
 * Time: 10:26
 */
@Component
public class SourceCache {
    private Cache<String, Map<String, SourceName>> SourceNameCache;
    private Cache<String, Map<String, SourceDetail>> SourceDetailCache;

    public SourceCache() {
        SourceNameCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
        SourceDetailCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();

    }

    public SourceDetail getSourceDetailFromCache(String sourceNameID, String id) {
        Map<String, SourceDetail> sourceMap = SourceDetailCache.getIfPresent(sourceNameID);
        if (sourceMap != null) {
            SourceDetail sourceDetail = sourceMap.get(id);
            if (sourceDetail != null) {
                return sourceDetail;
            }
        }
        return null;
    }

    public List<SourceName> getSourceNamesFromCache(String key) {
        Map<String, SourceName> sourceNameMap = this.SourceNameCache.getIfPresent(key);
        if (sourceNameMap == null) {
            return null;
        }
        Collection<SourceName> values = sourceNameMap.values();
        if (values == null || values.size() == 0) {
            return null;
        }
        return new ArrayList<>(values);
    }

    public SourceName getSourceNameFromCache(String source, String id) {
        Map<String, SourceName> sourceMap = SourceNameCache.getIfPresent(source);
        if (sourceMap != null) {
            SourceName sourceName = sourceMap.get(id);
            if (sourceName != null) {
                return sourceName;
            }
        }
        return null;
    }

    public Cache<String, Map<String, SourceName>> getSourceNameCache() {
        return SourceNameCache;
    }

    public void setSourceNameCache(Cache<String, Map<String, SourceName>> sourceNameCache) {
        SourceNameCache = sourceNameCache;
    }

    public Cache<String, Map<String, SourceDetail>> getSourceDetailCache() {
        return SourceDetailCache;
    }

    public void setSourceDetailCache(Cache<String, Map<String, SourceDetail>> sourceDetailCache) {
        SourceDetailCache = sourceDetailCache;
    }

    public List<SourceDetail> getSourceDetailsFromCache(String sourceNameID) {
        Map<String, SourceDetail> sourceDetailMap = SourceDetailCache.getIfPresent(sourceNameID);
        if (sourceDetailMap == null) {
            return null;
        }
        Collection<SourceDetail> values = sourceDetailMap.values();
        if (values == null || values.size() == 0) {
            return null;
        }
        return new ArrayList<>(values);
    }
}
