create database if not exists `myblog`;
use `myblog`;
create table if not exists user
(
    id bigint(19) not null primary key,
    number varchar(45) not null,
    password varchar(65) not null,
    nick_name varchar(65) ,
    avatar_url mediumtext,
    email varchar(80),
    github varchar(80),
    sign varchar(200),
    insert_time datetime not null default current_timestamp,
    update_time datetime default current_timestamp,
    index (number),
    unique (number)
);

create table if not exists article
(
    id bigint(19) not null primary key,
    label varchar(40),
    label_count int,
    title varchar(200),
    img_url mediumtext,
    digest varchar(200),
    content longtext,
    insert_time datetime not null default current_timestamp,
    update_time datetime default current_timestamp,
    index (label)
);

create table if not exists message
(
    id bigint(19) not null primary key,
    content varchar(200) not null ,
    insert_time datetime not null default current_timestamp,
    update_time datetime default current_timestamp
);

create table if not exists page_info
(
    id bigint(19) not null primary key,
    name varchar(60) not null ,
    background_url mediumtext not null,
    description varchar(200),
    insert_time datetime not null default current_timestamp,
    update_time datetime default current_timestamp
);