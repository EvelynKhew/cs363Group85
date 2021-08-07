create index tags on hasTags(hastagname);
create index mthPost on tweet(month_posted);
create index yrPost on tweet(year_posted);

-- Justification for rewriting Q9 query:
-- With our original design, we already have a run time of 6ms for it, so there wasn't much that we could do. 
-- As such, we decided to redesign it to only use one select statement (no inner select statements) as we thought that it could push the execution time a little bit more. 