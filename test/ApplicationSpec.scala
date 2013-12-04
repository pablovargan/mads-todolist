package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import models.User
import models.Task

import controllers.Application

class ApplicationSpec extends Specification {

  "Renderizar" should {

    "Peticion que no existe" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/tareas")) must beNone        
      }
    }
    
    "Renderizar pagina login" in {
      running(FakeApplication()) {

        val Some(home) = route(FakeRequest(GET, "/login"))

        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must contain ("Login")
      }
    }

    "Renderizar pagina crear usuario" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/create")).get
        // Debe existir
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html") 
        contentAsString(home) must contain ("Add new user")
      }
    }

    "Renderizar pagina lista de usuarios" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/listUsers")).get
        // Debe existir
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html") 
        contentAsString(home) must contain ("List of users")
      }
    }
    
    "Renderizar pagina de tareas despues de loguearse" in {
      running(FakeApplication()) {
        val login = route(FakeRequest(GET, "/tasks").withSession("email" -> "jaime@ua.es")).get
        // Ahora compruebo que me lleva a la lista de tareas
        status(login) must equalTo(OK)
        contentType(login) must beSome.which(_ == "text/html")
        contentAsString(login) must contain ("jaime@ua.es")
        contentAsString(login) must contain ("0 task(s)")
      }
    }

    "Renderizar pagina de tareas sin loguearse" in {
      running(FakeApplication()) {
        // Sin sesion
        val req = route(FakeRequest(GET, "/tasks")).get
        // Debe lanzarme un rechazo redireccionandome

        status(req) must equalTo(SEE_OTHER)
        // Y me debe redireccionar al login
        redirectLocation(req) must equalTo(Some("/login"))
      }
    }

    "Renderizar pagina de una tarea nueva" in {
      running(FakeApplication()) {
        val newTask = route(FakeRequest(GET, "/new").withSession("email" -> "jaime@ua.es")).get
        status(newTask) must equalTo(OK)
        contentType(newTask) must beSome.which(_ == "text/html")
        contentAsString(newTask) must contain ("Add a new task, jaime@ua.es")
      }
    }
    
    "Renderizar pagina de una tarea nueva sin loguearse" in {
       running(FakeApplication()) {
        val newTaskWithout = route(FakeRequest(GET, "/new")).get
        // Me rechaza porque no existe ninguna sesion de usuario y me lleva al login
        status(newTaskWithout) must equalTo(SEE_OTHER)
        redirectLocation(newTaskWithout) must equalTo(Some("/login"))
      }
    }

    "Renderizar pagina de tareas despues de crear una tarea" in {
      running(FakeApplication()) {
        // Creo una nueva tarea de ese usuario
        val Some(newTask) = route(FakeRequest(POST, "/save").withFormUrlEncodedBody(("label", "Comprar pepinos"), 
          ("finishDate", "1988-05-01"), ("usuario", "jaime@ua.es")))
        status(newTask) must equalTo(SEE_OTHER)
        val tasksRender = route(FakeRequest(GET, "/tasks").withSession("email" -> "jaime@ua.es")).get
        // Y debe mostrarme una nueva tarea
        status(tasksRender) must equalTo(OK)
        contentAsString(tasksRender) must contain ("jaime@ua.es")
        contentAsString(tasksRender) must contain ("1 task(s)")
      }
    }

    "Renderizar al editar una tarea sin tener una sesion de usuario" in {
      running(FakeApplication()) {
        //Edito una sin estar logueado
        val editWithout = route(FakeRequest(GET, "/tasks/3").withFormUrlEncodedBody(("label", "Jugar a Balonmano"), ("finishDate", "19828-08-01"), ("usuario", "felipe@ua.es"))).get
        // No debe dejarme ir a la edicion y me redireccionará al login
        status(editWithout) must equalTo(SEE_OTHER)
        redirectLocation(editWithout) must equalTo(Some("/login"))
      }
    }

    "Renderizar eliminar una tarea y que se muestre una menos en la lista" in {
      running(FakeApplication()) {
        // Solicito la lista de tareas de un usuario
        val login = route(FakeRequest(GET, "/tasks").withSession("email" -> "cristina@ua.es")).get
        // Ahora compruebo que me lleva a la lista de tareas
        status(login) must equalTo(OK)
        contentType(login) must beSome.which(_ == "text/html")
        contentAsString(login) must contain ("cristina@ua.es")
        // Tiene 4 tareas
        contentAsString(login) must contain ("4 task(s)")
        // Ahora elimino una de ellas
        val deleteTask = route(FakeRequest(POST, "/tasks/13/delete").withSession("email" -> "cristina@ua.es")).get
        status(deleteTask) must equalTo(SEE_OTHER)
        // Vuelvo a pedir la lista y debe aparecer una menos
        val login2 = route(FakeRequest(GET, "/tasks").withSession("email" -> "cristina@ua.es")).get
         // Ahora compruebo que me lleva a la lista de tareas
        status(login2) must equalTo(OK)
        contentType(login2) must beSome.which(_ == "text/html")
        contentAsString(login2) must contain ("cristina@ua.es")
        // Y deben haber 3 tareas
        contentAsString(login2) must contain ("3 task(s)")
      }
    }
  }

  "Login" should {
    
    "Loguearse" in {
      running(FakeApplication()) {

        val Some(result) = route(
          FakeRequest(POST, "/login").withFormUrlEncodedBody(("email","felipe@ua.es"),("password","1234")))

        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must equalTo(Some("/"))
        session(result).apply("email") must equalTo("felipe@ua.es")
      }      
    }

    "Loguearse con cuenta desconocida" in {
      running(FakeApplication()) {

        val Some(result) = route(
          FakeRequest(POST, "/login").withFormUrlEncodedBody(("email","maria@ua.es"),("password","1234"))
          )
        // Debe volver al login puesto que no existe ese usuario
        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must equalTo(Some("/login"))
      } 
    }

    "Desloguearse" in {
      running(FakeApplication()) {
        val user = route(FakeRequest(GET, "/logout")).get
        headers(user).get("Set-Cookie").getOrElse("") contains "Expires"
      }     
    }

    "Crear usuario con campos correctos" in {
      running(FakeApplication()) {
        val Some(create) = route(
          // Peticion POST a crear el usuario
          FakeRequest(POST, "/saveUser").withFormUrlEncodedBody(("email", "alberto@ua.es"),("password", "1234")))
        status(create) must equalTo(SEE_OTHER)
        redirectLocation(create) must equalTo(Some("/"))
        session(create).apply("email") must equalTo("alberto@ua.es")
      }
    }

    "Crear un usuario con los datos de uno que ya existe" in {
      running(FakeApplication()) {
        val Some(fake) = route(
          // Peticion POST a crear el usuario
          FakeRequest(POST, "/saveUser").withFormUrlEncodedBody(("email", "felipe@ua.es"),("password", "1234")))
        status(fake) must equalTo(SEE_OTHER)
        // Si nos vuelve a redirigir al login es porque el usuario ya existe
        redirectLocation(fake) must equalTo(Some("/login"))
      }
    }

    "Crear un usuario con campos vacios" in {
      running(FakeApplication()) {
        val empty = route(
          FakeRequest(POST, "/saveUser").withFormUrlEncodedBody(("email", ""),("password", ""))).get
        status(empty) must equalTo(BAD_REQUEST)
        contentType(empty) must beSome.which(_ == "text/html") 
        contentAsString(empty) must contain ("Required")
      }
    }
  }

  "Tareas" should {
    "Crear tarea con sesion" in {
      running(FakeApplication()) {
        // Creo una nueva tarea de ese usuario
        val Some(newTask) = route(FakeRequest(POST, "/save").withFormUrlEncodedBody(("label", "Comprar tomates"), 
          ("finishDate", "1980-05-01"), ("usuario", "jaime@ua.es")).withSession("email" -> "jaime@ua.es"))
        status(newTask) must equalTo(SEE_OTHER)
        redirectLocation(newTask) must equalTo(Some("/tasks"))
      }
    }

    "Crear una tarea con campos vacios" in {
      running(FakeApplication()) {
        //Creo una nueva tarea vacia
        val emptyTask = route(FakeRequest(POST, "/save").withFormUrlEncodedBody(("label", ""), ("finishDate", ""), ("usuario", "jaime@ua.es")).withSession("email" -> "jaime@ua.es")).get
        // Rechaza la peticion porque hay errores y me da un mensaje de error en la cabecera
        status(emptyTask) must equalTo(BAD_REQUEST)
        contentAsString(emptyTask) must contain ("Add a new task, ERROR")
      }
    }

    "Editar una tarea de un usuario con sesion" in {
      running(FakeApplication()) {
        // Accedo a editar la tarea con la sesion de ese email con id 4
        val editTask = route(FakeRequest(POST, "/tasks/4").withFormUrlEncodedBody(("label", "Jugar a Basket"), ("finishDate", "1982-08-01"), ("usuario", "felipe@ua.es")).withSession("email" -> "felipe@ua.es")).get
        // Aceptará la peticion
        status(editTask) must equalTo(SEE_OTHER)
        // Y me redirige a nuestra vista de tareas
        redirectLocation(editTask) must equalTo(Some("/tasks"))
      }
    }

    "Editar una tarea de un usuario con campos vacios" in {
      running(FakeApplication()) {
        // Accedo a editar la tarea con la sesion de ese email con id 4
        val editEmptyTask = route(FakeRequest(POST, "/tasks/4").withFormUrlEncodedBody(("label", ""), ("finishDate", ""), ("usuario", "felipe@ua.es")).withSession("email" -> "felipe@ua.es")).get
        // Hay errores en el form y me genera un mensaje de que es necesario el label de la tarea
        status(editEmptyTask) must equalTo(BAD_REQUEST)
        contentType(editEmptyTask) must beSome.which(_ == "text/html") 
        contentAsString(editEmptyTask) must contain ("This field is required")
      }
    }

    "Eliminar una tarea de un usuario sin sesion" in {
      running(FakeApplication()) {
        // Le digo que me elimine una tarea pasandole un id y sin sesion
        val deleteTask = route(FakeRequest(POST, "/tasks/6/delete")).get
        // No me debe dejar 
        status(deleteTask) must equalTo(SEE_OTHER)
      }
    }
    
    "Eliminar una tarea de un usuarion con sesion" in {
      running(FakeApplication()) {
         val deleteTask = route(FakeRequest(POST, "/tasks/5/delete").withSession("email" -> "felipe@ua.es")).get
         // Me debe redirigir a la lista de tareas
         status(deleteTask) must equalTo(SEE_OTHER)
         redirectLocation(deleteTask) must equalTo(Some("/tasks"))
      }
    }
  }
}