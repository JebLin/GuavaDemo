package indi.sword.guavademo.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author jeb_lin
 * @Date Created in 4:03 PM 12/07/2018
 * @MODIFIED BY
 */
public class GuavaCacheMapInfo<T>{

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCacheMapInfo.class);

    private long size;
    private long duration;
    private TimeUnit unit;
    private int initialCapacity;

    private Cache<String,T> cache;

    public GuavaCacheMapInfo(long size,long duration,TimeUnit unit,int initialCapacity){
        cache = CacheBuilder.newBuilder().maximumSize(size)
                .expireAfterAccess(duration, unit)
                .initialCapacity(initialCapacity)
                .removalListener(new RemovalListener<String, T>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, T> rn) {
                        if (LOGGER.isInfoEnabled()) {
                            LOGGER.info("remove cache {}:{}", rn.getKey(), rn.getValue());
                        }
                    }
                }).build();
    }

    public Object get(String key) {
        return StringUtils.isNotEmpty(key) ? cache.getIfPresent(key) : null;
    }

    public void put(String key, T value) {
        if (StringUtils.isNotEmpty(key) && value != null) {
            cache.put(key, value);
        }
    }

    public void remove(String key) {
        if (StringUtils.isNotEmpty(key)) {
            cache.invalidate(key);
        }
    }

    public void remove(List<String> keys) {
        if (keys != null && keys.size() > 0) {
            cache.invalidateAll(keys);
        }
    }
}