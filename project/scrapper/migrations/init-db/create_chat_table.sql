
-- Create Tables
-- ================================================================
-- Document
create table chat
(
    id       bigint      not null,
    username varchar(255),

-- Не обязательно хранить колонку username. Но тогда получается очень странная табличка с одной колонкой

    constraint chat_pk primary key (id)
);