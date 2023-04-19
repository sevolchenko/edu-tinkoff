-- Create Tables
-- ================================================================
-- Document
create table subscription
(
    tg_chat_id bigint      not null,
    link_id    bigserial   not null,
    created_at timestamptz not null,

    primary key (tg_chat_id, link_id),

    foreign key (tg_chat_id) references tg_chat (tg_chat_id),
    foreign key (link_id) references link (link_id) on delete cascade
);