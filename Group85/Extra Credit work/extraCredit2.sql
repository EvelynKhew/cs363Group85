select count(distinct tid)
from tweet;

select * from tweet;
select * from mentions;
select posting_user from tweet;
select count(distinct hastagname)
from hasTags;

select distinct sub_category from users;

select * 
from hasUrls;

select count(distinct url)
from hasUrls;

select count(distinct screen_name)
from users;


 select * from tweet;
 
 select screen_name from users where screen_name = 'realDonaldTrump';
 
 use Group85;
 select * from users where screen_name = 'JKeld';