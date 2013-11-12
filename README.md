
# Práctica 3: Múltiples usuarios#

31/10/2013 - Arreglados los errores y modificados los métodos recomendados por el profesor.

31/10/2013 - Creada la nueva tabla "owner" y modificado la tabla para que contenga la clave ajena.

	# --- !Ups
	CREATE TABLE owner (
		email varchar(255) NOT NULL,
		password varchar(255) NOT NULL,
		PRIMARY KEY(email)
	);
	
	ALTER TABLE task ADD user varchar(255);
	ALTER TABLE task ADD FOREIGN KEY (user) REFERENCES owner(email);
	
	# --- !Downs
	DROP TABLE owner;
	ALTER TABLE task DROP FOREIGN KEY user;
	ALTER TABLE task DROP user;

12/11/2013

 - 12:05 AM

 	- Creada la nueva clase en Modelo "User.scala" con el ResultSet y el método de crear un usuario y (adicional) muestra una lista de usuarios.
 	- Añadido al controlador los métodos de crear usuario y listarlos y configurado el enrutado para poder realizar las peticiones GET y POST.
 	- Crea un usuario (email, password) y te lleva a la lista de usuarios pero aún no entra en la sesión (TODO)













