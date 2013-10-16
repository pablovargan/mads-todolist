

-----------


# Práctica 2: Ampliando la aplicación mads-todolist #
***

En esta segunda práctica de la asignatura de Metodologías Ágiles para el Desarrollo de Software comenzamos a trabajar de manera no-guiada con Play framework con Scala añadiendo nuevas funcionalidades a la práctica anterior.
Partiendo de la base de los conocimientos adquiridos en la práctica anterior, del ejemplo "computer database" y de dos lecturas recomendadas (que explicaremos a continuación con más detalle) podemos implementar nuestra práctica.


El proyecto esta alojado en git y compartido junto con el profesor en Bitbucket. Para finalizar la aplicación se hace uso de la herramienta Heroku para desplegar la mini-aplicación.

- El url de heroku es el siguiente: [mads-todolist2](http://mighty-spire-9116.herokuapp.com/tasks) 

Considero que la mejor manera de explicar la práctica es empezar a explicar con un breve resumen de las nuevas características de las dos lecturas recomendadas por el profesor.

### The template engine ###

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









