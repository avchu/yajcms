GRANT ALL PRIVILEGES ON DATABASE yajcms TO yajcms;

create table blob_entity (
  oid        bigint CONSTRAINT firstkey PRIMARY KEY,
  path       varchar(255) NOT NULL,
  hash          varchar(255)  NULL,
  content_hash    varchar(255)  NULL,
  source BYTEA
);