package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.Date

case class Task(id: Pk[Long] = NotAssigned, label: String, finishDate: Option[Date], usuario: String)

object Task {
  
  //Parser from a ResultSet
  val task = {
    get[Pk[Long]]("id") ~ 
    get[String]("label") ~
    get[Option[Date]]("finishDate") ~ 
    get[String]("usuario") map {
      case id~label~finishDate~usuario => Task(id, label, finishDate, usuario)
    }
  }

  //Return a list of tasks
  def all(opt: Option[Int], usuario:String): List[Task] = {
    opt match { 
      case Some(1) => orderByASC(usuario)
      case _ => order(usuario)
    }
  }

  def order(usuario:String): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task where task.usuario = {usuario}").on('usuario -> usuario).as(task *)
  }
  //Handle list of task with the order propose by user
  def orderByASC(usuario:String): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task where task.usuario = {usuario} order by finishDate nulls last").on('usuario -> usuario).as(task *)
  }
  
  //Insert a new task
  def create(task: Task) {
    DB.withConnection { implicit c =>
      SQL(
        """
          insert into task(label, finishDate, usuario) values (
            {label}, {finishDate}, {usuario}
          )
        """
      ).on(
        'label -> task.label,
        'finishDate -> task.finishDate,
        'usuario -> task.usuario
      ).executeUpdate()
    }
  }

  //Delete a task
  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  //Retrieve a task from the id
  def findById(id: Long): Option[Task] = {
    DB.withConnection { implicit connection =>
      SQL("select * from task where id = {id}").on('id -> id).as(Task.task.singleOpt)
    }
  }

  //Update a task
  def update(id: Long, task: Task, usuario:String) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          update task
          set label = {label}, finishDate = {finishDate}
          where id = {id} and usuario = {usuario}
        """
      ).on(
        'id -> id,
        'label -> task.label,
        'finishDate -> task.finishDate,
        'usuario -> usuario
      ).executeUpdate()
    }
  }

  /**
   * Check if a user is the owner of this task
   */
  def isOwnerOf(id: Long, user: String): Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select count(task.id) = 1 from task
          where task.usuario = {email} and task.id = {id}

        """
      ).on(
        'id -> id,
        'email -> user
      ).as(scalar[Boolean].single)
    }
  }

}


