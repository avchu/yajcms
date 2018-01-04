package org.yajcms.beans.cache;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import io.vavr.collection.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yajcms.beans.entities.nosql.EntitiesInitializer;
import org.yajcms.beans.pipeline.EntitiesDao;
import org.yajcms.beans.entities.Entity;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class EntityCache {

    private Integer cacheExpirationInSeconds;

    private EntitiesDao entitiesDao;

    private EntitiesInitializer entitiesInitializer;

    @Autowired
    public void setEntitiesDao(EntitiesDao entitiesDao) {
        this.entitiesDao = entitiesDao;
    }

    @Autowired
    public void setEntitiesInitializer(EntitiesInitializer entitiesInitializer) {
        this.entitiesInitializer = entitiesInitializer;
    }


    @Value("${ogr.yajcms.core.cache.expiration:60}")
    public void setCacheExpirationInSeconds(Integer cacheExpirationInSeconds) {
        this.cacheExpirationInSeconds = cacheExpirationInSeconds;
    }

    private Ticker ticker = Ticker.systemTicker();

    /**
     * Override for tests
     *
     * @param ticker
     */
    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    private LoadingCache<String, List<Entity>> loadingCache;

    @PostConstruct
    public void initCache() {
        loadingCache = CacheBuilder.newBuilder()
                .concurrencyLevel(1)
                .ticker(ticker)
                .refreshAfterWrite(cacheExpirationInSeconds, TimeUnit.SECONDS)
                .build(
                        new CacheLoader<String, List<Entity>>() {
                            @Override
                            public List<Entity> load(String key) {
                                return entitiesDao.getAllByKey(key);
                            }

                            @Override
                            public ListenableFuture<List<Entity>> reload(String key, List<Entity> oldValue) throws Exception {
                                return super.reload(key, oldValue);
                            }
                        });
        Optional.ofNullable(entitiesInitializer.getEntities()).orElse(HashMap.empty()).values().forEach(e -> {
            if (e.getCache()) loadingCache.getUnchecked(e.getKey());
        });
    }

    public List<Entity> getAll(String key) {
        return loadingCache.getUnchecked(key);
    }
}