-- q3
-- the below query is the same as in Before.sql. 
-- It is optimised due to the "tags" index created in PhysicalDesign.sql
select count(distinct u.ofstate) as statenum, group_concat(distinct u.ofstate) as states, h.hastagname as name
from users u, tweet t, hastags h
where   t.tid = h.tid and u.screen_name = t.posting_user 
    and t.year_posted = 2016 
    and u.ofstate != 'na'
group by hastagname
order by statenum desc limit 5;

-- q7 
-- the below query is the same as in Before.sql. 
-- It is optimised due to the "tags" index created in PhysicalDesign.sql
select count(distinct t.tid) as tweet_count, u.screen_name, u.category
from users u, tweet t, hastags h
where u.screen_name = t.posting_user 
	and t.tid = h.tid 
	and t.month_posted = 2 
	and t.year_posted = 2016 
    and h.hastagname = 'GOPDebate' 
    and u.ofstate = 'NC' 
group by screen_name
order by tweet_count desc limit 5;

-- q9
-- rewrote schema 
select u.screen_name, u.sub_category, u.numFollowers
from users u 
where u.sub_category = 'GOP'
order by numFollowers desc
limit 5;

-- q16
-- the below query is the same as in Before.sql. 
-- It is optimised due to the "mthPost" index created in PhysicalDesign.sql
select u.screen_name as user_name , u.category, t.textbody as texts, t.retweet_count as retweetCt, h.url as address 
from users u, tweet t, hasurls h
where u.screen_name = t.posting_user 
	and t.tid = h.tid 
    and t.month_posted = 2 
    and t.year_posted = 2016 
order by t.retweet_count desc limit 5; 

-- q18
-- the below query is the same as in Before.sql. 
-- It is optimised due to the "mthPost" index created in PhysicalDesign.sql
select um.screen_name as mentionedUser, um.ofstate as mentionedUserState, group_concat(distinct up.screen_name) as postingUsers
from users um, tweet t, mentions m, users up
where t.posting_user = up.screen_name
    and um.screen_name = m.screen_name
    and t.tid = m.tid
    and (up.sub_category = 'GOP' and t.month_posted = 1 and t.year_posted = 2016)
group by mentionedUser
order by count(distinct m.tid) desc limit 5;

-- q23
-- the below query is the same as in Before.sql. 
-- It is optimised due to the "yrPost" index created in PhysicalDesign.sql
select  h.hastagname as name, count(distinct t.tid) as num_uses
from users u, tweet t, hastags h
where   t.tid = h.tid and u.screen_name = t.posting_user 
    and (t.month_posted = 1
    or t.month_posted = 2
    or t.month_posted = 3)
    and t.year_posted = 2016 
    and u.sub_category = 'GOP'
group by hastagname
order by num_uses desc limit 5;