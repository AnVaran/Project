create table public.flower (
id serial,
flower_name varchar not null,
flower_color varchar not null,
flower_length varchar not null,
flower_price numeric(5,2),
primary key (id));

create table public.bouquet_wrapper (
id serial,
wrapper_name varchar not null,
wrapper_description varchar not null,
wrapper_material varchar not null,
wrapper_price numeric(5,2),
primary key (id));

create table public.bouquet_decoration (
id serial,
decoration_name varchar not null,
decoration_description varchar not null,
decoration_material varchar not null,
decoration_price numeric(5,2),
primary key (id));

create table public.user_roles (
id serial,
role_name varchar not null,
primary key (id));

create table public.flower_user (
id serial,
role_id int not null,
user_name varchar,
user_email varchar not null,
user_password varchar not null,
session_key varchar,
primary key (id),
FOREIGN key (role_id) REFERENCES user_roles(id));

create table public.bouquet_order (
id serial,
order_status varchar not null,
order_commentary varchar,
shipping_adress varchar not null,
primary key (id));

create table public.flower_bouquet (
id serial,
decoration_id int,
wrapper_id int,
bouquet_price numeric(5,2),
order_id bigint not null,
primary key (id),
FOREIGN key (decoration_id) REFERENCES bouquet_decoration(id) ON DELETE CASCADE,
FOREIGN key (order_id) REFERENCES bouquet_order(id) ON DELETE CASCADE,
FOREIGN key (wrapper_id) REFERENCES bouquet_wrapper(id) ON DELETE CASCADE);

create table public.flower_component (
id serial,
flower_quantyty int not null,
flower_id int not null,
bouquet_id bigint,
primary key (id),
FOREIGN key (flower_id) REFERENCES flower(id) ON DELETE CASCADE,
FOREIGN key (bouquet_id) REFERENCES flower_bouquet(id) ON DELETE CASCADE);

INSERT INTO public.flower (flower_name, flower_color, flower_length, flower_price) VALUES 
('Rose', 'RED', 'SHORT', 2),
('Rose', 'RED', 'MEDIUM', 3),
('Rose', 'RED', 'LONG', 4),
('Tulip', 'RED', 'SHORT', 2.50),
('Tulip', 'RED', 'MEDIUM', 3),
('Tulip', 'RED', 'LONG', 3.50),
('Rose', 'WHITE', 'SHORT', 2.5),
('Rose', 'WHITE', 'MEDIUM', 3.5),
('Rose', 'WHITE', 'LONG', 4.5),
('Camomile', 'WHITE', 'SHORT', 2),
('Camomile', 'WHITE', 'MEDIUM', 2),
('Camomile', 'WHITE', 'LONG', 3);

INSERT INTO public.bouquet_decoration (decoration_name, decoration_material, decoration_price, decoration_description) VALUES 
('Decorative butterfly', 'SYNTH_MATERIAL', 3.50, 'Medium sized synthetic butterfly.'),
('Paper star', 'PAPER', 1.50, 'Set of 3 paper stars.'),
('Wooden heart', 'WOODEN', 4, 'Small wooden heart on textile ribbon');

INSERT INTO public.bouquet_wrapper (wrapper_name, wrapper_material, wrapper_price, wrapper_description) VALUES 
('Styled newsaper', 'PAPER', 0.80, 'Paper wrapper styled as a newspaper'),
('Two layer paper ', 'PAPER', 1.5, 'Two laer paper. First layer - white, second - red'),
('Spring melody', 'PAPER_AND_TEXTILE', 2.50, 'Wrapping set. Consist of: white wrapping paper, textile tissue and white ribbon.');

INSERT INTO public.user_roles (role_name)
VALUES ('USER');