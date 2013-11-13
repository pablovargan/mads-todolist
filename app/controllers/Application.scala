package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import anorm._
import models.Task
import models.User

object Application extends Controller {

  /* FORM'S */

  //Describe the task form
  val taskForm = Form(
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "label" -> nonEmptyText,
      "finishDate" -> optional(date("yyyy-MM-dd")),
      "usuario" -> nonEmptyText
    ) (Task.apply)(Task.unapply)
  )
  //Describe the task form
  val userForm = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    ) (User.apply)(User.unapply)
  )

  //Set order with id to order tasks
  var or: Option[Int] = None
  var orderBy:Int = 0

  def change(id:Int): Option[Int] = {
    if(id > 0) Some(1)
    else None
  }

  /* ACTIONS */

  /* TASK */
  def index = GetLoggedUser { user => implicit request =>
    Redirect(routes.Application.tasks)
  }
  // Handle value of order to comunicate on the view
  def tasks = GetLoggedUser { user => implicit request =>
    Ok(views.html.index(Task.all(or, user), orderBy, user))
  }
  // Display form to create the new task
  // Check user to create a task (Condition)
  def create = GetLoggedUser { user => implicit request =>
    Ok(views.html.newtask(taskForm, user))
  }
  // Handle the "new task form" submission
  def save() = Action { implicit request =>
	  taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.newtask(taskForm,"ERROR")),
	    task => {
	      Task.create(task)
	      Redirect(routes.Application.tasks)
	    }
	  )
  }
  // Delete task from bbdd?
  def delete(id: Long) = IsOwnerOf(id) { user => implicit request =>
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }
  
  // Edit task and rediret to Taks(index)
  def edit(id: Long) = GetLoggedUser { user => implicit request =>
    Task.findById(id).map { task =>
      Ok(views.html.edittask(id, taskForm.fill(task), user))
    }.getOrElse(NotFound)
  }
  // Update task from bbdd?
  def update(id: Long) = IsOwnerOf(id) { user => implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.edittask(id,errors,user)),
      task => {
        Task.update(id, task, user)
        Redirect(routes.Application.tasks)
      })
  }

  def order(id:Int) = Action {
    id match {
      case 1 => orderBy = 0
      case 0 => orderBy = 1
    }
    // Change status of 'or'
    or = change(id)
    Redirect(routes.Application.tasks)
  }

  /* USER */

  // Display form to create a new user
  def createUser = Action {
    Ok(views.html.newuser(userForm))
  }

  // Display a list of users
  def listUsers = Action { implicit request =>
    Ok(views.html.users(User.userList()))
  }

  // Handle the "new user form" submission
  def saveUser = Action { implicit request =>
    userForm.bindFromRequest.fold(
      errors => BadRequest(views.html.newuser(userForm)),
      user => {
        // Si ya existe el email no se puede crear el usuario ya que es PK
        User.findByEmail(user.email) match {
          case Some(usrfnd) => { Redirect(routes.Application.login) }
          case None => {
              User.create(user)
              Redirect(routes.Application.index).withSession("email" -> user.email)
          }
        }
      }
    )
  }

  /* LOGIN */

  def login = Action {
    Ok(views.html.loginUser(userForm))
  }

  // Authenticate
  def doLogin = Action { implicit request =>
    userForm.bindFromRequest.fold(
      errors => BadRequest(views.html.loginUser(errors)),
      user => {
        User.findByEmail(user.email) match {
          case Some(usrfnd) => if(usrfnd.password == user.password) {
                              Redirect(routes.Application.index).withSession("email" -> user.email)
                            }
                            else {
                              //Forbidden
                              Redirect(routes.Application.login)
                            }
          case None => Redirect(routes.Application.login)
        }
      } 
    )
  }

  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  /**
  * Retrieve the connected user email.
  */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
  * Redirect to login if the user in not authorized.
  */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  /** 
  * Check authenticated user
  */
  def GetLoggedUser (f: => String => Request[AnyContent] => Result) = 
    Security.Authenticated(username, onUnauthorized) { 
      user => Action(request => f(user)(request))
    }

  /**
  * Check if the connected user is a owner of the task
  */
  def IsOwnerOf(task: Long)(f: => String => Request[AnyContent] => Result) = 
    GetLoggedUser { user => request =>
      if(Task.isOwnerOf(task, user)) {
        f(user)(request)
      } else {
        Results.Forbidden("PROHIBIDO - NO TIENES ACCESO")
      }
    } 
}
  