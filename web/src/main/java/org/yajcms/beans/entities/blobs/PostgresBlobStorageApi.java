package org.yajcms.beans.entities.blobs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.yajcms.db.entities.BlobEntity;
import org.yajcms.db.repos.BlobRepository;

import java.util.UUID;

@Slf4j
public class PostgresBlobStorageApi implements BlobStorageApi {

    private BlobRepository blobRepository;

    @Autowired
    public void setBlobRepository(BlobRepository blobRepository) {
        this.blobRepository = blobRepository;
    }

    @Override
    public BlobEntity get(Long id) {
        BlobEntity byId = blobRepository.getByOid(id);
        if (byId == null) {
            throw new RuntimeException();
        }
        return byId;
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
    public Boolean delete(long id) {
        BlobEntity byId = blobRepository.getByOid(id);
        if (byId == null) {
            log.debug("blob not found: {}", id);
            return false;
        }
        log.debug("blob deleting: {}", id);
        blobRepository.delete(byId);
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
