# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table course (
  id                            bigint auto_increment not null,
  title                         varchar(255) not null,
  price_amount                  float not null,
  price_currency                integer not null,
  enrolments                    integer,
  constraint ck_course_price_currency check ( price_currency in (0,1,2,3)),
  constraint uq_course_title unique (title),
  constraint pk_course primary key (id)
);

create table lesson (
  id                            bigint auto_increment not null,
  ord                           integer not null,
  title                         varchar(255) not null,
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
  name                          varchar(255) not null,
  constraint pk_module primary key (id)
);

create table module_course (
  module_id                     bigint not null,
  course_id                     bigint not null,
  constraint pk_module_course primary key (module_id,course_id)
);

create table user (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  username                      varchar(255) not null,
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id)
);

create table user_course (
  user_id                       bigint not null,
  course_id                     bigint not null,
  constraint pk_user_course primary key (user_id,course_id)
);

alter table lesson_module add constraint fk_lesson_module_lesson foreign key (lesson_id) references lesson (id) on delete restrict on update restrict;
create index ix_lesson_module_lesson on lesson_module (lesson_id);

alter table lesson_module add constraint fk_lesson_module_module foreign key (module_id) references module (id) on delete restrict on update restrict;
create index ix_lesson_module_module on lesson_module (module_id);

alter table module_course add constraint fk_module_course_module foreign key (module_id) references module (id) on delete restrict on update restrict;
create index ix_module_course_module on module_course (module_id);

alter table module_course add constraint fk_module_course_course foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_module_course_course on module_course (course_id);

alter table user_course add constraint fk_user_course_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_course_user on user_course (user_id);

alter table user_course add constraint fk_user_course_course foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_user_course_course on user_course (course_id);


# --- !Downs

alter table lesson_module drop foreign key fk_lesson_module_lesson;
drop index ix_lesson_module_lesson on lesson_module;

alter table lesson_module drop foreign key fk_lesson_module_module;
drop index ix_lesson_module_module on lesson_module;

alter table module_course drop foreign key fk_module_course_module;
drop index ix_module_course_module on module_course;

alter table module_course drop foreign key fk_module_course_course;
drop index ix_module_course_course on module_course;

alter table user_course drop foreign key fk_user_course_user;
drop index ix_user_course_user on user_course;

alter table user_course drop foreign key fk_user_course_course;
drop index ix_user_course_course on user_course;

drop table if exists course;

drop table if exists lesson;

drop table if exists lesson_module;

drop table if exists module;

drop table if exists module_course;

drop table if exists user;

drop table if exists user_course;

