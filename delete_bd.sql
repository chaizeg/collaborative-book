alter table stories drop constraint fk_users_stories;
alter table paragraphs drop constraint fk_users_paragraphs;
alter table paragraphs drop constraint fk_stories_paragraphs;
alter table choices drop constraint fk_stories_choices;
alter table choices drop constraint fk_source_choices;
alter table choices drop constraint fk_destination_choices;
alter table invites drop constraint fk_stories_invites;
alter table invites drop constraint fk_users_invites;
alter table conditions drop constraint fk_stories_conditions;
alter table conditions drop constraint fk_subject_conditions;
alter table conditions drop constraint fk_object_conditions;
alter table history drop constraint  fk_users_history;
alter table history drop constraint fk_users_history;


drop table invites;
drop table choices;
drop table conditions;
drop table history;
drop table paragraphs;
drop table stories;
drop table users;

drop sequence users_seq;
drop sequence story_seq;