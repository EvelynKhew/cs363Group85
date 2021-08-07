-- q3: Find k hashtags that appeared in the most number of states in a given year; list the total number of states the hashtag appeared, the list of the distinct
-- states it appeared (FL is the same as Florida), and the hashtag itself in descending order of the number of states the hashtag appeared.
-- This query finds k hashtags that are used across the most number of states, which could indicate a certain agenda. 
select count(distinct u.ofstate) as statenum, group_concat(distinct u.ofstate) as states, h.hastagname as name
from users u, tweet t, hastags h
where   t.tid = h.tid and u.screen_name = t.posting_user 
    and t.year_posted = 2016 
    and u.ofstate != 'na'
group by hastagname
order by statenum desc limit 5;

-- q7 Find k users who has used a given hashtag in a given state in a given month of a given year. Show the count of tweets posted with that hashtag along with the user's screen name and category in 
-- descending order of the tweet count. 
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

-- q9: Find top k most followed users in a given party. Show the user's screen name, the user's party, and the number of followers in descending order of the number of 
-- followers. 
select screen_name, sub_category, numFollowers
from (
	select screen_name, sub_category, numFollowers, row_number() over (order by numFollowers desc) as followers
	from users where sub_category = 'Democrat') followCount
where followers <=5;

-- q16 Show names and categories of k users along with the tweet text, retweet count, and the url used by the user a given month of a given year in descending order of the 
-- retweet count. 
select u.screen_name as user_name , u.category, t.textbody as texts, t.retweet_count as retweetCt, h.url as address 
from users u, tweet t, hasurls h
where u.screen_name = t.posting_user 
	and t.tid = h.tid 
    and t.month_posted = 2 
    and t.year_posted = 2016 
order by t.retweet_count desc limit 5; 

-- q18: Find k users who were mentioned the most in tweets of users of a given party in a given month of a given year. Show the user's screen name, user's state, and the 
-- list of the screen name of the user(s) who mentioned this user in descending order of the number of tweets mentioning this user. 
select um.screen_name as mentionedUser, um.ofstate as mentionedUserState, group_concat(distinct up.screen_name) as postingUsers
from users um, tweet t, mentions m, users up
where t.posting_user = up.screen_name
    and um.screen_name = m.screen_name
    and t.tid = m.tid
    and (up.sub_category = 'GOP' and t.month_posted = 1 and t.year_posted = 2016)
group by mentionedUser
order by count(distinct m.tid) desc limit 5;

-- q23 
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

insert into users values('JKeld', 'JKeld', 'na', '', 'IA', 23, 41);
delete from users where screen_name = 'realDonaldTrump';

   