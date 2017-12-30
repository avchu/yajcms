package org.yajcms.beans;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import io.vavr.collection.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yajcms.core.Entity;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class EntityCache {

    Integer cacheExpirationInSeconds;

    EntitiesStorage entitiesStorage;

    EntitiesInitializer entitiesInitializer;

    @Autowired
    public void setEntitiesStorage(EntitiesStorage entitiesStorage) {
        this.entitiesStorage = entitiesStorage;
    }

    @Autowired
    public void setEntitiesInitializer(EntitiesInitializer entitiesInitializer) {
        this.entitiesInitializer = entitiesInitializer;
    }


    @Value("${ogr.yajcms.core.cache.expiration:60}")
    public void setCacheExpirationInSeconds(Integer cacheExpirationInSeconds) {
        this.cacheExpirationInSeconds = cacheExpirationInSeconds;
    }

    Ticker ticker = Ticker.systemTicker();

    /**
     * Override for tests
     *
     * @param ticker
     */
    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    LoadingCache<String, List<Entity>> loadingCache;

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
                                return entitiesStorage.getAllByKey(key);
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
