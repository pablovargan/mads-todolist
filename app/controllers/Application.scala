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
      "label" -> nonEmptyText,
      "finishDate" -> optional(date("yyyy-MM-dd"))
    ) (Task.apply)(Task.unapply)
  )
  //Handle from Option[Int], save Some(int)
  def orderBy(num:Int):Option[Int] = {
    if(num > 1) Some(1)
    else None
  }
  //Set order with id to order tasks
  var or:Option[Int] = orderBy(1)

  /* ACTIONS */

  //Redirect to list of tasks (application home)
  def index = Action {
    Redirect(routes.Application.tasks)
  }
  //Display a list of tasks
  def tasks = Action {
    or match {
      case Some(1) => Ok(views.html.index(Task.orderByASC()))
      case None => Ok(views.html.index(Task.all())) 
    }
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
	      Task.create(task)
	      Redirect(routes.Application.tasks)
	    }
	  )
  }
  //Delete task from bbdd?
  def delete(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }
  //Edit task and rediret to Taks(index)
  def edit(id: Long) = Action {
    Task.findById(id).map { task =>
      Ok(views.html.edittask(id, taskForm.fill(task)))
    }.getOrElse(NotFound)
  }
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

  def order = Action {
    Ok(views.html.index(Task.orderByASC()))
  }
}

  