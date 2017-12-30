package org.yajcms.db.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;

@Data
@Entity
public class BlobEntity {
    @Id
    Long oid;
    @Column
    String path;
    @Column
    String hash;
    @Column
    String contentHash;
    @Column
    private byte[] source;

    @Override
    public String toString() {
        return "BlobEntity{" +
                "oid=" + oid +
                ", path='" + path + '\'' +
                ", hash='" + hash + '\'' +
                ", contentHash='" + contentHash + '\'' +
                ", source=" + Arrays.toString(source).length() +
                '}';
    }
}
