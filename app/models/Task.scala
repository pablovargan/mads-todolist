package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Task(id: Pk[Long] = NotAssigned, label: String)

object Task {
  
  //Parser from a ResultSet
  val task = {
    get[Pk[Long]]("id") ~ 
    get[String]("label") map {
      case id~label => Task(id, label)
    }
  }

  //Return a list of tasks
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  } 

  //Insert a new task
  def create(label: String) {
    DB.withConnection { implicit c =>
      SQL("insert into task (label) values ({label})").on(
        'label -> label
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
}

