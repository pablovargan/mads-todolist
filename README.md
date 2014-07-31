# Corrección de la práctica

Calificación: **0,4**

Bien el resumen. Conciso, pero contiene todo lo importante. 

Nota sobre el uso Markdown: utiliza las citas (">") sólo cuando sea
una cita, no por defecto como has hecho.

30 de septiembre  
Domingo Gallardo

-----------


# Práctica 1: Introducción a Play Framework #
***

Para esta primera práctica de la asignatura de Metodologías Ágiles para el Desarrollo de Software se pide que realicemos Play framework con Scala y conozcamos de cerca su funcionamiento es cual vamos a explicar brevemente a continuación.
Hemos seguido el "tutorial" que tenemos en la página principal y nos sirve para conocer de cerca y explorar el framework y repasar algunos conceptos de Scala que dimos el año pasado.
El proyecto esta alojado en git y compartido junto con el profesor en Bitbucket. Para finalizar la aplicación se hace uso de la herramienta Heroku para desplegar la mini-aplicación.

- El url de heroku es el siguiente: [mads-todolist](http://murmuring-fortress-7964.herokuapp.com/tasks) 

### Your first Play application ###
>Empezamos con este nuevo framework de desarrollo web para Java y Scala en el que en sus inicios fue desarrollado como proyecto interno de una empresa y más tarde, liberado como Open Source. Es en la versión 2.0 cuando el creador de Scala Martin Odersky lo incluyó en el paquete [TypeSafe Stack](http://typesafe.com/stack).

>Primero, creamos dentro del directorio donde vayamos a crear el proyecto y desde el terminal ejecutamos *play new helloWorld*. Una vez creado el proyecto dentro del directorio con el *helloWorld* tendremos una serie de archivos y ficheros. Los más importantes son:

>- app/: es el corazón de nuestra aplicación, contiene los directorios del controlador, modelo y vista y desde donde se ejecuta nuestra aplicación de Scala.
>- conf/: contiene los archivos de configuración de la aplicación, en especial las definiciones de las rutas que efectuará nuestra aplicación para hacer las peticiones http y que serán a las que podremos acceder.
>- project: contiene los scripts para funcionar el framework, el cual es un [sbt](http://www.scala-sbt.org/release/docs/Getting-Started/Basic-Def.html) similar a un Maven para Scala.
>- public/: contiene los recursos públicos (estilos, imagenes...).

>Para poder ejecutar nuestra aplicación en modo desarrollo tenemos que ejecutar *play run* y en nuestro navegador podremos consultar nuestros resultados en esta dirección [http://localhost:9000](http://localhost:9000).

> Una vez definido en nuestra configuración de rutas que nuestro *controllers.Aplicaction.index*, vamos a explicar como vamos a construir nuestra aplicación:
>	
	package controllers
	import play.api._
	import play.api.mvc._
>
	object Application extends Controller {
  		def index = Action {
    		Ok("Hello World")
  		}
	}
>A través de objetos singleton creamos nuestra primera aplicación que será que cuando realicemos la petición al servidor devolverá un http de respuesta con el mensaje "Hello World!". Más adelante explicaré el uso de Controller, Action y Result, y como es interpretado en el framework. Ahora vamos a centrarnos en la integración de Scala en este framework.

> Scala es utilizado para integrar las características de la programación funcional y la programación orientada a objetos. Aprovechando que actualmente la programación funcional está de moda podemos decir que en el lado del backend podemos hacer uso de funciones de orden superior, inmutabilidad y manipular datos de una forma más rápida que con otros paradigmas y hacer más escalables e incluso paralelizar nuestras aplicaciones ante un número de peticiones muy elevado (por ejemplo, los servidores de juegos). Es por eso que al integrar este framework Scala podemos hacer conocer más de cerca este paradigma del lado del servidor.

### Actiones, Controllers and Results ###
> - *Action* es una función que toma un objeto singleton **Request** como parámetro(implícito o explícito) y lo devuelve. Esta función maneja las peticiones del navegador y genera el resultado en el servidor y enviado al cliente con código de respuesta **200 OK**.
> - *Controller* un objeto singleton que genera el valor del *Action*. Éste debe ser heredados en nuestros *object* para cuando el navegador realice las peticiones el *Controller* traduzca la petición y genere la respuesta.
> - *Results* simplemente es lo que se genera cuando el *controller* termina la función y es envíado como respuesta al cliente.


### HTTP Routing ###
> Es el encargado de traducir cada solicitud HTTP entrante a un *Action*. Estas rutas consiten en el método del protocolo con el URI y asociado con el *Action*. El URI al que acceder puede ser estático, tener partes dinámicas e incluso utilizando expresiones regulares para acceder al *Action*.
> 
> Los métodos soportados por el protocolo son los siguientes:
> 
> - GET
> - POST
> - PUT
> - DELETE
> - HEAD
> 
>  Estas rutas debemos especificarlas en el fichero *conf/routes* con la siguiente sintaxis:
>
	# Display a client.
	GET   /clients/:id          controllers.Clients.show(id: Long) 

> Por otro lado, también podemos hacer llamadas a un metodo que nos genere un *Action*, es decir una solicitud en la que el object al que accedemos nos devuelva un *Action*. El URI al que accedamos es opcional, si bien podemos descartar el URI, y el object le podemos pasar un id:Integer y este devolvernos el *Action* correspondiente o bien dentro del URI ir este id:Integer que nos devuelva la *Action* para acceder a la petición.

>Finalmente, en este apartado tenemos el enrutamiendo invertido, es decir el enrutado puede tambien generar la URL de acceso con una llamada de Scala. Este proceso, puede ser centralizado con tlas URI en un único archivo de configuración, además puede ser mas silencioso cuando refactorizas tu aplicación. Un ejemplo claro de este enrutamiento invertido es *Redirect to*:
>
	def helloBob = Action {
    	Redirect(routes.Application.hello("Bob"))    
	}
>
### Manipulating Results ###
> Cuando hablamos de manipulación de resultados nos referimos que el valor que tiene por ejemplo un String, en Scala podemos pasarlo a etiquetado en HTML (o XML) y así preparar la respuesta. Pero no solo podemos realizarlo para etiquetado, sino que además se puede manipilar el *header* o guardar/descartar cookies y acemas cambiar todo lo que tiene el template y generar uno nuevo. Se puede encontrar más información [aquí](http://www.playframework.com/documentation/2.1.x/ScalaResults).


# Corrección de la práctica

### Documentación

- Bien el resumen y un poco floja la explicación técnica. El apartado 6 es bastante confuso.

### Código e implementación

- El controlador `Application.tasks` debería tomar un parámetro `Option[Int]`. El código sería mucho más claro y conciso. En la vista debería haber dos enlaces que enviaran distintos parámetros a la petición GET.

- Un error en el formulario de nueva tarea debería volver a pedir el formulario en lugar de devolver la lista de tareas. 

         def save = Action { implicit request =>
                  taskForm.bindFromRequest.fold(
                      /** Mal: se debería devolver la vista de creación con los errores **/
                    errors => BadRequest(views.html.index(Task.all(or),orderBy)), //TODO: CHANGE REDIRECT ERROR
                    task => {
                      Task.create(task)
                      Redirect(routes.Application.tasks)
                    }
                  )
          }


- El *match* de `Task.all` debería contemplar todos los casos, para evitar un error cuando se le pase otro valor. En lugar de `None` se debería poner la opción por defecto (`case _`).

### Calificación

En general está bien, pero los errores y el problema de la documentación te bajan un poco la nota.

Calificación: **0,3**

24 de octubre de 2013  
Domingo Gallardo

------

-----------
# Práctica 2: Ampliando la aplicación mads-todolist #
***

En esta segunda práctica de la asignatura de Metodologías Ágiles para el Desarrollo de Software comenzamos a trabajar de manera no-guiada con Play framework con Scala añadiendo nuevas funcionalidades a la práctica anterior.
Partiendo de la base de los conocimientos adquiridos en la práctica anterior, del ejemplo "computer database" y de dos lecturas recomendadas (que explicaremos a continuación con más detalle) podemos implementar nuestra práctica.


El proyecto esta alojado en git y compartido junto con el profesor en Bitbucket. Para finalizar la aplicación se hace uso de la herramienta Heroku para desplegar la mini-aplicación.

- El url de heroku es el siguiente: [mads-todolist2](http://mighty-spire-9116.herokuapp.com/tasks) 

Pero antes de explicar la práctica voy a resumir las nuevas características aplicadas a nuestro proyecto.

### The template engine ###
Este potente motor de templates cuyo diseño se inspiró en ASP.NET tiene unas magnificas características que hacen que podamos trabajar de una manera más cómoda en nuestra vista. Estas son:

- Compacto, expresivo y fluido. El *parser* es lo suficientemente inteligente para deducir las partes de código que hacen referencia a la parte del controlador. Esto hace que podamos construir nuestro HTML de una manera más limpia con menos caracteres y un flujo de trabajo y codificación más rápido.
- Fácil de aprender e implementar ya que está especificado con un mínimo de conceptos.
- No forma parte del lenguaje sino que es una especificación que nos facilita además la integración del HTML con Scala.

¡Pero eso no es lo más importante!
Lo que si llama la atención es el uso de la *@* como algunos lo mencionan como 'caracter mágico'. La principal característica es que podemos convertir una declaración en dinámica accediendo a las propiedades de nuestro parametro y sin cerrar nuestro bloque de código ya que será el propio motor del template quien detectará el final. Esta característica la podemos encontrar en la vista de la práctica para acceder a las propiedades de nuestras tareas.

Más informacíon: [The template engine](http://www.playframework.com/documentation/2.2.x/ScalaTemplates).


### Handling form submission ###
Play Framework tiene unas características particulares para manejar los Forms de una aplicación web de forma fácil teniendo en cuenta la complejidad de este Form. Esta característica está basada en el enlazado de datos a través de peticiones POST en la que nuestra aplicación comprueba los valores que hay dentro para enlazarlos, en la mayoría de casos, con un objeto contenido dentro del Form. Estos objetos contenidos dentro del Form son instanciados desde el controlador de nuestra aplicación y ya manipulados desde el modelo de negocio.

Pasos a seguir:

1. Definir el form.
2. Definir las restricciones en el form.
3. Validar las acciones del form.
4. Mostrar el form en la vista de nuestra aplicación.
5. Procesar los resultados del form en la vista. 

Con un clase ya predefinida en nuestra vista, ahora en el controlador creamos la estructura del form que transformará los datos del form en una instancia de nuestra clase de la siguiente manera que "mapea" nuestro datos para asignarlos al objeto:

	val taskForm = Form(
	    mapping(
	      "id" -> ignored(NotAssigned:Pk[Long]),
	      "label" -> nonEmptyText,
	      "finishDate" -> optional(date("yyyy-MM-dd"))
	    ) (Task.apply)(Task.unapply)
  	)

Esta forma de "mapear" nuestros datos explícitamente con los métodos *apply* y *unapply* pueden hacerse también con tuplas aunque en la práctica no hemos trabajado con esta variante.

Para poder enlazar nuestros datos desde la vista debemos hacer uso de la propiedad *fold*, que será utilizado o bien para cuando hemos enlazado correctamente entre la visa y el controlador, o si falla para poder tratar el error de la manera que nosotros implementemos. Un ejemplo de ello lo tenemos en nuestra práctica:

	def update(id: Long) = Action { implicit request =>
	    taskForm.bindFromRequest.fold(
	      errors => BadRequest(views.html.edittask(id, errors)),
	      task => {
	        Task.update(id, task)
	        Redirect(routes.Application.tasks)
	      }
	    )
	}

Finalmente, para poder mostrar en la vista nuestro form debemos añadir el parámetro que haga la referencia a este y así desde el controlador pasarle el Form creado y pueda ser llamado desde la vista. Un ejemplo de lo comentado sería así:


- Desde el controlador:
 
		def create = Action {
    		Ok(views.html.newtask(taskForm))
  		}

- Desde la vista:

		@(taskForm: Form[Task])

		@form(routes.Application.save) {
	        
	        @inputText(taskForm("label"))
	        @inputText(taskForm("finishDate")) 
	        
	        <input type="submit" value="Create">
		}

Más informacíon: [Handling form submission](http://www.playframework.com/documentation/2.2.x/ScalaForms).


### Explicación del funcionamiento de la práctica ###

Se ha dividido esta práctica en 6 pasos. Estos son:

**1. y 2.** Hemos creado un Form con el objeto Task que contiene las propiedades para crear una tarea y se ha creado una pagina nueva que será pedida (GET) al servidor para crearla y posteriormente guardar estos datos (POST) que conectará con las dos Action que comunicarán al modelo de negocio la inserción en la base de datos y la otra nos llevará a la vista del HTML del form.

		//Display form to create the new task
	  	def create = Action {
	    	Ok(views.html.newtask(taskForm))
		  }
		//Handle the "new task form" submission
		def save = Action { implicit request =>
		  taskForm.bindFromRequest.fold(
		    errors => BadRequest(views.html.index(Task.all(or),orderBy)), //TODO: CHANGE REDIRECT ERROR
		    task => {
		      Task.create(task)
		      Redirect(routes.Application.tasks)
		    }
		  )
		}


**3 y 4.** Añadimos más funcionalidades a nuestra aplicación, en este caso a la fecha para editar una tarea y además la opción de *Editar*. Primeramente configuramos el enrutado para acceder a ese id de la tarea y entrar en un form (con una petición GET) y que nos permita guardar los cambios realizados de ese id y que haga un *update* de la base de datos (POST) a través de la comunicación con el controlador.

		//Update task from bbdd?
		def update(id: Long) = Action { implicit request =>
			taskForm.bindFromRequest.fold(
			  errors => BadRequest(views.html.edittask(id, errors)),
			  task => {
			    Task.update(id, task)
			    Redirect(routes.Application.tasks)
			  }
			)
		}

**5.** El cambio que se realiza en este paso es la de crear una tabla dinámica con las columnas de cada propiedad de nuestra tarea en vez de listarlas como anteriormente la habíamos creado. Para ello, le decimos las columnas que vamos a necesitar y luego mapeamos el contenido de cada tarea que tenemos y para "volcarla" en el HTML. Esto es:

	<table id="task table"
		border="1">
		<thead>
		    <tr>
		        <th>Task</th>
		        <th>Finish Date</th>
		        <th>Actions</th>
		    </tr>
		</thead>
		@tasks.map { task =>
		    <tbody>    
		        <tr>
		            <td>@task.label</td>
		            <td>@task.finishDate.map(_.format("dd MMM yyyy")).getOrElse { <em>-</em> }</td>
		            <td>@form(routes.Application.delete(task.id.get)) {
		                    <input type="submit" value="Delete">
		                }
		                @form(routes.Application.edit(task.id.get)) {
		                    <input type="submit" value="Edit">
		                }
		            </td>
		        </tr>
		    </tbody>
		}
	</table> 

**6.** En este último paso y el que más tiempo nos puede llevar es la de controlar el listado de las tareas con 2 opciones; por orden de creación o por fecha de finalización. Para controlar este estado creamos una variable de tipo entero para manejarla desde el controlador y nos servirá de estado para mostrar con un orden u otro cuando accedamos a la prágina principal (index.html). También en el controlador de nuestra aplicación creamos una variable de tipo Option[Int] en la que tendremos 2 valores disponibles(Some(1) y None) y que serán los que trasladaremos al modelo de negocio para que realize la sentencia SQL.
> En el controlador:
Pero todo esto es controlado desde el evento *order* de nuestro controlador que es el que cambia el estado del orden del siguiente click en la vista y cambia el estado de nuesto **Option[Int]** y es el que posteriormente será el encargado de decirle al modelo de negoció el tipo de sentencia SQL que queremos en nuestra aplicación.
 

> En el modelo: hacemos uso de pattern maching en nuestro método de la sentencia SQL para obtener el estado de nuestra variable Option[Int] y devolvemos la sentencia SQL asociada para mostrar en la vista una vez comunicado al controlador.

Finalmente, este cambio de la sentencia de muestra debemos aplicar unos pequeños cambios a las demas acciones de nuestra controladora para que con el estado que tenemos nos respete el orden que le hemos establecido.
 

----------

----------

## Corrección de la práctica


### Documentación

- Bien la explicación, aunque falta algo de código para ver los detalles de algunas funciones
- Falta alguna pequeña explicación de cómo has probado los cambios
- Muy bien la idea del log, con las fechas y las horas

### Código e implementación

- Bien la restricción de acceso a los controllers
- Bien los nuevos métodos en las clases modelo y la actualización de la BD

Algunes errores:

- La lista de tareas solo debería mostrar las tareas del usuario logeado
- El controller `edit` debería estar también restringido a los propietarios de las tareas

### Calificación

Muy bien, salvo los pequeños errores. Enhorabuena por el trabajo.

Calificación: **0,5**

11 de diciembre de 2013  
Domingo Gallardo



------
# Práctica 3: Múltiples usuarios #

Proyecto desplegado en heroku: http://mighty-spire-9116.herokuapp.com/


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

###Conclusión###
Técnicamente me ha llamado la atención el uso del parámetro by-name y del como guardar la coockie de la sesión de usuario en el navegador, ya que desconocía como implementarlo y su recuperación desde el método GetLoggedUsser.

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

-----

-----


## Corrección de la práctica

VARELA GANDIA, PABLO

[Bitbucket](https://bitbucket.org/pablovarela/mads-todolist-4)

### Documentación

- Algo escasa

### Código e implementación

- Pocos commits
- Bien los tests de los controladores
- Algo escasos los tests del modelo 
- Los tests de ordenación no prueban realmente que las tareas están ordenadas, faltaría un bucle para asegurarlo

### Calificación

Falta un poco de documentación y mejorar algo los tests del modelo. Buen trabajo todo lo demás.

Calificación: **0,45**

7 de enero de 2014  
Domingo Gallardo

------

# Práctica 4: Pruebas en Play Framework #

El objetivo de esta cuarta práctica es entender el funcionamiento de las pruebas en Play Framework utilizando el famework de pruebas *specs2*.
Al realizar esta práctica nos sirve para conocer como realizar pruebas de nuestras aplicaciones y además nos servirá para conocer los errores que me hemos cometido en las prácticas anteriores tanto de seguridad como de implementación y poder tratarlos y solucionarlos para sacar versiones previas de nuestra aplicación.

Para realizar estas pruebas he tenido que documentarme de estos 5 puntos que explicaré brevemente:

1. Testing your application

En este apartado encontramos una pequeña introducción para realizar las pruebas de test de nuestra aplicación en la que se nos menciona el uso del framework de pruebas *specs2* en la que estas pruebas se crearan y se ejecutaran por debajo de la aplicación para realizar las pruebas.
Para implementar estas pruebas disponemos de una clase que será donde dentro crearemos todas las pruebas unitarias que realizaremos sobre ese apartado de la aplicación y se irán ejecutando unas detrás de otras. Pero para que se ejecuten y se realicen las pruebas es necesario crear un *FakeApplication* y así usar los datos que tenemos por defecto en la base de datos.

2. Writing functional tests

Para escribir test realmente funcionales no nos debemos olvidar que debemos comprobar toda nuestra aplicación: las vistas, los controladores, los modelos y las rutas de acceso, es decir todo lo que haga funcionar la aplicación. En este apartado podemos ver ejemplos para ver como empezar a desarrollar nuestras primeras pruebas unitarias y tener un mínimo conocimiento.

3. Specs2 Quick Start

En este apartado es donde realmente empezamos a conocer más de cerca este framework que está diseñado más para BDD (Behavior-Driven-Development) y he podido conocer toda la sintaxis para seguir haciendo más pruebas en concreto. En concreto, para mis 2 clases que he realizado las pruebas he heredado de *Specification* y usado (como he comentado anteriormente) las pruebas unitarias en las que combino el texto de la especificación se intercale con el código.
Para hacer uso de este estilo primeramente tenemos que crear un bloque en la que crearemos el ejemplo y luego tenemos que verificar el resultado. Estps resultados pueden ser:

- Resultados estándar
- Resultados 'Match'
- Valores boleanos
- Propiedades de ScalaCheck 

Ejemplo:

		"Actualizar una tarea" in {
		    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
	            // Busco la tarea que voy a modificar y la guardo
	            val Some(taskAux) = Task.findById(2)
	            // Modifico la tarea que he guardado
	            Task.update(2, new Task(anorm.NotAssigned, "Comprar baguette", Option(strToDate("1980-05-01")), "andres@ua.es"), "andres@ua.es")
	            // Compruebo que no sean iguales
	            taskAux must not be equalTo (Task.findById(2))
	            // Ahora compruebo que se haya actualizado en la bbdd parametro a parametro
	            val Some(taskNueva) = Task.findById(2)
	            taskNueva.label must equalTo("Comprar baguette")
	            dateIs(taskNueva.finishDate.get, "1980-05-01") must beTrue
	            taskNueva.usuario must equalTo("andres@ua.es")
		    }
		} 

4. Matchers de specs2
Como pueden haber muchos resultados, este framework nos ayuda con estos matchers para comprobar resultados que han se adapten a nuestras necesidades y conociendo la sintaxis es similar a cuando llamas a un método durante el desarrollo para que te devuelva un valor que te de el resultado que esperas. Es decir, esto nos proviene pequeñas funciones que nos dan una solución rápida ya que están implementados. Los más usados en la práctica han sido: equalTo, beTrue, contain y BeTrue.

5. API códigos de estado HTTP
Este documento nos lleva a la API de códigos de estados y es imprescindible para comprobar cuando haces cualquier tipo de petición y comprobar el estado de la respuesta para ver si el resultado es el esperado.












