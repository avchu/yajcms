package org.yajcms.controller.core;

import org.springframework.data.repository.CrudRepository;

public interface BlobRepository extends CrudRepository<BlobEntity, Long> {
    BlobEntity getByHash(String path);
}
