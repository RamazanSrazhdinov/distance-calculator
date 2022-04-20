--liquibase formatted sql
-- changeset ramazan:4
alter table distance
    add constraint distance_city_id_fk
        foreign key (from_city) references city (id)
            on update cascade on delete cascade;