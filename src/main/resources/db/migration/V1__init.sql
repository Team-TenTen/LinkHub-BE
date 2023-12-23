drop table if exists spaces;
create table spaces
(
    id                   bigint auto_increment
    primary key,
    created_at           timestamp                                                                                            null,
    updated_at           timestamp                                                                                            null,
    is_deleted           bit                                                                                                  not null,
    category             enum ('ENTER_ART', 'ETC', 'HOBBY_LEISURE_TRAVEL', 'KNOWLEDGE_ISSUE_CAREER', 'LIFE_KNOWHOW_SHOPPING') null,
    description          varchar(255)                                                                                         null,
    favorite_count       bigint                                                                                               not null,
    is_comment           bit                                                                                                  not null,
    is_link_summarizable bit                                                                                                  not null,
    is_read_mark_enabled bit                                                                                                  not null,
    is_visible           bit                                                                                                  not null,
    member_id            bigint                                                                                               not null,
    scrap_count          bigint                                                                                               not null,
    space_name           varchar(255)                                                                                         not null,
    view_count           bigint                                                                                               not null,
    FULLTEXT INDEX `ft_space_name` (`space_name`) WITH PARSER `ngram`
                                                      )
    ENGINE=InnoDB
    DEFAULT CHARACTER SET = utf8;
SET GLOBAL innodb_ft_enable_stopword = 0;

drop table if exists members;
create table members
(
    id            bigint auto_increment
    primary key,
    created_at    timestamp              null,
    updated_at    timestamp              null,
    is_deleted    bit                    not null,
    about_me      varchar(500)           null,
    is_subscribed bit                    null,
    news_email    varchar(320)           null,
    nickname      varchar(24)            null,
    provider      enum ('kakao')         not null,
    role          enum ('ADMIN', 'USER') not null,
    social_id     varchar(255)           not null
    );

drop table if exists profile_images;
create table profile_images
(
    id         bigint auto_increment
        primary key,
    created_at timestamp     null,
    updated_at timestamp     null,
    is_deleted bit           not null,
    name       varchar(255)  not null,
    path       varchar(2083) not null,
    member_id  bigint        not null
);

drop table if exists follows;
create table follows
(
    id           bigint auto_increment
        primary key,
    follower_id  bigint    null,
    following_id bigint    null,
    created_at   timestamp null,
    updated_at   timestamp null
);

drop table if exists favorite_categories;
create table favorite_categories
(
    id        bigint auto_increment
        primary key,
    category  enum ('ENTER_ART', 'ETC', 'HOBBY_LEISURE_TRAVEL', 'KNOWLEDGE_ISSUE_CAREER', 'LIFE_KNOWHOW_SHOPPING') not null,
    member_id bigint                                                                                               null
);

drop table if exists space_members;
create table space_members
(
    id         bigint auto_increment
        primary key,
    member_id  bigint                                 not null,
    space_id   bigint                                 not null,
    role       enum ('CAN_EDIT', 'CAN_VIEW', 'OWNER') null,
    created_at timestamp                              null,
    updated_at timestamp                              null,
    is_deleted bit                                    not null,
    constraint space_member_unique
        unique (space_id, member_id)
);

drop table if exists space_images;
create table space_images
(
    id         bigint auto_increment
        primary key,
    space_id   bigint        not null,
    path       varchar(2083) not null,
    name       varchar(255)  not null,
    created_at timestamp     null,
    updated_at timestamp     null,
    is_deleted bit           not null
);

drop table if exists links;
create table links
(
    id         bigint auto_increment
    primary key,
    created_at timestamp    null,
    updated_at timestamp    null,
    is_deleted bit          not null,
    like_count bigint       not null,
    member_id  bigint       not null,
    title      varchar(255) not null,
    url        varchar(255) null,
    space_id   bigint       not null
    );

drop table if exists tags;
create table tags
(
    id       bigint auto_increment
    primary key,
    color    enum ('BLUE', 'EMERALD', 'GRAY', 'INDIGO', 'PINK', 'PURPLE', 'RED', 'YELLOW') null,
    name     varchar(255)                                                                  not null,
    space_id bigint                                                                        not null
    );

drop table if exists link_tags;
create table link_tags
(
    id         bigint auto_increment
        primary key,
    created_at timestamp null,
    updated_at timestamp null,
    is_deleted bit       not null,
    link_id    bigint    not null,
    tag_id     bigint    not null
);

drop table if exists comments;
create table comments
(
    id                bigint auto_increment
    primary key,
    created_at        timestamp     null,
    updated_at        timestamp     null,
    is_deleted        bit           not null,
    content           varchar(1000) not null,
    group_number      bigint        null,
    member_id         bigint        not null,
    parent_comment_id bigint        null,
    space_id          bigint        not null
    );

drop table if exists favorites;
create table favorites
(
    id         bigint auto_increment
    primary key,
    member_id  bigint    not null,
    space_id   bigint    not null,
    created_at timestamp null,
    updated_at timestamp null,
    constraint space_member_unique
    unique (member_id, space_id)
    );

drop table if exists invitations;
create table invitations
(
    id              bigint auto_increment
    primary key,
    is_accepted     bit                                    not null,
    member_id       bigint                                 not null,
    notification_id bigint                                 not null,
    role            enum ('CAN_EDIT', 'CAN_VIEW', 'OWNER') null,
    space_id        bigint                                 not null
    );

drop table if exists likes;
create table likes
(
    id        bigint auto_increment
    primary key,
    member_id bigint not null,
    link_id   bigint not null,
    constraint like_unique
    unique (member_id, link_id)
    );

drop table if exists link_view_histories;
create table link_view_histories
(
    id         bigint auto_increment
    primary key,
    created_at timestamp null,
    updated_at timestamp null,
    member_id  bigint    not null,
    link_id    bigint    not null
    );

drop table if exists notifications;
create table notifications
(
    id                bigint auto_increment
    primary key,
    is_checked        bit                                      null,
    recipient_id      bigint                                   not null,
    sender_id         bigint                                   not null,
    notification_type enum ('COMMENT', 'FOLLOW', 'INVITATION') not null,
    created_at        timestamp                                null,
    updated_at        timestamp                                null
    );

drop table if exists scraps;
create table scraps
(
    id              bigint auto_increment
    primary key,
    member_id       bigint not null,
    source_space_id bigint not null,
    target_space_id bigint not null
);
