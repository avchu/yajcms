package org.yajcms.core.blobs;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yajcms.db.entities.BlobEntity;
import org.yajcms.db.repos.BlobRepository;

import java.util.UUID;

@Component
public class PostgresBlobStorageApi implements BlobStorageApi {

    @Autowired
    BlobRepository blobRepository;

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
            return false;
        }
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
        blobRepository.save(blobEntity);
        return blobEntity;
    }
}
