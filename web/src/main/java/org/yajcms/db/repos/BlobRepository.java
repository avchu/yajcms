package org.yajcms.db.repos;

import org.springframework.data.repository.CrudRepository;
import org.yajcms.db.entities.BlobEntity;

public interface BlobRepository extends CrudRepository<BlobEntity, Long> {
    BlobEntity getByHash(String path);
}
