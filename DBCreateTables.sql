drop table if exists goods;
drop table if exists product;
drop table if exists location;
drop table if exists type;

create table if not exists type
(
    id   serial primary key,
    name varchar not null
);

create table if not exists product
(
    id      serial primary key,
    name    varchar                  not null,
    type_id int references type (id) not null
);

create table location
(
    id      serial primary key,
    address varchar not null
);

create table if not exists goods
(
    location_id int references location (id) not null,
    product_id  int references product (id)  not null,
    quantity    int                          not null
);

-- create index idx on goods (type_id);
