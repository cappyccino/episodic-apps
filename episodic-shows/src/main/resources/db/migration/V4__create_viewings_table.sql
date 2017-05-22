CREATE TABLE viewings (
  id bigint NOT NULL auto_increment PRIMARY KEY,
  show_id bigint,
  user_id bigint,
  episode_id bigint,
  updated_at timestamp,
  timecode int(11),
  foreign key (show_id) references shows(id),
  foreign key (user_id) references users(id),
  foreign key (episode_id) references episodes(id)
);