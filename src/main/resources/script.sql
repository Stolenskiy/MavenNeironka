create table if not exists located
(
	id serial not null
		constraint located_pk
			primary key,
	country varchar(50) not null,
	region varchar(50) not null,
	district varchar(50),
	street varchar(100),
	house_number varchar(10),
	floor smallint,
	room_number varchar(20)
);

comment on table located is 'Розташування торгових площадок';

comment on column located.floor is 'Поверх';

alter table located owner to "CossackProger";

create table if not exists trading_floor
(
	id serial not null
		constraint trading_floor_pk
			primary key,
	main_name varchar(200) not null,
	description varchar(5000),
	located_id integer not null
		constraint trading_floor_located_id_fk
			references located,
	price double precision default 0 not null,
	discont smallint default 0,
	number_of_square_meters double precision
);

comment on table trading_floor is 'Tоргова площадка';

alter table trading_floor owner to "CossackProger";

create table if not exists image
(
	id serial not null
		constraint image_pk
			primary key,
	expancion varchar(10) default 'jpg'::character varying not null
);

alter table image owner to "CossackProger";

create table if not exists images
(
	id serial not null
		constraint images_pk
			primary key,
	trading_floor_id integer
		constraint images_trading_floor_id_fk
			references trading_floor,
	image_id integer
		constraint images_image_id_fk
			references image
);

comment on table images is 'В цій таблиці будуть групуватись картинки по торгових площадках';

alter table images owner to "CossackProger";

create table if not exists "user"
(
	id serial not null
		constraint user_pk
			primary key,
	first_name varchar(20) not null,
	second_name varchar(30) not null,
	login varchar(30) not null,
	password varchar(30) not null,
	date_of_birthday date
);

alter table "user" owner to "CossackProger";

create unique index if not exists user_login_uindex
	on "user" (login);

create table if not exists customer
(
	id serial not null
		constraint customers_pk
			primary key,
	name varchar(100) not null,
	email varchar(40),
	phone varchar(15) not null,
	message varchar(1500),
	trading_floor_id integer
		constraint customers_trading_floor_id_fk
			references trading_floor
);

alter table customer owner to "CossackProger";

create table if not exists subscriber
(
	id serial not null
		constraint subscriber_pk
			primary key,
	date date,
	email varchar(30) not null
);

comment on table subscriber is 'Підписники';

alter table subscriber owner to "CossackProger";

create unique index if not exists subscriber_email_uindex
	on subscriber (email);

