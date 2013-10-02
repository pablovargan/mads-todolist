package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import java.util.{Date}

case class Task(id: Pk[Long] = NotAssigned, label: String, finishDate: Option[Date])

object Task {
  
  //Parser from a ResultSet
  val task = {
    get[Pk[Long]]("id") ~ 
    get[String]("label") ~
    get[Option[Date]]("finishDate") map {
      case id~label~finishDate => Task(id, label, finishDate)
    }
  }

  //Return a list of tasks
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  } 

  //Insert a new task
  def create(task: Task) {
    DB.withConnection { implicit c =>
      SQL(
        """
          insert into task(label, finishDate) values (
            {label}, {finishDate}
          )
        """
      ).on(
        'label -> task.label,
        'finishDate -> task.finishDate
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

  //Retrieve a computer from the id
  def findById(id: Long): Option[Task] = {
    DB.withConnection { implicit connection =>
      SQL("select * from task where id = {id}").on('id -> id).as(Task.task.singleOpt)
    }
  }

  //Update a task
  def update(id: Long, task: Task) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          update task
          set label = {label}, finishDate = {finishDate}
          where id = {id}
        """
      ).on(
        'id -> id,
        'label -> task.label,
        'finishDate -> task.finishDate
      ).executeUpdate()
    }
  }

}


