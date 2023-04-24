-- Create Tables
-- ================================================================
-- Document
create table subscription
(
    tg_chat_id bigint      not null references tg_chat (tg_chat_id),
    link_id    bigserial   not null references link (link_id) on delete cascade,
    created_at timestamptz not null,

    primary key (tg_chat_id, link_id)
);