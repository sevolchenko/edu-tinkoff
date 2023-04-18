
-- Create Tables
-- ================================================================
-- Document
create table chat_links
(
    chat_id bigint not null,
    link_id bigserial not null,

    primary key (chat_id, link_id),

    foreign key (chat_id) references chat (id),
    foreign key (link_id) references link (id)
);