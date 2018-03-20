# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table course (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  price_amount                  float,
  price_currency                integer,
  enrolment                     integer,
  constraint ck_course_price_currency check ( price_currency in (0,1,2,3)),
  constraint pk_course primary key (id)
);

create table course_user (
  course_id                     bigint not null,
  user_id                       bigint not null,
  constraint pk_course_user primary key (course_id,user_id)
);

create table course_module (
  course_id                     bigint not null,
  module_id                     bigint not null,
  constraint pk_course_module primary key (course_id,module_id)
);

create table lesson (
  id                            bigint auto_increment not null,
  ord                           integer,
  title                         varchar(255),
  content                       TEXT,
  constraint pk_lesson primary key (id)
);

create table lesson_module (
  lesson_id                     bigint not null,
  module_id                     bigint not null,
  constraint pk_lesson_module primary key (lesson_id,module_id)
);

create table module (
  id                            bigint auto_increment not null,
  namex                         varchar(255),
  constraint pk_module primary key (id)
);

create table module_course (
  module_id                     bigint not null,
  course_id                     bigint not null,
  constraint pk_module_course primary key (module_id,course_id)
);

create table module_lesson (
  module_id                     bigint not null,
  lesson_id                     bigint not null,
  constraint pk_module_lesson primary key (module_id,lesson_id)
);

create table user (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  username                      varchar(255),
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id)
);

create table user_course (
  user_id                       bigint not null,
  course_id                     bigint not null,
  constraint pk_user_course primary key (user_id,course_id)
);

alter table course_user add constraint fk_course_user_course foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_course_user_course on course_user (course_id);

alter table course_user add constraint fk_course_user_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_course_user_user on course_user (user_id);

alter table course_module add constraint fk_course_module_course foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_course_module_course on course_module (course_id);

alter table course_module add constraint fk_course_module_module foreign key (module_id) references module (id) on delete restrict on update restrict;
create index ix_course_module_module on course_module (module_id);

alter table lesson_module add constraint fk_lesson_module_lesson foreign key (lesson_id) references lesson (id) on delete restrict on update restrict;
create index ix_lesson_module_lesson on lesson_module (lesson_id);

alter table lesson_module add constraint fk_lesson_module_module foreign key (module_id) references module (id) on delete restrict on update restrict;
create index ix_lesson_module_module on lesson_module (module_id);

alter table module_course add constraint fk_module_course_module foreign key (module_id) references module (id) on delete restrict on update restrict;
create index ix_module_course_module on module_course (module_id);

alter table module_course add constraint fk_module_course_course foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_module_course_course on module_course (course_id);

alter table module_lesson add constraint fk_module_lesson_module foreign key (module_id) references module (id) on delete restrict on update restrict;
create index ix_module_lesson_module on module_lesson (module_id);

alter table module_lesson add constraint fk_module_lesson_lesson foreign key (lesson_id) references lesson (id) on delete restrict on update restrict;
create index ix_module_lesson_lesson on module_lesson (lesson_id);

alter table user_course add constraint fk_user_course_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_course_user on user_course (user_id);

alter table user_course add constraint fk_user_course_course foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_user_course_course on user_course (course_id);


# --- !Downs

alter table course_user drop foreign key fk_course_user_course;
drop index ix_course_user_course on course_user;

alter table course_user drop foreign key fk_course_user_user;
drop index ix_course_user_user on course_user;

alter table course_module drop foreign key fk_course_module_course;
drop index ix_course_module_course on course_module;

alter table course_module drop foreign key fk_course_module_module;
drop index ix_course_module_module on course_module;

alter table lesson_module drop foreign key fk_lesson_module_lesson;
drop index ix_lesson_module_lesson on lesson_module;

alter table lesson_module drop foreign key fk_lesson_module_module;
drop index ix_lesson_module_module on lesson_module;

alter table module_course drop foreign key fk_module_course_module;
drop index ix_module_course_module on module_course;

alter table module_course drop foreign key fk_module_course_course;
drop index ix_module_course_course on module_course;

alter table module_lesson drop foreign key fk_module_lesson_module;
drop index ix_module_lesson_module on module_lesson;

alter table module_lesson drop foreign key fk_module_lesson_lesson;
drop index ix_module_lesson_lesson on module_lesson;

alter table user_course drop foreign key fk_user_course_user;
drop index ix_user_course_user on user_course;

alter table user_course drop foreign key fk_user_course_course;
drop index ix_user_course_course on user_course;

drop table if exists course;

drop table if exists course_user;

drop table if exists course_module;

drop table if exists lesson;

drop table if exists lesson_module;

drop table if exists module;

drop table if exists module_course;

drop table if exists module_lesson;

drop table if exists user;

drop table if exists user_course;

