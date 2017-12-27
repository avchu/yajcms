package org.yajcms.db.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
}
