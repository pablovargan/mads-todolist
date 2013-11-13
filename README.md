
# Práctica 3: Múltiples usuarios #

Proyecto desplegado en heroku: //TODO: mirar log de heroku con errores


El objetivo de esta práctica ha sido ampliar la aplicación para que permita gestionar las tareas con diferentes usuarios por medio de una autentificación para restringir el uso a solo los usuarios.
Los pasos a realizar han sido decididos previamente a continuación de leer toda la documentación expuesta en el enunciado y priorizando las funciones ya que a medida que avanzaba en cada paso hacia pruebas para comprobar su funcionamiento. El orden a seguir ha sido el siguiente:

1. Ampliación de la base de datos.
2. Creación del modelo User.scala
3. Modificación de las rutas (GET, POST).
4. Implementación el login y registro.
5. Añadir la variable de sesión para mostrar y crear peticiones de un usuario.
6. Modificar los métodos editar y borrar para un usuario.
7. Añadir la seguridad a los métodos anteriores para que solo sean accesibles al usuario de la sesión.

Todo este orden se puede comprobar al final de la explicación ya que he escrito un pequeño *log* con los cambios que he ido haciendo en la práctica. Ahora contaré un poco más en detalle los pasos seguidos.


###1. Amplicación de la base de datos ###
Se ha añadido una tabla llamada owner con el email y el password y se he añadido como clave ajena esta tabla a la tarea.

###2. Creación del modelo User.scala ###
Una vez creada la nueva tabla he implementado el modelo de negocio que accede a la tabla creando los métodos necesarios como el *parser*, crear un usuario, buscar a un usuario y un método adicional; listar los usuarios.

###3. Modificación de las rutas (GET, POST)###
He creado dos pequeños bloques para el login y para el registro. En el primero, la petición GET que nos accede a la vista de loguearnos y luego la petición POST para realizar el ingreso a nuestras tareas y, una petición GET para desconectarnos. En el segundo bloque, está la petición GET para acceder al registro y la petición POST para crear un nuevo usuario.


###4. Implementación el login y registro###
Ya con la lógica implementada ahora he creado los métodos que he llamado en las rutas dentro del controlador y que realizaban las consultas al modelo User.scala .En este paso tan solo nos autentificabamos y nos redireccionaba a la lista de usuarios creados como parte de las pruebas.

###5. Añadir la variable de sesión para mostrar y crear peticiones de un usuario###
Este paso ha sido el más largo ya que hemos establecido la variable de sesión, listado las tareas por usuario y permitir crearlas. Vamos a entrar en más detalle:

- En el método que hace el login buscamos en la bbdd si existe por pattern matching establecemos la variable de sesión que pertenece al email del usuario o nos redirige a que insertemos otra vez los datos.
- En el método crear el usuario he detectado que antes de registrarnos debíamos comprobar si el email existe ya que la bbdd fallaba al encontrarse con 2 claves primarias iguales y por lo tanto al crear el registro he impuesto esa condición e igual que en el anterior método por pattern matching redireccionaba al login para establecer la sesión y entrar a nuestra lista de tareas, en este caso vacía.
- Bien, a continuación debemos crear tareas y que se mostrara en nuestro "listado". Es el paso más importante puesto que hay que modificar el crear la petición de la práctica anterior comprobando si estábamos logueados utilizando el parámetro by-name explicado en el enunciado da la posibilidad de recuperar el email del usuario para enviarlo a la lógica de la tarea y realizar la búsqueda en la bbdd y cargar el listado de tareas que hace referencia a nuestro /index que también he modificado siguiendo los pasos anteriores y también priorizando el orden de éstas.
- A la hora de crear las tareas es importante pasar el usuario a la vista para enviar el form con el usuario (aunque lo he ocultado) y realizar un form sin errores y completo para enviar a la lógica.
He realizado pruebas de login y de acceso sin registrar.

###6. Modificar los métodos editar y borrar para un usuario###
Ahora he modificado igual que en los métodos de login y registro en el controlador para acceder mediante la coockie de la sesión y pasarle el email almacenado en la lógica y acceder a las peticiones GET con el usuario en este caso en la vista ocultado (igual que para crear una tarea) y mandar los campos necesarios en la petición POST y editemos o borremos la tarea.

###7. Añadir la seguridad a los métodos anteriores para que solo sean accesibles al usuario de la sesión###
En el paso anterior tan solo comprobabamos si estábamos logueados como usuarios y por lo tanto, estando logueados podríamos ver las tareas de los demás usuarios. Por lo tanto en este paso he añadido la función dada en el enunciado en la que desde el controlador haciendo uso de el parámetro by-name desde el cual con el usuario y haciendo la petición nos devuelve el resultado para poder ya entrar en la lógica y acceder a editar y borrar nuestra tarea. Esto es, desde el parámetro isOwnerOf primeramente comprueba si estamos logueados como usuario, comprueba que esa tarea pertenece a nuestro usuario y el resultado es el acceso otra vez a la lógica de negocio para editar/borrar la tarea.

Con esto nos permite que si ponemos la URL en el navegador se haga esta comprobación y así añadir una seguridad para nuestros usuarios.

###LOG DE LA PRÁCTICA###
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













