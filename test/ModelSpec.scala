package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import anorm._

import models.User
import models.Task

class ModelSpec extends Specification {

    def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str
    def strToDate(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

    "User" should {
        "Crear y buscar usuario" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                // La BD está vacía
                // LLamamos al modelo para crear un usuario

                User.create(User("pepito@gmail.com","1234"))

                // Comprobamos que se ha añadido un usuario en la BD 
                // Realizamos una actualización de la fila con esas
                // características y comprobamos que el número 
                // de filas actualizadas es 1

                val Some(user) = User.findByEmail("pepito@gmail.com")
                user.email must equalTo("pepito@gmail.com")
                user.password must equalTo("1234")
            }
        }

        "Autentificar usuario" in {
        	running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      			// Creo el usuario
        		User.create(User("pepito@gmail.com","1234"))
        		// Le mando un usuario creado anteriormente
        		val Some(user) = User.authenticate("pepito@gmail.com","1234")
        		// Compruebo si lo autentifica como usuario
        		user.email must equalTo("pepito@gmail.com")
                user.password must equalTo("1234")
        	}
        }
    }

    "Task" should {
        "Encontrar una tarea" in {
          running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
            // Busco una tarea
            val Some(task) = Task.findById(1)
            // Compruebo si existe y es esa
            task.label must equalTo("Jugar a Padel")
            dateIs(task.finishDate.get, "1991-01-01") must beTrue
            task.usuario must equalTo("andres@ua.es")
          }
        }
        
        "Crear tarea" in {
        	running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      			//Creo la tarea
      			Task.create(Task(anorm.NotAssigned,"Comer kebab",Option(strToDate("1991-01-01")), "andres@ua.es"))
      			// Busco la tarea
      			val Some(task) = Task.findById(15)
      			//Compruebo que existe
      			task.label must equalTo("Comer kebab")
      			dateIs(task.finishDate.get, "1991-01-01") must beTrue
      			task.usuario must equalTo("andres@ua.es")
        	}
        }

        "Eliminar tarea" in {
          running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
            // Saco una lista de tareas de ese usuario
            val lista1:List[Task] = Task.all(Some(1), "andrea@ua.es")
            // Elimino una tarea de ese usuario
            Task.delete(8);
            // La lista no debe ser igual ya que tiene un elemento menos
            lista1 must not be equalTo(Task.all(Some(1), "andrea@ua.es"))
          }
        }

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

        "Tarea relacionada con un usuario" in {
          running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
            // Busco la tarea con id que corresponde a este email
            val propietario:Boolean = Task.isOwnerOf(3, "felipe@ua.es")
            propietario must beTrue;
          }
        }

        "Orden normal" in {
          running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
            // Creo una lista
            val listaNormal: List[Task] = Task.order("cristina@ua.es")
            val t: Task = listaNormal.head
            // Compruebo el valor de la primera tarea de la lista por orden de insercion
            t.label must equalTo("Comprar aceite")
            dateIs(t.finishDate.get, "1985-01-01") must beTrue
            t.usuario must equalTo("cristina@ua.es")
          }
        }

        "Orden ascendente" in {
          running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
            // Creo una lista
            val listaNormal: List[Task] = Task.orderByASC("cristina@ua.es")
            val t: Task = listaNormal.head
            // Compruebo el valor de la primera tarea de la lista que esta ordenada por fecha
            t.label must equalTo("Jugar a rugby")
            dateIs(t.finishDate.get, "1982-08-01") must beTrue
            t.usuario must equalTo("cristina@ua.es")
          }
        }
    }
}