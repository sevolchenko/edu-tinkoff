-- Create Tables
-- ================================================================
-- Document
create table tg_chat
(
    tg_chat_id    bigint                   not null,
    username      varchar(255)             not null,
    registered_at timestamp with time zone not null,

    constraint tg_chat_pk primary key (tg_chat_id)
);