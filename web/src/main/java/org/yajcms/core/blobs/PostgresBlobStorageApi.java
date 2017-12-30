package org.yajcms.core.blobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.yajcms.db.entities.BlobEntity;
import org.yajcms.db.repos.BlobRepository;

import java.util.UUID;

@Slf4j
public class PostgresBlobStorageApi implements BlobStorageApi {

    BlobRepository blobRepository;

    @Autowired
    public void setBlobRepository(BlobRepository blobRepository) {
        this.blobRepository = blobRepository;
    }

    @Override
    public BlobEntity get(String path) {
        BlobEntity byHash = blobRepository.getByHash(DigestUtils.md5Hex(path));
        if (byHash == null) {
            throw new RuntimeException();
        }
        return byHash;
    }

    @Override
    public Boolean delete(String path) {
        BlobEntity byHash = blobRepository.getByHash(DigestUtils.md5Hex(path));
        if (byHash == null) {
            log.debug("blob not found: {}", path);
            return false;
        }
        log.debug("blob deleting: {}", byHash.toString());
        blobRepository.delete(byHash);
        return true;
    }

    @Override
    public BlobEntity put(String path, byte[] source) {
        BlobEntity blobEntity = new BlobEntity();
        blobEntity.setHash(DigestUtils.md5Hex(path));
        blobEntity.setPath(path);
        blobEntity.setContentHash(DigestUtils.md5Hex(source));
        blobEntity.setSource(source);
        blobEntity.setOid(UUID.randomUUID().getLeastSignificantBits());
        log.debug("blob puts: {}", blobEntity.toString());
        blobRepository.save(blobEntity);
        return blobEntity;
    }
}
