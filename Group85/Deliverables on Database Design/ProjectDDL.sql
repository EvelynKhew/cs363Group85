-- create schema to be used with example-load-in-file.
-- 
drop database if exists Group85;
CREATE DATABASE IF NOT EXISTS Group85;
USE Group85;

drop table if exists users;
create table users( 
	screen_name varchar(200),
	other_name varchar(200),
    sub_category varchar(10),
    category varchar(30),
    ofstate varchar(30),
    numFollowers int,
    numFollowing int,
    primary key(screen_name)
);

 DROP TABLE IF EXISTS tweet;
  CREATE TABLE tweet (
  tid bigint NOT NULL,
  textbody text,
  retweet_count int(11) DEFAULT NULL,
  retweeted tinyint(1) DEFAULT NULL,
  day_posted int DEFAULT NULL,
  month_posted int DEFAULT NULL,
  year_posted int default NULL,
  posting_user varchar(70) not null,
  PRIMARY KEY (tid),
  foreign key (posting_user) references users(screen_name) on delete cascade);

drop table if exists hasTags;
create table hasTags(
	tid bigint NOT NULL,
    hastagname varchar(200),
    primary key(tid, hastagname),
    foreign key(tid) references tweet(tid) on delete cascade
);

drop table if exists hasUrls;
create table hasUrls (
	tid bigint NOT NULL,
    url varchar(500),
    primary key(tid, url),
    foreign key(tid) references tweet(tid) on delete cascade
);

drop table if exists mentions;
create table mentions(
	tid bigint NOT NULL,
    screen_name varchar(200),
    primary key(tid, screen_name),
	foreign key(tid) references tweet(tid) on delete cascade,
	foreign key(screen_name) references users(screen_name) on delete cascade
);
