
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

- 3:08 PM
	- Copiado los fragmentos de código del enunciado y realizadas las pruebas de login e implementado el logout.

13/11/2013

- 12:21 AM Login funcional y entra en sesión con seguridad de no insertar el mismo email para crear un usuario. 

- 3:25 PM Solo se puede crear tareas si estas logueado
TODO: SEGURIDAD!

- 4:34 PM Una vez logueado se ha modificado la entidad de negocio de la tarea para mostrar con sus ordenes aquellos que corresponden al usuario en concreto para mostrar sus tareas. 
- 5:47 PM Implementada la seguridad para editar y eliminar las tareas con sus cambios en la base de dato para acceder a ellas. Para las peticiones GET he comprobado unicamente que se esté logueado.

- 5:50 PM Finalizada la práctica y TODO: Documentación













