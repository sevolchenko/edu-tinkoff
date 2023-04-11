
-- Create Tables
-- ================================================================
-- Document
create table chat_links
(
    chat_id bigint not null,
    link_id bigserial not null,

    constraint chat_links_pk primary key (chat_id, link_id)
);

alter table chat_links
    add constraint chat_id_fk
    foreign key (chat_id)
    references chat (id);

alter table chat_links
    add constraint link_id_fk
    foreign key (link_id)
    references link (id);
