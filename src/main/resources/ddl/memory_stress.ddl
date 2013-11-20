CREATE TABLE 'memory_stress'
ROW KEY FORMAT (eid LONG, HASH(SIZE = 4))
WITH LOCALITY GROUP default (
  MAXVERSIONS = INFINITY,
  TTL = FOREVER,
  INMEMORY = false,
  FAMILY info (
    string "string"
  )
);
