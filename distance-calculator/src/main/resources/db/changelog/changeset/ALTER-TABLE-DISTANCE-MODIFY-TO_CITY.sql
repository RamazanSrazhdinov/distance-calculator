--liquibase formatted sql
-- changeset ramazan:3
alter table distance
    modify to_city bigint unsigned not null;