--liquibase formatted sql
-- changeset ramazan:2
alter table distance
    modify from_city bigint unsigned not null;