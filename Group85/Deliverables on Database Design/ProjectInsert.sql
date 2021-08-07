-- run forimport.sql
-- make sure that the file is named correctly.
-- for 2020 data, use ',' as the field terminator
-- for 2016 data, use ';' as the field terminator
-- for Linux, the line terminator may be different
-- 81906 rows are the correct number of rows to be imported
-- the REPLACE INTO TABLE is to empty the existing rows in the table before adding new rows.
-- the tweets.csv file must locate inside the specified folder.
-- find out where are the folder MySQL expects the data files to be.
-- to load data from a different folder beyond the default folder, MySQL server configuration file must be changed.

show variables like 'secure_file_priv';

use Group85;

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/user.csv' REPLACE INTO TABLE users
FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\n'
IGNORE 1 LINES
(screen_name,other_name,sub_category,category,ofstate,numFollowers,numFollowing);

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/tweets.csv' REPLACE INTO TABLE tweet
FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\n'
IGNORE 1 LINES
(tid,textbody,retweet_count,retweeted,@col5,posting_user)
set day_posted= day(str_to_date(@col5, '%Y-%m-%d %H:%i:%s')), month_posted= month(str_to_date(@col5, '%Y-%m-%d %H:%i:%s')), year_posted= year(str_to_date(@col5, '%Y-%m-%d %H:%i:%s'));


LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/tagged.csv' REPLACE INTO TABLE hasTags
FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\n'
IGNORE 1 LINES
(tid,hastagname);

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/urlused.csv' REPLACE INTO TABLE hasUrls
FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\n'
IGNORE 1 LINES
(tid,url);

LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/mentioned.csv' REPLACE INTO TABLE mentions
FIELDS TERMINATED BY ';' OPTIONALLY ENCLOSED BY '"' LINES TERMINATED BY '\n'
IGNORE 1 LINES
(tid, screen_name);