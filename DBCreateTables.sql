drop table if exists goods;
drop table if exists location;
drop table if exists type;

create table if not exists type(
                                   id serial primary key,
                                   name varchar not null
);

create table if not exists location(
                         id serial primary key,
                         address varchar not null
);

create table if not exists goods(
                                    id serial primary key,
                                    address int references location (id) not null ,
                                    name int references type (id) not null ,
                                    quantity int not null,
                                    constraint goods_unique_constraint unique (address, name)
);
