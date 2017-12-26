package org.yajcms.beans;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.yajcms.core.Entity;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class EntityCache {

    Integer cacheExpirationInSeconds;

    MongoOperations mongoOperations;

    EntitiesInitializer entitiesInitializer;

    @Autowired
    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
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
                                return mongoOperations.findAll(Entity.class, key);
                            }

                            @Override
                            public ListenableFuture<List<Entity>> reload(String key, List<Entity> oldValue) throws Exception {
                                return super.reload(key, oldValue);
                            }
                        });

    }

    public List<Entity> getAll(String key) {
        return loadingCache.getUnchecked(key);
    }
}
