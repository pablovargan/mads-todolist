
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












