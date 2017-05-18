CREATE TABLE episodes (
  id bigint NOT NULL auto_increment PRIMARY KEY,
  show_id bigint,
  episode_number int,
  season_number int,
  constraint fk_show_id foreign key (show_id) references shows (id)
);