package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import anorm._
import models.Task

object Application extends Controller {
  
  //Describe the task form
  val taskForm = Form(
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "label" -> nonEmptyText
    ) (Task.apply)(Task.unapply)
  )

  /* ACTIONS */

  //Redirect to list of tasks (application home)
  def index = Action {
    Redirect(routes.Application.tasks)
  }
  //Display a list of tasks
  def tasks = Action {
    Ok(views.html.index(Task.all()))
  }
  //Display form to create the new task
  def create = Action {
    Ok(views.html.newtask(taskForm))
  }
  //Handle the "new task form" submission
  def save = Action { implicit request =>
	  taskForm.bindFromRequest.fold(
	    errors => BadRequest(views.html.index(Task.all())), //TODO: CHANGE REDIRECT ERROR
	    task => {
	      Task.create(task.label)
	      Redirect(routes.Application.tasks)
	    }
	  )
  }
  //Delete task from bbdd?
  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }
}

  