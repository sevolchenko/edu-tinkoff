-- Create Sequences
-- ================================================================
create sequence link_sequence start with 1 increment by 1;

-- Create Tables
-- ================================================================
-- Document
create table link
(
    link_id         integer      not null default nextVal('link_sequence'),
    url             varchar(255) not null unique,
    last_scanned_at timestamptz  not null, -- Время последней проверки данных по ссылке
    created_at      timestamptz  not null,

    constraint link_pk primary key (link_id)
);