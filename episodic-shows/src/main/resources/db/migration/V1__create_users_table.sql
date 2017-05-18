CREATE TABLE users (
  id bigint NOT NULL auto_increment PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE
);