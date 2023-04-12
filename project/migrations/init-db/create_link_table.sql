-- Create Sequences
-- ================================================================
create sequence link_sequence start with 1 increment by 1;

-- Create Tables
-- ================================================================
-- Document
create table link
(
    id         integer      not null default nextVal('link_sequence'),
    url        varchar(255) not null,
    scanned_at timestamptz  not null,

    constraint link_pk primary key (id)
);