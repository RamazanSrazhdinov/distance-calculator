--liquibase formatted sql
-- changeset ramazan:5
alter table distance
    add constraint distance_city_id_fk_2
        foreign key (to_city) references city (id)
            on update cascade on delete cascade;