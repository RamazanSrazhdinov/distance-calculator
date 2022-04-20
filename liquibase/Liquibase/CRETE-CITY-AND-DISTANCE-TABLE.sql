--liquibase formatted sql
--changeset ramazan:1
create table city(
    id serial primary key,
    name varchar(255),
    latitude double,
    longitude double
);

create table distance(
    id serial primary key,
    from_city varchar(255),
    to_city varchar(255),
    distance double
);