create table activities
(
    id                          bigint auto_increment
        primary key,
    company_id                  bigint       not null,
    creative_company_user_id    bigint       not null,
    responsible_company_user_id bigint       not null,
    negotiation_id              bigint       not null,
    description                 varchar(255) not null,
    response                    varchar(255) null,
    type                        tinyint      not null,
    status                      tinyint      not null,
    scheduled                   datetime(6) not null,
    created_at                  datetime(6) not null,
    updated_at                  datetime(6) not null,
    finished_at                 datetime(6) null
);