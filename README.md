

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
 











