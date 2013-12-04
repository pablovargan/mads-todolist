# --- Sample dataset
# --- !Ups

insert into owner (email,password) values ( 'andres@ua.es','1234');
insert into owner (email,password) values ( 'felipe@ua.es','1234');
insert into owner (email,password) values ( 'pablo@ua.es','1234');
insert into owner (email,password) values ( 'jaime@ua.es','1234');
insert into owner (email,password) values ( 'sara@ua.es','1234');
insert into owner (email,password) values ( 'andrea@ua.es','1234');
insert into owner (email,password) values ( 'cristina@ua.es','1234');

insert into task (label,finishDate,usuario) values ('Jugar a Padel','1991-01-01', 'andres@ua.es');
insert into task (label,finishDate,usuario) values ('Comprar pan','2006-01-10', 'andres@ua.es');
insert into task (label,finishDate,usuario) values ('Jugar a Futsal','1980-05-01', 'felipe@ua.es');
insert into task (label,finishDate,usuario) values ('Comprar agua','1983-12-01', 'felipe@ua.es');
insert into task (label,finishDate,usuario) values ('Limpiar la cocina','2006-08-01', 'felipe@ua.es');
insert into task (label,finishDate,usuario) values ('Dormir','2005-01-22', 'felipe@ua.es');
insert into task (label,finishDate,usuario) values ('Jugar a Basket','2006-08-07', 'sara@ua.es');
insert into task (label,finishDate,usuario) values ('Jugar a Beisbol','1994-03-01', 'andrea@ua.es');
insert into task (label,finishDate,usuario) values ('Comprar papel','1991-01-01', 'andrea@ua.es');
insert into task (label,finishDate,usuario) values ('Ir al museo','1990-01-01', 'andrea@ua.es');
insert into task (label,finishDate,usuario) values ('Comprar aceite','1985-01-01', 'cristina@ua.es');
insert into task (label,finishDate,usuario) values ('Jugar a rugby','1982-08-01', 'cristina@ua.es');
insert into task (label,finishDate,usuario) values ('Ir al teato','1989-08-01', 'cristina@ua.es');
insert into task (label,finishDate,usuario) values ('Ir al aeropuerto','2000-03-24', 'cristina@ua.es');

# --- !Downs

delete from owner;
delete from task;